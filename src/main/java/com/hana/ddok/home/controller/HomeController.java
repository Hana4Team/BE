package com.hana.ddok.home.controller;

import com.hana.ddok.home.dto.HomeFindByUsersRes;
import com.hana.ddok.home.service.HomeService;
import com.hana.ddok.products.domain.ProductsType;
import com.hana.ddok.products.dto.ProductsFindAllRes;
import com.hana.ddok.products.dto.ProductsFindByIdRes;
import com.hana.ddok.users.domain.UsersDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1")
@RestController
@RequiredArgsConstructor
public class HomeController {
    private final HomeService homeService;

    @GetMapping("/home")
    public ResponseEntity<HomeFindByUsersRes> homeFindByUsers(@AuthenticationPrincipal UsersDetails usersDetails) {
        HomeFindByUsersRes homeFindByUsersRes = homeService.homeFindByUsers(usersDetails.getUsername());
        return ResponseEntity.ok(homeFindByUsersRes);
    }
}
