package com.example.handoverbackend.controller.admin;

import com.example.handoverbackend.service.admin.AdminService;
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
public class AdminControllerTest {

    @InjectMocks
    AdminController adminController;
    @Mock
    AdminService adminService;

    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
    }

    @Test
    @DisplayName("유저 관리 테스트")
    void findAllReportedMember() throws Exception {
        //given, when
        mockMvc.perform(
            get("/api/admin/members")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        //then
        verify(adminService).findAllReportedMember();
    }

    @Test
    @DisplayName("산고된 유저 정지 해제")
    void unlockMember() throws Exception {
        //given
        Long id = 1L;

        //when
        mockMvc.perform(
            post("/api/admin/members/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        //then
        verify(adminService).unlockMember(id);
    }

    @Test
    @DisplayName("유저 정지")
    void lockMember() throws Exception {
        //given
        Long id = 1L;

        //when
        mockMvc.perform(
            post("/api/admin/members/report/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        //then
        verify(adminService).lockMember(id);
    }

    @Test
    @DisplayName("유저 삭제")
    void deleteReportedMember() throws Exception {
        //given
        Long id = 1L;

        //when
        mockMvc.perform(
            delete("/api/admin/members/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        //then
        verify(adminService).deleteReportedMember(id);
    }

    @Test
    @DisplayName("게시판 관리 테스트")
    void findAllReportedBoards() throws Exception {
        //given, when
        mockMvc.perform(
            get("/api/admin/boards")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        //then
        verify(adminService).findAllReportedBoards();
    }

    @Test
    @DisplayName("산고된 게시판 정지 해제")
    void unlockBoard() throws Exception {
        //given
        Long id = 1L;

        //when
        mockMvc.perform(
            post("/api/admin/boards/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        //then
        verify(adminService).unlockBoard(id);
    }

    @Test
    @DisplayName("게시판 정지")
    void lockBoard() throws Exception {
        //given
        Long id = 1L;

        //when
        mockMvc.perform(
            post("/api/admin/boards/report/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        //then
        verify(adminService).lockBoard(id);
    }

    @Test
    @DisplayName("유저 삭제")
    void deleteReportedBoard() throws Exception {
        //given
        Long id = 1L;

        //when
        mockMvc.perform(
            delete("/api/admin/boards/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        //then
        verify(adminService).deleteReportedBoard(id);
    }

    @Test
    @DisplayName("매칭 관리 테스트")
    void findAllReportedMatches() throws Exception {
        //given, when
        mockMvc.perform(
            get("/api/admin/matches")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        //then
        verify(adminService).findAllReportedMatches();
    }

    @Test
    @DisplayName("산고된 매칭 정지 해제")
    void unlockMatch() throws Exception {
        //given
        Long id = 1L;

        //when
        mockMvc.perform(
            post("/api/admin/matches/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        //then
        verify(adminService).unlockMatch(id);
    }

    @Test
    @DisplayName("매칭 정지")
    void lockMatch() throws Exception {
        //given
        Long id = 1L;

        //when
        mockMvc.perform(
            post("/api/admin/matches/report/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        //then
        verify(adminService).lockMatch(id);
    }

    @Test
    @DisplayName("유저 삭제")
    void deleteReportedMatch() throws Exception {
        //given
        Long id = 1L;

        //when
        mockMvc.perform(
            delete("/api/admin/matches/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        //then
        verify(adminService).deleteReportedMatch(id);
    }
}
