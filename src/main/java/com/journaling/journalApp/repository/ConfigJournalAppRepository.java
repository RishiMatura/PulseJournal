package com.journaling.journalApp.repository;

import com.journaling.journalApp.entity.ConfigJournalAppEntity;
import com.journaling.journalApp.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConfigJournalAppRepository extends MongoRepository<ConfigJournalAppEntity, ObjectId> {

}
