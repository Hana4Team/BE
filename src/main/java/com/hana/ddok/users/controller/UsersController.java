package com.hana.ddok.users.controller;

import com.hana.ddok.users.domain.UsersDetails;
import com.hana.ddok.users.dto.*;
import com.hana.ddok.users.service.UsersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "Users", description = "사용자 API")
public class UsersController {
    private final UsersService usersService;

    @PostMapping("/users/login")
    @Operation(summary = "로그인")
    public ResponseEntity<UsersLoginRes> usersLogin(@RequestBody UsersLoginReq usersLoginReq) {
        UsersLoginRes usersLoginRes = usersService.usersLogin(usersLoginReq);
        return ResponseEntity.ok(usersLoginRes);
    }

    @PostMapping("/users/join")
    @Operation(summary = "회원가입")
    public ResponseEntity<UsersJoinRes> usersJoin(@RequestBody UsersJoinReq usersJoinReq) {
        UsersJoinRes usersJoinRes = usersService.usersJoin(usersJoinReq);
        return ResponseEntity.ok(usersJoinRes);
    }

    @PostMapping("/users/message")
    @Operation(summary = "문자인증 전송")
    public ResponseEntity<UsersMessageRes> usersMessage(@RequestBody UsersMessageReq usersMessageReq) {
        UsersMessageRes usersMessageRes = usersService.usersMessage(usersMessageReq);
        return ResponseEntity.ok(usersMessageRes);
    }

    @PostMapping("/users/msgCheck")
    @Operation(summary = "문자인증 확인")
    public ResponseEntity<UsersMsgCheckRes> usersMsgCheck(@RequestBody UsersMsgCheckReq usersMsgCheckReq) {
        UsersMsgCheckRes usersMsgCheckRes = usersService.usersMsgCheck(usersMsgCheckReq);
        return ResponseEntity.ok(usersMsgCheckRes);
    }

    @GetMapping("/users")
    @Operation(summary = "사용자정보 조회")
    public ResponseEntity<UsersGetRes> usersGet(@AuthenticationPrincipal UsersDetails usersDetails) {
        UsersGetRes usersGetRes = usersService.usersGet(usersDetails.getUsername());
        return ResponseEntity.ok(usersGetRes);
    }

    @GetMapping("/users/point")
    @Operation(summary = "하나머니 조회")
    public ResponseEntity<UsersGetPointRes> usersGetPoint(@AuthenticationPrincipal UsersDetails usersDetails) {
        UsersGetPointRes usersGetPointRes = usersService.usersGetPoint(usersDetails.getUsername());
        return ResponseEntity.ok(usersGetPointRes);
    }

    @PutMapping("/users/point")
    @Operation(summary = "하나머니 적립")
    public ResponseEntity<UsersSavePointRes> usersSavePoint(@AuthenticationPrincipal UsersDetails usersDetails,
                                                           @RequestBody UsersSavePointReq usersSavePointReq) {
        UsersSavePointRes usersSavePointRes = usersService.usersSavePoint(usersDetails.getUsername(), usersSavePointReq);
        return ResponseEntity.ok(usersSavePointRes);
    }

    @PutMapping("/users/start")
    @Operation(summary = "미션 시작")
    public ResponseEntity<UsersMissionRes> usersMissionStart(@AuthenticationPrincipal UsersDetails usersDetails) {
        UsersMissionRes usersMissionRes = usersService.usersMissionStart(usersDetails.getUsername());
        return ResponseEntity.ok(usersMissionRes);
    }

    @PutMapping("/users/check")
    @Operation(summary = "미션 종료확인")
    public ResponseEntity<UsersMissionRes> usersMissionCheck(@AuthenticationPrincipal UsersDetails usersDetails) {
        UsersMissionRes usersMissionRes = usersService.usersMissionCheck(usersDetails.getUsername());
        return ResponseEntity.ok(usersMissionRes);
    }

    @PutMapping("/users/news")
    @Operation(summary = "뉴스 열람")
    public ResponseEntity<UsersReadNewsRes> usersReadNews(@AuthenticationPrincipal UsersDetails usersDetails) {
        UsersReadNewsRes usersReadNewsRes = usersService.usersReadNews(usersDetails.getUsername());
        return ResponseEntity.ok(usersReadNewsRes);
    }
}
