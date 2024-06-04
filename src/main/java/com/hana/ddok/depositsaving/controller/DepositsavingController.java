package com.hana.ddok.depositsaving.controller;

import com.hana.ddok.depositsaving.dto.DepositsavingFindbySaving100Res;
import com.hana.ddok.depositsaving.dto.DepositsavingFindbySavingRes;
import com.hana.ddok.depositsaving.service.DepositsavingService;
import com.hana.ddok.users.domain.UsersDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1")
@RestController
@RequiredArgsConstructor
public class DepositsavingController {
    private final DepositsavingService depositsavingService;

    @GetMapping("/depositsaving/saving100")
    public ResponseEntity<DepositsavingFindbySaving100Res> depositsavingFindBySaving100(@AuthenticationPrincipal UsersDetails usersDetails) {
        DepositsavingFindbySaving100Res depositsavingFindbySaving100Res = depositsavingService.depositsavingFindBySaving100(usersDetails.getUsername());
        return ResponseEntity.ok(depositsavingFindbySaving100Res);
    }

    @GetMapping("/depositsaving/saving")
    public ResponseEntity<DepositsavingFindbySavingRes> depositsavingFindBySaving(@AuthenticationPrincipal UsersDetails usersDetails) {
        DepositsavingFindbySavingRes depositsavingFindbySavingRes = depositsavingService.depositsavingFindBySaving(usersDetails.getUsername());
        return ResponseEntity.ok(depositsavingFindbySavingRes);
    }
}
