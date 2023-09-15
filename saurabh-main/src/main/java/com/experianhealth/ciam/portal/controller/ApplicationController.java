package com.experianhealth.ciam.portal.controller;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.experianhealth.ciam.portal.entity.AppResponse;
import com.experianhealth.ciam.portal.service.ApplicationService;
import com.experianhealth.ciam.scimapi.utils.AuthorizationUtils;
@RestController
@RequestMapping("/myapplication")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @GetMapping
    public ResponseEntity<AppResponse> getApplicationDetails(
            @RequestHeader(value = "Authorization", required = false) Optional<String> bearerToken) {
        String token = AuthorizationUtils.validateBearerToken(bearerToken);
        AppResponse response = applicationService.getApplicationDetails(token);
        if(response.getApps().isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(response);
    }
}

