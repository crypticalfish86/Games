package com.jace.games.model;

import java.time.LocalDateTime;

public record Profile(
        String username, //The username associated with the game profile
        ProfileType gameName, //The game the profile is a part of
        int wins, //The total wins on this game
        int losses, //The total losses on this game,
        LocalDateTime lastUpdated //The last time a game was played on this profile
) {

    public Profile incrementWins() {
        return new Profile(username, gameName, wins + 1, losses, LocalDateTime.now());
    }

    public Profile incrementLosses() {
        return new Profile(username, gameName, wins, losses + 1, LocalDateTime.now());
    }
}
