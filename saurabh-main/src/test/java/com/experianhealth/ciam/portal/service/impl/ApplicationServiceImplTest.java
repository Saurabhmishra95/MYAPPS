package com.experianhealth.ciam.portal.service.impl;

import com.experianhealth.ciam.CIAMTestBase;
import com.experianhealth.ciam.exception.ApplicationNotFoundException;
import com.experianhealth.ciam.forgerock.model.User;
import com.experianhealth.ciam.forgerock.model.frApplication;
import com.experianhealth.ciam.forgerock.model.frApplicationDetails;
import com.experianhealth.ciam.forgerock.service.ForgeRockAMService;
import com.experianhealth.ciam.forgerock.service.ManagedApplicationService;
import com.experianhealth.ciam.forgerock.service.ManagedUserService;
import com.experianhealth.ciam.portal.entity.AppResponse;
import com.experianhealth.ciam.portal.service.ApplicationServiceImpl;
import com.experianhealth.ciam.portal.utility.ApplicationDetailsMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ApplicationServiceImplTest extends CIAMTestBase {

    @Mock
    private ForgeRockAMService forgeRockAMService;

    @Mock
    private ManagedUserService managedUserService;

    @Mock
    private ManagedApplicationService managedApplicationService;

    @Mock
    private ApplicationDetailsMapper applicationDetailsMapper;

    @InjectMocks
    private ApplicationServiceImpl applicationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        applicationService = new ApplicationServiceImpl(forgeRockAMService, managedUserService, managedApplicationService, applicationDetailsMapper);
    }

    @Test
    void testGetApplicationDetailsSuccess() {
        String token = "sampleToken";

        User user = new User();
        user.set_id("sampleUserId");

        User detailedUser = new User();
        List<frApplication> effectiveApplications = Arrays.asList(new frApplication());
        detailedUser.setEffectiveApplications(effectiveApplications);

        frApplication mockApplication = new frApplication();
        mockApplication.set_id("mockAppId");
        mockApplication.setName("mockAppName");

        frApplicationDetails mockAppDetails = new frApplicationDetails();
        mockAppDetails.set_id("mockAppDetailsId");
        mockAppDetails.setName("mockAppName");

        List<frApplicationDetails> mockAppDetailsList = Arrays.asList(mockAppDetails);

        when(forgeRockAMService.getUserInfo(token)).thenReturn(user);
        when(managedUserService.getById(token, user.get_id())).thenReturn(java.util.Optional.of(detailedUser));
        when(managedApplicationService.search(anyString(), any())).thenReturn(mockAppDetailsList);

        AppResponse result = applicationService.getApplicationDetails(token);

        assertNotNull(result);
        verify(forgeRockAMService, times(1)).getUserInfo(token);
        verify(managedUserService, times(1)).getById(token, user.get_id());
    }

    @Test
    void testGetApplicationDetailsNoAppsFound() {
        String token = "sampleToken";
        User user = new User();
        user.set_id("sampleUserId");
        User detailedUser = new User();
        detailedUser.setEffectiveApplications(Collections.emptyList());

        when(forgeRockAMService.getUserInfo(token)).thenReturn(user);
        when(managedUserService.getById(token, user.get_id())).thenReturn(java.util.Optional.of(detailedUser));
        when(managedApplicationService.search(anyString(), any())).thenReturn(Collections.emptyList());

        assertThrows(ApplicationNotFoundException.class, () -> applicationService.getApplicationDetails(token));
    }
}
