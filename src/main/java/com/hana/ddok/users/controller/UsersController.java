package com.hana.ddok.users.controller;

import com.hana.ddok.users.dto.*;
import com.hana.ddok.users.service.UsersService;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/users")
public class UsersController {
    private final UsersService usersService;

    @PostMapping("/login")
    public ResponseEntity<UsersLoginRes> usersLogin(@RequestBody UsersLoginReq req) {
        UsersLoginRes res = usersService.usersLogin(req);
        return ResponseEntity.ok(res);
    }


    @PostMapping("/join")
    public ResponseEntity<UsersJoinRes> usersJoin(@RequestBody UsersJoinReq req) {

        UsersJoinRes res = usersService.usersJoin(req);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/message")
    public ResponseEntity<SingleMessageSentResponse> usersMessage(@RequestBody UsersMessageReq req) {
        return ResponseEntity.ok(usersService.usersMessage(req));
    }
}
