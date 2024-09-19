package com.journaling.journalApp.repository;

import com.journaling.journalApp.entity.JournalEntry;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface JournalEntryRepository extends MongoRepository<JournalEntry, ObjectId> {
    List<JournalEntry> findByOwner(String owner);

    void deleteByOwner(String owner);

}
