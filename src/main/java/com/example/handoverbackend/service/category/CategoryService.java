package com.example.handoverbackend.service.category;

import com.example.handoverbackend.domain.category.Category;
import com.example.handoverbackend.dto.category.CategoryCreateRequestDto;
import com.example.handoverbackend.dto.category.CategoryResponseDto;
import com.example.handoverbackend.exception.CategoryAlreadyExistException;
import com.example.handoverbackend.exception.CategoryNotFoundException;
import com.example.handoverbackend.repository.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private static final String SUCCESS_CREATE_CATEGORY = "카테고리를 생성하였습니다.";
    private static final String SUCCESS_DELETE_CATEGORY = "카테고리를 제거하였습니다.";

    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<CategoryResponseDto> findAllCategory(){
        return categoryRepository.findAll().stream()
            .map(CategoryResponseDto::toDto)
            .toList();
    }

    @Transactional
    public String createCategory(CategoryCreateRequestDto requestDto){
        if (validationCategoryName(requestDto.getName())){
            throw new CategoryAlreadyExistException(requestDto.getName());
        }
        Category category = Category.createCategory(requestDto.getName());
        categoryRepository.save(category);
        return SUCCESS_CREATE_CATEGORY;
    }

    @Transactional
    public String deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(CategoryNotFoundException::new);
        categoryRepository.delete(category);
        return SUCCESS_DELETE_CATEGORY;
    }

    private boolean validationCategoryName(String name){
        return categoryRepository.findByName(name).isPresent();
    }
}
