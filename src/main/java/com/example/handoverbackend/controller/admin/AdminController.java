package com.example.handoverbackend.controller.admin;

import com.example.handoverbackend.response.Response;
import com.example.handoverbackend.service.admin.AdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Api(value = "Admin Controller", tags = "Admin")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    @Operation(summary = "유저 관리", description = "유저를 관리합니다.")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/members")
    public Response findAllReportedMember(){
        return Response.success(adminService.findAllReportedMember());
    }

    @Operation(summary = "신고된 유저 정지 해제", description = "신고된 유저를 정지 해제시킵니다.")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/members/{id}")
    public Response unlockMember(@PathVariable Long id) {
        return Response.success(adminService.unlockMember(id));
    }

    @Operation(summary = "유저 정지", description = "유저를 정지시킵니다.")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/members/report/{id}")
    public Response lockMember(@PathVariable Long id) {
        return Response.success(adminService.lockMember(id));
    }

    @Operation(summary = "신고된 유저 삭제", description = "신고된 유저를 삭제 시킵니다.")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/members/{id}")
    public Response deleteReportedMember(@PathVariable Long id) {
        return Response.success(adminService.deleteReportedMember(id));
    }

    @Operation(summary = "게시판 관리", description = "게시판을 관리합니다.")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/boards")
    public Response findAllReportedBoards() {
        return Response.success(adminService.findAllReportedBoards());
    }

    @Operation(summary = "신고된 게시판 정지 해제", description = "신고된 게시판을 정지 해제시킵니다.")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/boards/{id}")
    public Response unlockBoard(@PathVariable Long id) {
        return Response.success(adminService.unlockBoard(id));
    }

    @Operation(summary = "게시판 정지", description = "게시판을 정지시킵니다.")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/boards/report/{id}")
    public Response lockBoard(@PathVariable Long id) {
        return Response.success(adminService.lockBoard(id));
    }

    @Operation(summary = "신고된 게시글 삭제", description = "신고된 게시글을 삭제 시킵니다.")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/boards/{id}")
    public Response deleteReportedBoard(@PathVariable Long id) {
        return Response.success(adminService.deleteReportedBoard(id));
    }

    @Operation(summary = "매칭 관리", description = "매칭을 관리합니다.")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/matches")
    public Response findAllReportedMatches() {
        return Response.success(adminService.findAllReportedMatches());
    }

    @Operation(summary = "매칭 정지 해제", description = "매칭 정지를 해제시킵니다.")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/matches/{id}")
    public Response unlockMatch(@PathVariable Long id) {
        return Response.success(adminService.unlockMatch(id));
    }

    @Operation(summary = "매칭 정지", description = "매칭을 정지시킵니다.")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/matches/report/{id}")
    public Response lockMatch(@PathVariable Long id) {
        return Response.success(adminService.lockMatch(id));
    }

    @Operation(summary = "매칭 삭제", description = "매칭을 삭제 시킵니다.")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/matches/{id}")
    public Response deleteReportedMatch(@PathVariable Long id) {
        return Response.success(adminService.deleteReportedMatch(id));
    }
}
