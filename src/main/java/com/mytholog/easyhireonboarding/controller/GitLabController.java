package com.mytholog.easyhireonboarding.controller;


import com.mytholog.easyhireonboarding.model.GitLabUser;
import com.mytholog.easyhireonboarding.repository.UserClient;
import com.mytholog.easyhireonboarding.service.S3Uploader;
import com.mytholog.easyhireonboarding.service.impl.GitLabService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
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
    private final S3Uploader uploaderService;

    @Value("${save.to.s3}")
    private boolean uploadToS3Bucket;

    public GitLabController(UserClient gitLabClient, GitLabService gitLabService, S3Uploader uploaderService) {
        this.gitLabClient = gitLabClient;
        this.gitLabService = gitLabService;
        this.uploaderService = uploaderService;
    }

    @GetMapping("/user")
    public ResponseEntity<GitLabUser> getUser(@AuthenticationPrincipal OidcUser user, @RequestParam String username) {
        List<GitLabUser> users = gitLabClient.getUserByUsername(username);
        if (!users.isEmpty()) {
            return new ResponseEntity<>(users.getFirst(), HttpStatus.OK); // Return the first match
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/download")
    public ResponseEntity<Map<String, String>> downloadProjects(@AuthenticationPrincipal OidcUser user, @RequestParam String userName) {
        try {
            String path = null;
            if (uploadToS3Bucket) {
                path = gitLabService.downloadUserRepositoriesAsZip(userName);
            } else {
                path = gitLabService.downloadUserRepositoriesAsZipLocally(userName);
            }

            Map<String, String> response = Map.of(
                    "status", "success",
                    "zipPath", path != null ? path : "No projects found"
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", "error", "message", e.getMessage()));
        }
    }

    @GetMapping("/authenticated/user")
    public String getUser(@AuthenticationPrincipal OidcUser user) {
        //just a demo api to test
        return "Hello, " + user.getFullName() + " (" + user.getEmail() + ")";
    }
}
