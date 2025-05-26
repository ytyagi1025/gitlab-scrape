package com.mytholog.easyhireonboarding.service;

import com.mytholog.easyhireonboarding.model.Footprint;

public interface PlatformService {

    Footprint fetchAndSaveUserProfile(String profileUrl, String footprintType) throws Exception;

}
