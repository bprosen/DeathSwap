package me.xxben.deathswap.game;

import org.bukkit.entity.Player;

import java.util.concurrent.ThreadLocalRandom;

public class Game {

    private int roundTimeMinutes;
    private Player[] players = new Player[2];

    public Game(Player player1, Player player2) {
        // init partially filled array
        players[0] = player1;
        players[1] = player2;
    }

    // return in mins
    public int getRoundTimeMinutes() {
        return roundTimeMinutes;
    }

    // return in secs
    public int getRoundTimeSeconds() {
        return roundTimeMinutes * 60;
    }

    // get player based off of index
    public Player getPlayer(int index) {
        if (index <= players.length && index >= 0)
            return players[index];
        return null;
    }

    // return winner from loser
    public Player getWinner(Player loser) {
        if (players[0].getName().equalsIgnoreCase(loser.getName()))
            return players[1];
        else
            return players[0];
    }

    // return random time from min and max
    public void pickRoundTimeMinutes(int min, int max) {
        roundTimeMinutes = ThreadLocalRandom.current().nextInt(min, max + 1);
    }
}
