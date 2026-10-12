
package com.example.jira.controller;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
public class Healthcontoller {

    @Autowired
    private MongoTemplate mongoTemplate;

    @GetMapping("/health")
    public String healthcheck() {
        try {
            mongoTemplate.getDb()
                    .runCommand(new Document("ping", 1));

            return "MongoDB connected Successfully";

        } catch (Exception e) {
            System.err.println(
                    "MongoDB connection failed: "
                    + e.getClass().getSimpleName()
            );

            return "MongoDB connection failed. Check server logs.";
        }
    }
}