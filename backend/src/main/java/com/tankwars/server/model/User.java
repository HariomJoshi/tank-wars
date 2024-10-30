package com.tankwars.server.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;



@Getter
@Setter
@NoArgsConstructor
@Document(collection = "users")
public class User{
    @Id
    private ObjectId id;
    @Indexed(unique = true)
    @NonNull
    private  String username;
    @NonNull
    private String password;
    @Indexed(unique = true)
    @NonNull
    private String email;


//    for otp sending
    private String otp;
    private boolean verified;
    private String resetToken;

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.verified = false; // Default value
    }
}
