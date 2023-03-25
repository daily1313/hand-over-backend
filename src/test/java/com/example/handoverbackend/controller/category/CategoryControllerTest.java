package com.example.handoverbackend.controller.category;

import com.example.handoverbackend.dto.category.CategoryCreateRequestDto;
import com.example.handoverbackend.service.category.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
public class CategoryControllerTest {
    @InjectMocks
    CategoryController categoryController;
    @Mock
    CategoryService categoryService;
    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();
    }


    @Test
    @DisplayName("카테고리 전체 조회")
    public void findAllCategory() throws Exception {
        // given

        // when
        mockMvc.perform(get("/api/categories"))
            .andExpect(status().isOk());

        //then
        verify(categoryService).findAllCategory();
    }

    @Test
    @DisplayName("카테고리 생성")
    public void createCategory() throws Exception {
        // given
        CategoryCreateRequestDto req = new CategoryCreateRequestDto("category");

        // when
        mockMvc.perform(
                post("/api/categories")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(req)))
            .andExpect(status().isCreated());

        //then
        verify(categoryService).createCategory(req);
    }

    @Test
    @DisplayName("카테고리 삭제")
    public void deleteCategory() throws Exception {
        // given
        Long id = 1L;

        // when
        mockMvc.perform(
                delete("/api/categories/{id}", id))
            .andExpect(status().isOk());

        //then
        verify(categoryService).deleteCategory(id);
    }
}
