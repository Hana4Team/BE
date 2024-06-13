package com.hana.ddok.home.controller;

import com.hana.ddok.home.dto.HomeFindByUsersRes;
import com.hana.ddok.home.service.HomeService;
import com.hana.ddok.products.domain.ProductsType;
import com.hana.ddok.products.dto.ProductsFindAllRes;
import com.hana.ddok.products.dto.ProductsFindByIdRes;
import com.hana.ddok.users.domain.UsersDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1")
@RestController
@RequiredArgsConstructor
@Tag(name = "Home", description = "집 API")
public class HomeController {
    private final HomeService homeService;

    @GetMapping("/home")
    @Operation(summary = "우리집 조회")
    public ResponseEntity<HomeFindByUsersRes> homeFindByUsers(@AuthenticationPrincipal UsersDetails usersDetails) {
        HomeFindByUsersRes homeFindByUsersRes = homeService.homeFindByUsers(usersDetails.getUsername());
        return ResponseEntity.ok(homeFindByUsersRes);
    }
}
