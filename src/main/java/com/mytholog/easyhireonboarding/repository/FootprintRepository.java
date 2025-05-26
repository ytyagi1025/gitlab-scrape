package com.mytholog.easyhireonboarding.repository;

import com.mytholog.easyhireonboarding.model.Footprint;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FootprintRepository extends MongoRepository<Footprint, String> {
}
