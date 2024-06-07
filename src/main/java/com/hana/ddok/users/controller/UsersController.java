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
@RequestMapping("/api/v1/users")
public class UsersController {
    private final UsersService usersService;

    @PostMapping("/login")
    public ResponseEntity<UsersLoginRes> usersLogin(@RequestBody UsersLoginReq req) {
        return ResponseEntity.status(200).body(usersService.usersLogin(req));
    }

    @PostMapping("/join")
    public ResponseEntity<UsersJoinRes> usersJoin(@RequestBody UsersJoinReq req) {
        return ResponseEntity.ok(usersService.usersJoin(req));
    }

    @PostMapping("/message")
    public ResponseEntity<UsersMessageRes> usersMessage(@RequestBody UsersMessageReq req) {
        return ResponseEntity.ok(usersService.usersMessage(req));
    }

    @PostMapping("/msgCheck")
    public ResponseEntity<UsersMsgCheckRes> usersMsgCheck(@RequestBody UsersMsgCheckReq req) {
        return ResponseEntity.ok(usersService.usersMsgCheck(req));
    }

    @GetMapping()
    public ResponseEntity<UsersGetRes> usersGet(@AuthenticationPrincipal UsersDetails usersDetails) {
        return ResponseEntity.ok(usersService.usersGet(usersDetails.getUsername()));
    }

    @GetMapping("/point")
    public ResponseEntity<UsersGetPointRes> usersGetPoint(@AuthenticationPrincipal UsersDetails usersDetails) {
        return ResponseEntity.ok(usersService.usersGetPoint(usersDetails.getUsername()));
    }

    @PutMapping("/point")
    public ResponseEntity<UsersSavePointRes> usersSavePoint(@AuthenticationPrincipal UsersDetails usersDetails,
                                                           @RequestBody UsersSavePointReq req) {
        return ResponseEntity.ok(usersService.usersSavePoint(usersDetails.getUsername(), req));
    }

    @PutMapping("/start")
    public ResponseEntity<UsersMissionRes> usersMissionStart(@AuthenticationPrincipal UsersDetails usersDetails) {
        return ResponseEntity.ok(usersService.usersMissionStart(usersDetails.getUsername()));
    }

    @PutMapping("/move")
    public ResponseEntity<UsersMissionRes> usersMove(@AuthenticationPrincipal UsersDetails usersDetails) {
        return ResponseEntity.ok(usersService.usersMove(usersDetails.getUsername()));
    }

    @PutMapping("/check")
    public ResponseEntity<UsersMissionRes> usersMissionCheck(@AuthenticationPrincipal UsersDetails usersDetails) {
        return ResponseEntity.ok(usersService.usersMissionCheck(usersDetails.getUsername()));
    }

    @PutMapping("/news")
    public ResponseEntity<UsersReadNewsRes> usersReadNews(@AuthenticationPrincipal UsersDetails usersDetails) {
        return ResponseEntity.ok(usersService.usersReadNews(usersDetails.getUsername()));
    }


}
