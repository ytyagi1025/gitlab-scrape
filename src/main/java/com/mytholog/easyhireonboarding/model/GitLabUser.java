package com.mytholog.easyhireonboarding.model;

import lombok.Data;

@Data
public class GitLabUser {
    private Long id;
    private String name;
    private String username;
    private String web_url;
    private String avatar_url;
    private String state;
    private String locked;
    private String public_email;
}
