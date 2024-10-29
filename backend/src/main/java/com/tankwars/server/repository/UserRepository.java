package com.tankwars.server.repository;

import com.tankwars.server.model.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, ObjectId>{
    User findByUsername(String username);
    User findByEmail(String email);
    User findByResetToken(String resetToken);
}