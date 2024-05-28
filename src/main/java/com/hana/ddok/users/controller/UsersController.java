package com.hana.ddok.users.controller;

import com.hana.ddok.users.dto.UsersJoinReq;
import com.hana.ddok.users.dto.UsersJoinRes;
import com.hana.ddok.users.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/users")
public class UsersController {
    private final UsersService usersService;

    @PostMapping("/join")
    public ResponseEntity<UsersJoinRes> usersJoin(@RequestBody UsersJoinReq req) {
        UsersJoinRes res = usersService.usersJoin(req);
        return ResponseEntity.ok(res);
    }
}
