package com.company.controller;

import com.company.controller.database.MongoController;
import com.company.controller.database.MongoCredentials;
import com.company.model.Greeting;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
public class GreetingController {

    private static final String template = "Hello, %s";

    @GetMapping("/hello")
    public Greeting getGreeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        Greeting g = new Greeting(String.format(template, name));

        MongoCredentials mongoCredentials = new MongoCredentials(
                "madalinoprea",
                "K4a2E!4yfwDaz-F",
                "stepitcluster.ce4kx.mongodb.net",
                "devDB");

        MongoController mc = new MongoController(mongoCredentials);

        MongoCollection<Document> coll = mc.getUsersCollection();

        Document doc = new Document();
        doc.append("content", g.getContent());
        coll.insertOne(doc);

        List<Document> documents = coll.find().into(new ArrayList<>());
        documents.forEach(db -> System.out.println(db.toJson()));

        return g;
    }

}
