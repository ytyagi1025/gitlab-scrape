package com.mytholog.easyhireonboarding.model;

import com.mytholog.easyhireonboarding.model.enumeration.FootprintType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document(collection = "footprints")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Footprint {
    @Id
    private String id;
    private String userId;
    private FootprintType footprintType;
    private Map<String, Object> data;
    private int version;
}

