package com.experianhealth.ciam.forgerock.service;
import org.springframework.stereotype.Service;

import com.experianhealth.ciam.forgerock.model.frApplicationDetails;



@Service
public class ManagedApplicationServiceImpl extends AbstractForgeRockIDMServiceImpl<frApplicationDetails> implements ManagedApplicationService {

    private static final String APPLICATION_PATH = "/openidm/managed/application";

    ManagedApplicationServiceImpl() {
        super(frApplicationDetails.class);
    }

    @Override
    String getBasePath() {
        return APPLICATION_PATH;
    }

}
