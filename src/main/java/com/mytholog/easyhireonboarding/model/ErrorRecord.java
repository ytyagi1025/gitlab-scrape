package com.mytholog.easyhireonboarding.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document(collection = "errors")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorRecord {
    @Id
    private String id;
    private String userId;
    private String footprintId;
    private Map<String, Object> error;
}
