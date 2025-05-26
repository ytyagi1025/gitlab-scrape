package com.mytholog.easyhireonboarding.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mytholog.easyhireonboarding.exceptions.BadRequestException;
import com.mytholog.easyhireonboarding.model.Footprint;
import com.mytholog.easyhireonboarding.model.enumeration.FootprintType;
import com.mytholog.easyhireonboarding.repository.FootprintRepository;
import com.mytholog.easyhireonboarding.service.PlatformService;
import com.mytholog.easyhireonboarding.util.ApiClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;

@Service
@Slf4j
public class StackOverflowServiceImpl implements PlatformService {

    private static final String SITE = "stackoverflow";
    private static final String FILTER = "&site=" + SITE;
    private final ApiClient apiClient;
    private final ObjectMapper objectMapper;
    private final FootprintRepository footprintRepository;

    @Value("${easyhire.platforms.stackoverflow.base-url}")
    private String baseUrl;

    public StackOverflowServiceImpl(ApiClient apiClient, ObjectMapper objectMapper, FootprintRepository footprintRepository) {
        this.apiClient = apiClient;
        this.objectMapper = objectMapper;
        this.footprintRepository = footprintRepository;
    }

    @Override
    public Footprint fetchAndSaveUserProfile(String profileUrl, String footprintType) {
        String userId = extractUserIdFromUrl(profileUrl);
        if (userId == null) {
            log.error("Invalid StackOverflow profile URL");
            throw new BadRequestException("Invalid StackOverflow profile URL");
        }
        JsonNode userNode = apiClient.getJson(baseUrl + userId + "?order=desc&sort=reputation" + FILTER)
                .get("items").get(0);
        Map<String, Object> rawData = objectMapper.convertValue(userNode, new TypeReference<>() {});
        Footprint footprint = new Footprint();
        footprint.setFootprintType(FootprintType.valueOf(footprintType));
        footprint.setData(rawData);
        footprint.setVersion(1);
        return footprintRepository.save(footprint);
    }

    private String extractUserIdFromUrl(String url) {
        String[] parts = url.split("/");
        return Arrays.stream(parts)
                .filter(p -> p.matches("\\d+"))
                .findFirst()
                .orElseThrow(() -> new BadRequestException("Invalid profile URL"));
    }
}
