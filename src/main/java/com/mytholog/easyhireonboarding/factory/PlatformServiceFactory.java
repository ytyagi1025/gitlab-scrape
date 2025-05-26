package com.mytholog.easyhireonboarding.factory;

import com.mytholog.easyhireonboarding.model.enumeration.FootprintType;
import com.mytholog.easyhireonboarding.service.PlatformService;
import com.mytholog.easyhireonboarding.service.impl.StackOverflowServiceImpl;
import org.springframework.stereotype.Component;

@Component
public class PlatformServiceFactory {

    private final StackOverflowServiceImpl stackOverflowServiceImpl;

    public PlatformServiceFactory(StackOverflowServiceImpl stackOverflowServiceImpl) {
        this.stackOverflowServiceImpl = stackOverflowServiceImpl;
    }

    public PlatformService getService(FootprintType type) {
        return switch (type) {
            case STACKOVERFLOW -> stackOverflowServiceImpl;
        };
    }
}
