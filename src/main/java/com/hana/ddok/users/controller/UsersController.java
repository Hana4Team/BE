package com.hana.ddok.users.controller;

import com.hana.ddok.users.domain.UsersDetails;
import com.hana.ddok.users.dto.*;
import com.hana.ddok.users.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UsersController {
    private final UsersService usersService;

    @PostMapping("/users/login")
    public ResponseEntity<UsersLoginRes> usersLogin(@RequestBody UsersLoginReq req) {
        return ResponseEntity.ok(usersService.usersLogin(req));
    }

    @PostMapping("/users/join")
    public ResponseEntity<UsersJoinRes> usersJoin(@RequestBody UsersJoinReq req) {
        return ResponseEntity.ok(usersService.usersJoin(req));
    }

    @PostMapping("/users/message")
    public ResponseEntity<UsersMessageRes> usersMessage(@RequestBody UsersMessageReq req) {
        return ResponseEntity.ok(usersService.usersMessage(req));
    }

    @PostMapping("/users/msgCheck")
    public ResponseEntity<UsersMsgCheckRes> usersMsgCheck(@RequestBody UsersMsgCheckReq req) {
        return ResponseEntity.ok(usersService.usersMsgCheck(req));
    }

    @GetMapping("/users")
    public ResponseEntity<UsersGetRes> usersGet(@AuthenticationPrincipal UsersDetails usersDetails) {
        return ResponseEntity.ok(usersService.usersGet(usersDetails.getUsername()));
    }

    @GetMapping("/users/point")
    public ResponseEntity<UsersGetPointRes> usersGetPoint(@AuthenticationPrincipal UsersDetails usersDetails) {
        return ResponseEntity.ok(usersService.usersGetPoint(usersDetails.getUsername()));
    }

    @PutMapping("/users/point")
    public ResponseEntity<UsersSavePointRes> usersSavePoint(@AuthenticationPrincipal UsersDetails usersDetails,
                                                           @RequestBody UsersSavePointReq req) {
        return ResponseEntity.ok(usersService.usersSavePoint(usersDetails.getUsername(), req));
    }

    @PutMapping("/users/start")
    public ResponseEntity<UsersMissionRes> usersMissionStart(@AuthenticationPrincipal UsersDetails usersDetails) {
        return ResponseEntity.ok(usersService.usersMissionStart(usersDetails.getUsername()));
    }

    @PutMapping("/users/move")
    public ResponseEntity<UsersMissionRes> usersMove(@AuthenticationPrincipal UsersDetails usersDetails) {
        return ResponseEntity.ok(usersService.usersMove(usersDetails.getUsername()));
    }

    @PutMapping("/users/check")
    public ResponseEntity<UsersMissionRes> usersMissionCheck(@AuthenticationPrincipal UsersDetails usersDetails) {
        return ResponseEntity.ok(usersService.usersMissionCheck(usersDetails.getUsername()));
    }

    @PutMapping("/users/news")
    public ResponseEntity<UsersReadNewsRes> usersReadNews(@AuthenticationPrincipal UsersDetails usersDetails) {
        return ResponseEntity.ok(usersService.usersReadNews(usersDetails.getUsername()));
    }
}
