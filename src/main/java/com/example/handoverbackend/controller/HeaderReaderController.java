package com.example.handoverbackend.controller;

import com.example.handoverbackend.response.Response;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HeaderReaderController {

    @Operation(summary = "API Operation", security = {@SecurityRequirement(name = "Authorization")})
    @GetMapping("/api")
    public Response authHeaderChecker(HttpServletRequest request) {
        String authorizationHeaderValue = request.getHeader("Authorization");
        Map<String, String> result = new HashMap<>();
        result.put("Authorization", authorizationHeaderValue);
        return Response.success(result);
    }
}