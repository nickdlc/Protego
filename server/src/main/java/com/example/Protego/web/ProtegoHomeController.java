package com.example.Protego.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProtegoHomeController {
    @RequestMapping("/")
    public String root() {
        return "hi";
    }

    @GetMapping("/getUser")
    public User getUser() {
        // Temporarily create a new user and return that
        User user = new User();
        user.setId(12);
        user.setName("spring test");

        return user;
    }

    @PostMapping("/createUser")
    public User homie(@RequestBody User user) {
        /* Save the user and send back a completed message */

        user.setName(user.getName() + " COMPLETED");
        FirebaseAttributes.firestore.createNewObject(new User(name="dvsdsdfsd"))
        return user;
    }
}
