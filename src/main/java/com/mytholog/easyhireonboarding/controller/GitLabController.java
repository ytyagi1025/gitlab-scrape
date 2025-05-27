package com.mytholog.easyhireonboarding.controller;


import com.mytholog.easyhireonboarding.model.GitLabUser;
import com.mytholog.easyhireonboarding.repository.UserClient;
import com.mytholog.easyhireonboarding.service.impl.GitLabService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/gitlab")
public class GitLabController {

    private final UserClient gitLabClient;
    private final GitLabService gitLabService;

    public GitLabController(UserClient gitLabClient, GitLabService gitLabService) {
        this.gitLabClient = gitLabClient;
        this.gitLabService = gitLabService;
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

    @GetMapping("/download")
    public ResponseEntity<Map<String, String>> downloadProjects(@RequestParam String userName) {
        try {
            Path zipPath = gitLabService.downloadUserRepositoriesAsZip(userName);
            Map<String, String> response = Map.of(
                    "status", "success",
                    "zipPath", zipPath.toAbsolutePath().toString()
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", "error", "message", e.getMessage()));
        }
    }
}
