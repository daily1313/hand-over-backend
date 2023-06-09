package com.example.handoverbackend.service.category;

import com.example.handoverbackend.domain.category.Category;
import com.example.handoverbackend.dto.category.CategoryCreateRequestDto;
import com.example.handoverbackend.dto.category.CategoryResponseDto;
import com.example.handoverbackend.exception.CategoryAlreadyExistException;
import com.example.handoverbackend.exception.CategoryNotFoundException;
import com.example.handoverbackend.repository.category.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @InjectMocks
    CategoryService categoryService;
    @Mock
    CategoryRepository categoryRepository;

    @Test
    @DisplayName("카테고리 전체 조회")
    public void findAllCategory() {
        // given
        List<Category> categories = new ArrayList<>();
        categories.add(new Category("category"));
        given(categoryRepository.findAll()).willReturn(categories);

        // when
        List<CategoryResponseDto> result = categoryService.findAllCategory();

        // then
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("카테고리 생성")
    public void createCategory() {
        // given
        CategoryCreateRequestDto req = new CategoryCreateRequestDto("name");

        // when
        categoryService.createCategory(req);

        // then
        verify(categoryRepository).save(any());
    }

    @Test
    @DisplayName("카테고리 삭제")
    public void deleteCategory() {
        // given
        Category category = new Category("name");
        given(categoryRepository.findById(anyLong())).willReturn(Optional.of(category));

        // when
        categoryService.deleteCategory(anyLong());

        // then
        verify(categoryRepository).delete(any());
    }

    @Test
    @DisplayName("이미 존재하는 카테고리 이름을 생성하려하면 예외 발생")
    public void createCategoryException() {
        // given
        String categoryName = "name";
        CategoryCreateRequestDto req = new CategoryCreateRequestDto(categoryName);
        given(categoryRepository.existsByName(categoryName)).willReturn(true);

        // when, then
        assertThatThrownBy(() -> categoryService.createCategory(req))
            .isInstanceOf(CategoryAlreadyExistException.class);
    }

    @Test
    @DisplayName("존재하지 않는 카테고리를 삭제하려하면 예외 발생")
    public void deleteCategoryException() {
        // given
        Long id = 1L;
        given(categoryRepository.findById(anyLong())).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> categoryService.deleteCategory(id))
            .isInstanceOf(CategoryNotFoundException.class);
    }
}
