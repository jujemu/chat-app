package com.chatapp.kakaka;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Profile("dev")
public class DevMongoDBInitializer implements CommandLineRunner {

    private final MongoTemplate mongoTemplate;

    @Override
    public void run(String... args) throws Exception {
        String collectionName = "chat";
        mongoTemplate.dropCollection(collectionName);
    }
}
