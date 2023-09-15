package com.experianhealth.ciam.scimapi.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.experianhealth.ciam.forgerock.model.Application;
import com.experianhealth.ciam.forgerock.model.FRQuery;
import com.experianhealth.ciam.forgerock.model.FRQueryFilter;
import com.experianhealth.ciam.forgerock.model.User;
import com.experianhealth.ciam.forgerock.service.ApplicationDetails;
import com.experianhealth.ciam.forgerock.service.ForgeRockAMService;
import com.experianhealth.ciam.forgerock.service.GeneralForgeRockIDMService;
import com.experianhealth.ciam.forgerock.service.ManagedApplicationService;
import com.experianhealth.ciam.forgerock.service.ManagedUserService;
import com.experianhealth.ciam.portal.entity.AppDetail;
import com.experianhealth.ciam.portal.entity.AppResponse;
@RestController
@RequestMapping("/myapplication")
public class ApplicationController {

    @Autowired
    private ForgeRockAMService forgeRockAMService;

    @Autowired
    private ManagedUserService userService;

    @Autowired
    private ManagedApplicationService managedApplicationService;

    @GetMapping
    public ResponseEntity<AppResponse> getApplicationDetails(
            @RequestHeader(value = "Authorization") String bearerToken) {
        
        // Fetch the user using the token
        User user = forgeRockAMService.getUserInfo(bearerToken);
        
        // Use ManagedUserService to fetch the user by ID
        User detailedUser = userService.getById(bearerToken, user.get_id()).orElse(null);

        // Extract the list of effective applications
        List<Application> effectiveApplications = detailedUser.getEffectiveApplications();
        
        // Construct the query for fetching application details
        FRQuery appQueryIds = FRQuery.Builder.create().withFilterExpression(
            FRQueryFilter.in("_id", getApplicationIds(effectiveApplications))
        ).build();
        
        // Fetch application details using the constructed query
        List<ApplicationDetails> applicationDetailsList = managedApplicationService.search(bearerToken, appQueryIds);
        
        if(applicationDetailsList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        List<AppDetail> transformedApps = applicationDetailsList.stream()
            .map(this::mapToAppDetail)
            .collect(Collectors.toList());

        AppResponse response = new AppResponse();
        response.setApps(transformedApps);

        return ResponseEntity.ok(response);
    }

    private AppDetail mapToAppDetail(ApplicationDetails detail) {
        AppDetail appDetail = new AppDetail();
        appDetail.setAppId(detail.get_id());
        appDetail.setAppName(detail.getName());
        appDetail.setAppDescription(detail.getDescription());
        appDetail.setAppIcon(detail.getIcon());
        appDetail.setAppUrl(detail.getUrl());
        return appDetail;
    }

    private List<String> getApplicationIds(List<Application> applications) {
        return applications.stream().map(Application::get_id).collect(Collectors.toList());
    }


}
