package com.example.Protego.web;

import org.springframework.web.bind.annotation.*;

@RestController
public class ProtegoHomeController {
    @RequestMapping("/")
    public String root() {
        return "hi";
    }

    @GetMapping("/getUser")
    public ProtegoUser getUser() {
        // Temporarily create a new user and return that
        ProtegoUser user = new ProtegoUser();
        user.setId(12);
        user.setName("spring test");

        return user;
    }

    @PostMapping("/createUser")
    public ProtegoUser homie(@RequestBody ProtegoUser user) {
        /* Save the user and send back a completed message */

        user.setName(user.getName() + " COMPLETED");
        return user;
    }
}
