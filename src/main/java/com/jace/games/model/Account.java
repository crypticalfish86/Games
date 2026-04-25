package com.jace.games.model;

import java.time.LocalDateTime;
import java.util.List;


public record Account(
        String username, //The username of the account
        String password, //The hashed password of the account
        List<Profile> accountProfiles, //All the profiles associated with this account
        LocalDateTime dateCreated //The date the account was created on
) {

}
