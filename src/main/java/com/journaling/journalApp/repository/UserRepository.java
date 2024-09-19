package com.journaling.journalApp.repository;

import com.journaling.journalApp.entity.JournalEntry;
import com.journaling.journalApp.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, ObjectId> {
    User findByUserName(String username);

    void deleteByUserName(String name);
}
