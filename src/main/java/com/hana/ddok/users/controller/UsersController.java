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
    public ResponseEntity<UsersLoginRes> usersLogin(@RequestBody UsersLoginReq usersLoginReq) {
        UsersLoginRes usersLoginRes = usersService.usersLogin(usersLoginReq);
        return ResponseEntity.ok(usersLoginRes);
    }

    @PostMapping("/users/join")
    public ResponseEntity<UsersJoinRes> usersJoin(@RequestBody UsersJoinReq usersJoinReq) {
        UsersJoinRes usersJoinRes = usersService.usersJoin(usersJoinReq);
        return ResponseEntity.ok(usersJoinRes);
    }

    @PostMapping("/users/message")
    public ResponseEntity<UsersMessageRes> usersMessage(@RequestBody UsersMessageReq usersMessageReq) {
        UsersMessageRes usersMessageRes = usersService.usersMessage(usersMessageReq);
        return ResponseEntity.ok(usersMessageRes);
    }

    @PostMapping("/users/msgCheck")
    public ResponseEntity<UsersMsgCheckRes> usersMsgCheck(@RequestBody UsersMsgCheckReq usersMsgCheckReq) {
        UsersMsgCheckRes usersMsgCheckRes = usersService.usersMsgCheck(usersMsgCheckReq);
        return ResponseEntity.ok(usersMsgCheckRes);
    }

    @GetMapping("/users")
    public ResponseEntity<UsersGetRes> usersGet(@AuthenticationPrincipal UsersDetails usersDetails) {
        UsersGetRes usersGetRes = usersService.usersGet(usersDetails.getUsername());
        return ResponseEntity.ok(usersGetRes);
    }

    @GetMapping("/users/point")
    public ResponseEntity<UsersGetPointRes> usersGetPoint(@AuthenticationPrincipal UsersDetails usersDetails) {
        UsersGetPointRes usersGetPointRes = usersService.usersGetPoint(usersDetails.getUsername());
        return ResponseEntity.ok(usersGetPointRes);
    }

    @PutMapping("/users/point")
    public ResponseEntity<UsersSavePointRes> usersSavePoint(@AuthenticationPrincipal UsersDetails usersDetails,
                                                           @RequestBody UsersSavePointReq usersSavePointReq) {
        UsersSavePointRes usersSavePointRes = usersService.usersSavePoint(usersDetails.getUsername(), usersSavePointReq);
        return ResponseEntity.ok(usersSavePointRes);
    }

    @PutMapping("/users/start")
    public ResponseEntity<UsersMissionRes> usersMissionStart(@AuthenticationPrincipal UsersDetails usersDetails) {
        UsersMissionRes usersMissionRes = usersService.usersMissionStart(usersDetails.getUsername());
        return ResponseEntity.ok(usersMissionRes);
    }

    @PutMapping("/users/check")
    public ResponseEntity<UsersMissionRes> usersMissionCheck(@AuthenticationPrincipal UsersDetails usersDetails) {
        UsersMissionRes usersMissionRes = usersService.usersMissionCheck(usersDetails.getUsername());
        return ResponseEntity.ok(usersMissionRes);
    }

    @PutMapping("/users/news")
    public ResponseEntity<UsersReadNewsRes> usersReadNews(@AuthenticationPrincipal UsersDetails usersDetails) {
        UsersReadNewsRes usersReadNewsRes = usersService.usersReadNews(usersDetails.getUsername());
        return ResponseEntity.ok(usersReadNewsRes);
    }

    // TODO : 컨트롤러 제외
    @PutMapping("/users/move")
    public ResponseEntity<UsersMissionRes> usersMove(@AuthenticationPrincipal UsersDetails usersDetails) {
        return ResponseEntity.ok(usersService.usersMove(usersDetails.getUsername()));
    }
}
