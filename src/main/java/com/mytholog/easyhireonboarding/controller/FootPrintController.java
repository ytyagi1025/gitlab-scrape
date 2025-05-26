package com.mytholog.easyhireonboarding.controller;

import com.mytholog.easyhireonboarding.factory.PlatformServiceFactory;
import com.mytholog.easyhireonboarding.model.Footprint;
import com.mytholog.easyhireonboarding.model.enumeration.FootprintType;
import com.mytholog.easyhireonboarding.service.PlatformService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class FootPrintController {

    private final PlatformServiceFactory platformServiceFactory;

    public FootPrintController(PlatformServiceFactory platformServiceFactory) {
        this.platformServiceFactory = platformServiceFactory;
    }

    @GetMapping("/profile")
    public ResponseEntity<Footprint> saveProfile(
            @RequestParam String profileUrl,
            @RequestParam String footprintType) throws Exception {

        PlatformService platformService = platformServiceFactory.getService(FootprintType.valueOf(footprintType));
        return ResponseEntity.status(HttpStatus.OK).body(platformService.fetchAndSaveUserProfile(profileUrl, footprintType));
    }
}
