package com.example.handoverbackend.service.board;

import com.example.handoverbackend.domain.board.Board;
import com.example.handoverbackend.domain.board.Favorite;
import com.example.handoverbackend.domain.board.Image;
import com.example.handoverbackend.domain.category.Category;
import com.example.handoverbackend.domain.member.Member;
import com.example.handoverbackend.dto.board.*;
import com.example.handoverbackend.dto.page.PageInfoDto;
import com.example.handoverbackend.exception.BoardNotFoundException;
import com.example.handoverbackend.exception.CategoryNotFoundException;
import com.example.handoverbackend.exception.FavoriteNotFoundException;
import com.example.handoverbackend.exception.MemberNotEqualsException;
import com.example.handoverbackend.repository.board.BoardRepository;
import com.example.handoverbackend.repository.board.FavoriteRepository;
import com.example.handoverbackend.repository.category.CategoryRepository;
import com.example.handoverbackend.service.file.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.IntStream;

import static com.example.handoverbackend.domain.board.Board.*;
import static com.example.handoverbackend.domain.board.Favorite.*;

@Service
@RequiredArgsConstructor
public class BoardService {

    private static final String SUCCESS_CREATE_BOARD = "게시판이 등록되었습니다.";
    private static final String SUCCESS_UPDATE_BOARD = "게시판이 수정되었습니다.";
    private static final String SUCCESS_DELETE_BOARD = "게시판이 삭제되었습니다.";
    private static final String SUCCESS_CREATE_FAVORITE = "즐겨찾기를 하었습니다.";
    private static final String SUCCESS_DELETE_FAVORITE = "즐겨찾기가 해제되었습니다.";
    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final String DEFAULT_PAGE_SORT = "id";


    private final BoardRepository boardRepository;
    private final FavoriteRepository favoriteRepository;
    private final CategoryRepository categoryRepository;
    private final FileService fileService;

    @Transactional
    public String createBoard(BoardCreateRequestDto requestDto, Member member, Long categoryId) {
        List<Image> images = requestDto.getImages()
            .stream()
            .map(image -> new Image(image.getOriginalFilename()))
            .toList();
        Category category = getCategory(categoryId);
        Board board = Board.createBoard(requestDto.getTitle(), requestDto.getContent(), member, category, images);
        boardRepository.save(board);
        uploadImages(board.getImages(), requestDto.getImages());
        return SUCCESS_CREATE_BOARD;
    }

    @Transactional(readOnly = true)
    public BoardFindAllWithPagingResponseDto findAllBoardsWithCategory(Integer page, Long categoryId) {
        PageRequest pageRequest = getPageRequest(page);
        Page<Board> boards = boardRepository.findAllByCategoryId(categoryId, pageRequest);
        List<BoardFindAllResponseDto> boardsWithDto = boards.stream()
            .map(BoardFindAllResponseDto::toDto)
            .toList();
        return BoardFindAllWithPagingResponseDto.toDto(boardsWithDto, new PageInfoDto(boards));
    }

    @Transactional(readOnly = true)
    public BoardFindAllWithPagingResponseDto findAllBoards(Integer page) {
        PageRequest pageRequest = getPageRequest(page);
        Page<Board> boards = boardRepository.findAll(pageRequest);
        List<BoardFindAllResponseDto> boardsWithDto = boards.stream()
            .map(BoardFindAllResponseDto::toDto)
            .toList();
        return BoardFindAllWithPagingResponseDto.toDto(boardsWithDto, new PageInfoDto(boards));
    }

    @Transactional(readOnly = true)
    public BoardFindResponseDto findBoard(Long id) {
        Board board = boardRepository.findById(id)
            .orElseThrow(BoardNotFoundException::new);
        Member member = board.getMember();
        return BoardFindResponseDto.toDto(member.getUsername(), board);
    }

    @Transactional
    public String editBoard(Long id, BoardUpdateRequestDto requestDto, Member member) {
        Board board = boardRepository.findById(id)
            .orElseThrow(BoardNotFoundException::new);
        validateBoardWriter(board, member);
        ImageUpdatedResult result = board.update(requestDto);
        uploadImages(result.getAddedImages(), result.getAddedImageFiles());
        deleteImages(result.getDeletedImages());
        return SUCCESS_UPDATE_BOARD;
    }

    @Transactional
    public String deleteBoard(Long id, Member member) {
        Board board = boardRepository.findById(id)
            .orElseThrow(BoardNotFoundException::new);
        validateBoardWriter(board, member);
        deleteImages(board.getImages());
        boardRepository.delete(board);
        return SUCCESS_DELETE_BOARD;
    }

    @Transactional
    public String updateFavoriteBoard(Long id, Member member) {
        Board board = boardRepository.findById(id)
            .orElseThrow(BoardNotFoundException::new);
        if (hasFavoriteBoard(board, member)) {
            deleteFavoriteBoard(board, member);
            return SUCCESS_CREATE_FAVORITE;
        }
        createFavoriteBoard(board, member);
        return SUCCESS_DELETE_FAVORITE;
    }

    @Transactional(readOnly = true)
    public BoardFindAllWithPagingResponseDto findFavoriteBoards(Integer page, Member member) {
        PageRequest pageRequest = getPageRequest(page);
        Page<Favorite> favorites = favoriteRepository.findAllByMember(member, pageRequest);
        List<BoardFindAllResponseDto> boardsWithDto = favorites.stream()
            .map(Favorite::getBoard)
            .map(BoardFindAllResponseDto::toDto)
            .toList();
        return BoardFindAllWithPagingResponseDto.toDto(boardsWithDto, new PageInfoDto(favorites));
    }

    @Transactional(readOnly = true)
    public BoardFindAllWithPagingResponseDto searchBoard(String keyword, Integer page) {
        PageRequest pageRequest = getPageRequest(page);
        Page<Board> boards = boardRepository.findAllByTitleContaining(keyword, pageRequest);
        List<BoardFindAllResponseDto> boardWithDto = boards.stream()
            .map(BoardFindAllResponseDto::toDto)
            .toList();
        return BoardFindAllWithPagingResponseDto.toDto(boardWithDto, new PageInfoDto(boards));
    }

    private void deleteFavoriteBoard(Board board, Member member) {
        Favorite favorite = favoriteRepository.findByBoardAndMember(board, member)
            .orElseThrow(FavoriteNotFoundException::new);
        favoriteRepository.delete(favorite);
    }

    private void createFavoriteBoard(Board board, Member member) {
        Favorite favorite = createFavorite(board, member);
        favoriteRepository.save(favorite);
    }

    private boolean hasFavoriteBoard(Board board, Member member) {
        return favoriteRepository.findByBoardAndMember(board, member).isPresent();
    }

    private void validateBoardWriter(Board board, Member member) {
        if (!board.isOwnBoard(member)) {
            throw new MemberNotEqualsException();
        }
    }

    private PageRequest getPageRequest(Integer page) {
        return PageRequest.of(page, DEFAULT_PAGE_SIZE, Sort.by(DEFAULT_PAGE_SORT)
            .descending());
    }

    private Category getCategory(Long categoryId) {
        return categoryRepository.findById(categoryId)
            .orElseThrow(CategoryNotFoundException::new);
    }

    private void uploadImages(List<Image> images, List<MultipartFile> fileImages) {
        IntStream.range(0, images.size())
            .forEach(image -> fileService.upload(fileImages.get(image), images.get(image)
                .getUniqueName()));
    }

    private void deleteImages(List<Image> images) {
        images.forEach(image -> fileService.delete(image.getUniqueName()));
    }
}
