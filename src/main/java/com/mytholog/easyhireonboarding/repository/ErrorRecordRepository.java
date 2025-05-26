package com.mytholog.easyhireonboarding.repository;

import com.mytholog.easyhireonboarding.model.ErrorRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ErrorRecordRepository extends MongoRepository<ErrorRecord, String> {
}
