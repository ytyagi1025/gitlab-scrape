package com.mytholog.easyhireonboarding.service.impl;

import com.mytholog.easyhireonboarding.model.GitLabProject;
import com.mytholog.easyhireonboarding.repository.UserClient;
import com.mytholog.easyhireonboarding.service.S3Uploader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@Slf4j
public class GitLabService {
    private final UserClient gitLabClient;
    private final S3Uploader s3Uploader;

    public GitLabService(UserClient gitLabClient, S3Uploader s3Uploader) {
        this.gitLabClient = gitLabClient;
        this.s3Uploader = s3Uploader;
    }

    @Retryable(
            value = { IOException.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000)
    )
    public byte[] downloadArchive(String archiveUrl) throws IOException {
        try (InputStream in = new URL(archiveUrl).openStream()) {
            return in.readAllBytes();
        }
    }

    public String downloadUserRepositoriesAsZipLocally(String username) throws IOException {
        List<GitLabProject> projects = gitLabClient.getUserProjects(username);
        if (projects.isEmpty()) {
            log.warn("No projects found for username {}", username);
            return null;
        }

        //path where file will be stored locally
        Path outputDir = Paths.get("downloads");
        Files.createDirectories(outputDir);
        Path outputZip = outputDir.resolve(username + "-repositories.zip");
        try (ZipOutputStream zipStream = new ZipOutputStream(Files.newOutputStream(outputZip))) {
            for (GitLabProject project : projects) {
                String archiveUrl = String.format("%s/-/archive/%s/%s-%s.zip",
                        project.getHttp_url_to_repo().replace(".git", ""),
                        project.getDefault_branch(),
                        project.getName(),
                        project.getDefault_branch());

                byte[] repoZip = null;
                try {
                    repoZip = downloadArchive(archiveUrl);
                } catch (Exception ex) {
                    // This should never be reached because of @Recover fallback,
                    // but just in case log it
                    log.error("Unexpected error downloading archive {}: {}", archiveUrl, ex.getMessage());
                }

                if (repoZip != null) {
                    zipStream.putNextEntry(new ZipEntry(project.getName() + ".zip"));
                    zipStream.write(repoZip);
                    zipStream.closeEntry();
                } else {
                    log.warn("Skipping project {} due to download failure.", project.getName());
                }
                //break; // this is for testing locally and will be removed
            }
        }

        return outputZip.toAbsolutePath().toString();
    }

    public String downloadUserRepositoriesAsZip(String username) throws IOException {
        List<GitLabProject> projects = gitLabClient.getUserProjects(username);
        String s3Path = null;
        if (projects.isEmpty()) {
            log.warn("No projects found for username {}", username);
            return null;
        }

        ByteArrayOutputStream zipBuffer = new ByteArrayOutputStream();
        try (ZipOutputStream zipStream = new ZipOutputStream(zipBuffer)) {
            for (GitLabProject project : projects) {
                String archiveUrl = String.format("%s/-/archive/%s/%s-%s.zip",
                        project.getHttp_url_to_repo().replace(".git", ""),
                        project.getDefault_branch(),
                        project.getName(),
                        project.getDefault_branch());

                byte[] repoZip = null;
                try {
                    repoZip = downloadArchive(archiveUrl);
                } catch (Exception ex) {
                    // This should never be reached because of @Recover fallback,
                    // but just in case log it
                    log.error("Unexpected error downloading archive {}: {}", archiveUrl, ex.getMessage());
                }

                if (repoZip != null) {
                    zipStream.putNextEntry(new ZipEntry(project.getName() + ".zip"));
                    zipStream.write(repoZip);
                    zipStream.closeEntry();
                } else {
                    log.warn("Skipping project {} due to download failure.", project.getName());
                }
                //break; // this is for testing locally and will be removed
            }
            String s3Key = "gitlab-repos/" + username + "-repos.zip";
            s3Path = s3Uploader.uploadZip(zipBuffer.toByteArray(), s3Key);
        }

        return s3Path;
    }

    @Recover
    public byte[] recover(IOException ex, String archiveUrl) {
        log.error("Failed to download archive {} after retries. Error: {}", archiveUrl, ex.getMessage());
        return null; // indicate failure so the caller can skip this entry
    }
}
