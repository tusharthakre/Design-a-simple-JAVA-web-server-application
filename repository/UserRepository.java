package com.example.AssignmentBackend.repository;

import com.example.AssignmentBackend.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, Integer> {
}

