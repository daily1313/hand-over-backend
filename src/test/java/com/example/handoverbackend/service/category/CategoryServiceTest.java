package com.example.handoverbackend.service.category;

import com.example.handoverbackend.domain.category.Category;
import com.example.handoverbackend.dto.category.CategoryCreateRequestDto;
import com.example.handoverbackend.dto.category.CategoryResponseDto;
import com.example.handoverbackend.repository.MemberRepository;
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
        categories.add(Category.createCategory("category"));
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
        Category category = Category.createCategory("name");
        given(categoryRepository.findById(anyLong())).willReturn(Optional.of(category));

        // when
        categoryService.deleteCategory(anyLong());

        // then
        verify(categoryRepository).delete(any());
    }
}
