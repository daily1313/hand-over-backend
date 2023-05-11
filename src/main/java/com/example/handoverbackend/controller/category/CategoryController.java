package com.example.handoverbackend.controller.category;

import com.example.handoverbackend.dto.category.CategoryCreateRequestDto;
import com.example.handoverbackend.response.Response;
import com.example.handoverbackend.service.category.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Api(value = "Category Controller", tags = "Category")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "모든 카테고리 조회", description = "모든 카테고리를 조회합니다.")
    @GetMapping("/categories")
    @ResponseStatus(HttpStatus.OK)
    public Response findAllCategory() {
        return Response.success(categoryService.findAllCategory());
    }

    @Operation(summary = "카테고리 생성", description = "카테고리를 생성합니다.")
    @PostMapping("/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public Response createCategory(@Valid @RequestBody CategoryCreateRequestDto categoryCreateRequestDto) {
        return Response.success(categoryService.createCategory(categoryCreateRequestDto));
    }

    @Operation(summary = "카테고리 삭제", description = "카테고리를 삭제합니다.")
    @DeleteMapping("/categories/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response deleteCategory(@ApiParam(value = "카테고리 id", required = true) @PathVariable Long id) {
        return Response.success(categoryService.deleteCategory(id));
    }
}
