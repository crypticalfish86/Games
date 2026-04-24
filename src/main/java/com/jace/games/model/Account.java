package com.jace.games.model;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;


public record Account(
        String username, //The username of the account
        String password, //The hashed password of the account
        ArrayList<Profile> accountProfiles, //All the profiles associated with this account
        LocalDateTime dateCreated //The date the account was created on
) {

}
