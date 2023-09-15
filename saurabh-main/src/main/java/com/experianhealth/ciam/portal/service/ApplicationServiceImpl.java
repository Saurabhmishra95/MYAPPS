package com.experianhealth.ciam.portal.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.experianhealth.ciam.exception.ApplicationNotFoundException;
import com.experianhealth.ciam.forgerock.model.FRQuery;
import com.experianhealth.ciam.forgerock.model.FRQueryFilter;
import com.experianhealth.ciam.forgerock.model.User;
import com.experianhealth.ciam.forgerock.model.frApplication;
import com.experianhealth.ciam.forgerock.model.frApplicationDetails;
import com.experianhealth.ciam.forgerock.service.ForgeRockAMService;
import com.experianhealth.ciam.forgerock.service.ManagedApplicationService;
import com.experianhealth.ciam.forgerock.service.ManagedUserService;
import com.experianhealth.ciam.portal.entity.AppDetail;
import com.experianhealth.ciam.portal.entity.AppResponse;
import com.experianhealth.ciam.portal.utility.ApplicationDetailsMapper;

@Service
	public class ApplicationServiceImpl implements ApplicationService {

	    @Autowired
	    private ForgeRockAMService forgeRockAMService;

	    @Autowired
	    private ManagedUserService userService;

	    @Autowired
	    private ManagedApplicationService managedApplicationService;
	    
	    @Autowired
	    private ApplicationDetailsMapper applicationDetailsMapper;

	    @Override
	    public AppResponse getApplicationDetails(String token) {
	        User user = forgeRockAMService.getUserInfo(token);
	        User detailedUser = userService.getById(token, user.get_id()).orElse(null);
	        List<frApplication> effectiveApplications = detailedUser.getEffectiveApplications();
	        FRQuery appQueryIds = FRQuery.Builder.create().withFilterExpression(
	            FRQueryFilter.in("_id", getApplicationIds(effectiveApplications))
	        ).build();
	        List<frApplicationDetails> applicationDetailsList = managedApplicationService.search(token, appQueryIds);
	        if(applicationDetailsList.isEmpty()) {
	            throw new ApplicationNotFoundException("N/A", "No application details found for the user.");
	        }
	        List<AppDetail> transformedApps = applicationDetailsList.stream()
	                .map(applicationDetailsMapper::mapToAppDetail)
	                .collect(Collectors.toList());
	        AppResponse response = new AppResponse();
	        response.setApps(transformedApps);
	        return response;
	    }

	  
	    private List<String> getApplicationIds(List<frApplication> applications) {
	        return applications.stream().map(frApplication::get_id).collect(Collectors.toList());
	    }
}
