package com.mytholog.easyhireonboarding.repository;


import com.mytholog.easyhireonboarding.model.GitLabUser;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "gitlabClient", url = "https://gitlab.com/api/v4")
public interface UserClient {
    @GetMapping("/users")
    List<GitLabUser> getUserByUsername(@RequestParam("username") String username);
}
