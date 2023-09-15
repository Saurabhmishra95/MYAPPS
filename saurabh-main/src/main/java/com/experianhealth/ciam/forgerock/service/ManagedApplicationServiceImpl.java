package com.experianhealth.ciam.forgerock.service;
import org.springframework.stereotype.Service;

@Service
public class ManagedApplicationServiceImpl extends AbstractForgeRockIDMServiceImpl<ApplicationDetails> implements ManagedApplicationService {

    private static final String APPLICATION_PATH = "/openidm/managed/application";

    ManagedApplicationServiceImpl() {
        super(ApplicationDetails.class);
    }

    @Override
    String getBasePath() {
        return APPLICATION_PATH;
    }

}
