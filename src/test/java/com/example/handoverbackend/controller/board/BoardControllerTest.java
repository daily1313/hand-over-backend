package com.example.handoverbackend.controller.board;


import com.example.handoverbackend.domain.member.Member;
import com.example.handoverbackend.dto.board.BoardCreateRequestDto;
import com.example.handoverbackend.dto.board.BoardUpdateRequestDto;
import com.example.handoverbackend.repository.MemberRepository;
import com.example.handoverbackend.service.board.BoardService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.example.handoverbackend.factory.AuthenticationMaker.*;
import static com.example.handoverbackend.factory.ImageMaker.*;
import static com.example.handoverbackend.factory.MemberMaker.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class BoardControllerTest {

    @InjectMocks
    BoardController boardController;
    @Mock
    MemberRepository memberRepository;
    @Mock
    BoardService boardService;
    MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(boardController)
            .build();
    }

    @Test
    @DisplayName("게시판 생성")
    void createBoard() throws Exception {
        //given
        List<MultipartFile> images = createImages();
        BoardCreateRequestDto requestDto = new BoardCreateRequestDto("title", "content", images);
        Member member = createMember();
        Authentication authentication = createAuthentication(member);
        SecurityContextHolder.getContext()
            .setAuthentication(authentication);
        given(memberRepository.findByUsername(authentication.getName()))
            .willReturn(Optional.of(member));

        //when,then
        mockMvc.perform(
                multipart("/api/boards")
                    .file("images", images.get(0).getBytes())
                    .file("images", images.get(1).getBytes())
                    .param("title", requestDto.getTitle())
                    .param("content", requestDto.getContent())
                    .with(requestBoardProcessor -> {
                        requestBoardProcessor.setMethod("POST");
                        return requestBoardProcessor;
                    })
                    .contentType(MediaType.MULTIPART_FORM_DATA))
            .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("게시판 목록 조회")
    void findAllBoardsWithCategory() throws Exception {
        //given
        Long categoryId = 0L;
        Integer page = 0;

        //when
        mockMvc.perform(
                get("/api/boards/all{categoryId}", categoryId))
            .andExpect(status().isOk());

        //then
        verify(boardService).findAllBoardsWithCategory(page, categoryId);
    }

    @Test
    @DisplayName("게시판 전체 조회")
    void findAllBoards() throws Exception {
        //given
        Integer page = 0;

        //when
        mockMvc.perform(
                get("/api/boards"))
            .andExpect(status().isOk());

        //then
        verify(boardService).findAllBoards(page);
    }

    @Test
    @DisplayName("게시판 단건 조회")
    void findBoard() throws Exception {
        //given
        Long boardId = 1L;

        //when
        mockMvc.perform(
                get("/api/boards/{id}", boardId))
            .andExpect(status().isOk());


        //then
        verify(boardService).findBoard(boardId);
    }


    @Test
    @DisplayName("게시판 수정")
    void editBoard() throws Exception {
        //given
        Long boardId =1L;
        List<MultipartFile> addImages = createImages();
        addImages.add(new MockMultipartFile("test1","test1.jpg",MediaType.IMAGE_PNG_VALUE,"test1".getBytes()));
        List<Long> deletedImages = List.of(1L);
        BoardUpdateRequestDto req = new BoardUpdateRequestDto("title", "content", addImages, deletedImages);
        Member member = createMember();
        Authentication authentication = createAuthentication(member);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(member));

        //when, then
        mockMvc.perform(
            multipart("/api/boards/{id}", boardId)
                .file("addedImages", addImages.get(2).getBytes())
                .param("deletedImages", String.valueOf(deletedImages.get(0)))
                .param("title", req.getTitle())
                .param("content", req.getContent())
                .with(requestBoardProcessor -> {
                    requestBoardProcessor.setMethod("PATCH");
                    return requestBoardProcessor;
                })
                .contentType(MediaType.MULTIPART_FORM_DATA)
        ).andExpect(status().isOk());
    }

    @Test
    @DisplayName("게시판 삭제")
    void deleteBoard() throws Exception {
        //given
        Long boardId = 1L;
        Member member = createMember();
        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getId(), "", Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(member));

        //when
        mockMvc.perform(
            delete("/api/boards/{id}", boardId)
        ).andExpect(status().isOk());

        //then
        verify(boardService).deleteBoard(boardId,member);
    }

    @Test
    @DisplayName("게시판 즐겨찾기")
    void favoriteBoard() throws Exception {
        //given
        Long boardId =1L;
        Member member = createMember();
        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getId(), "", Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(member));

        //when
        mockMvc.perform(
            post("/api/boards/{id}/favorites", boardId)
        ).andExpect(status().isOk());

        //then
        verify(boardService).updateFavoriteBoard(boardId, member);
    }

    @Test
    @DisplayName("게시판 즐겨찾기 목록 조회")
    void findFavoriteBoards() throws Exception {
        //given
        Integer page =0;
        Member member = createMember();
        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getId(), "", Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(member));

        //when
        mockMvc.perform(
            get("/api/boards/favorites")
        ).andExpect(status().isOk());

        //then
        verify(boardService).findFavoriteBoards(page,member);
    }

    @Test
    @DisplayName("게시판 검색")
    void searchBoard() throws Exception {
        //given
        String keyword = "title";
        Integer page = 0;

        //when
        mockMvc.perform(
            get("/api/boards/search/{keyword}", keyword)
        ).andExpect(status().isOk());

        //then
        verify(boardService).searchBoard(keyword, page);
    }
}
