package com.mytholog.easyhireonboarding.model;

import lombok.Data;

@Data
public class GitLabProject {
    private String name;
    private String http_url_to_repo;
    private String default_branch;
}
