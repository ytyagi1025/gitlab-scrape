package com.mytholog.easyhireonboarding.controller;


import com.mytholog.easyhireonboarding.model.GitLabUser;
import com.mytholog.easyhireonboarding.repository.UserClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/gitlab")
public class GitLabController {

    private final UserClient gitLabClient;

    public GitLabController(UserClient gitLabClient) {
        this.gitLabClient = gitLabClient;
    }

    @GetMapping("/user")
    public ResponseEntity<GitLabUser> getUser(@RequestParam String username) {
        List<GitLabUser> users = gitLabClient.getUserByUsername(username);
        if (!users.isEmpty()) {
            return new ResponseEntity<>(users.getFirst(), HttpStatus.OK); // Return the first match
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
