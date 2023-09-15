package com.experianhealth.ciam.portal.service;

import com.experianhealth.ciam.portal.entity.AppResponse;

public interface ApplicationService {
    AppResponse getApplicationDetails(String token);
}

