package com.experianhealth.ciam.forgerock.service;

import java.util.List;

import com.experianhealth.ciam.forgerock.model.FRQuery;
import com.experianhealth.ciam.forgerock.model.frApplicationDetails;

public interface ManagedApplicationService {
    List<frApplicationDetails> search(String token, FRQuery query);
}
