package me.xxben.deathswap.game;

import me.xxben.deathswap.DeathSwap;
import me.xxben.deathswap.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class GameManager {

    private Game game;
    private int currentRoundSecs = 0;
    private BukkitTask timer, delay;

    public void startGame(Player player1, Player player2) {
        game = new Game(player1, player2);
        game.pickRoundTimeMinutes(3, 6); // pick between 3 and 6 minutes inclusively

        // broadcast and start timer
        broadcastMinutesLeft(game.getRoundTimeMinutes());
        startRoundScheduler();
    }

    public void endGame(Player winner, boolean forceStopped) {
        // cancel timer if not null!
        if (timer != null)
            timer.cancel();

        if (delay != null)
            delay.cancel();

        if (!forceStopped) {
            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage(Utils.color("&a&l" + winner.getName() + " HAS WON DEATH SWAP!"));
            Bukkit.broadcastMessage("");

            // 5 fireworks
            for (int i = 1; i <= 5; i++) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Firework firework = winner.getWorld().spawn(winner.getLocation(), Firework.class);
                        FireworkMeta meta = firework.getFireworkMeta();

                        meta.clearEffects();

                        // build the firework and then set the new one
                        FireworkEffect effect = FireworkEffect.builder()
                                .flicker(true)
                                .trail(true)
                                .with(FireworkEffect.Type.STAR)
                                .withColor(Color.ORANGE)
                                .withFade(Color.WHITE)
                                .build();

                        meta.setPower(5);
                        meta.addEffect(effect);
                        firework.setFireworkMeta(meta);
                    }
                }.runTaskLater(DeathSwap.getInstance(), 20 * 2);
            }
        } else {
            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage(Utils.color("&aDeath Swap was forcefully stopped"));
            Bukkit.broadcastMessage("");
        }
        // null game
        game = null;
    }

    public void nextRound() {
        game.pickRoundTimeMinutes(3, 6); // pick between 3 and 6 minutes inclusively

        // broadcast and start timer
        broadcastMinutesLeft(game.getRoundTimeMinutes());
        startRoundScheduler();
    }

    public void swapPlayers() {
        // get each then teleport
        Player player1 = game.getPlayer(0);
        Player player2 = game.getPlayer(1);
        Location player1Loc = player1.getLocation();
        Location player2Loc = player2.getLocation();

        // before each teleport, set their fall distance so if they try fall killing, wont damage them before tp
        player1.setFallDistance(0.0f);
        player1.teleport(player2Loc);

        player2.setFallDistance(0.0f);
        player2.teleport(player1Loc);
    }

    public boolean ifPlayerDied(Player player) {
        return game.getPlayer(0).getName().equalsIgnoreCase(player.getName()) || game.getPlayer(1).getName().equalsIgnoreCase(player.getName());
    }

    public void broadcastMinutesLeft(int minutesLeft) {
        Bukkit.broadcastMessage(Utils.color("&a&l" + minutesLeft + " &aminutes until swap!"));
    }

    public void broadcastSecondsLeft(int secondsLeft) {
        Bukkit.broadcastMessage(Utils.color("&a&l" + secondsLeft + " &aseconds until swap!"));
    }

    public void startRoundScheduler() {

        timer = new BukkitRunnable() {

            @Override
            public void run() {
                switch (getSecondsLeftInRound()) {

                    // time left alerts
                    case 60: broadcastSecondsLeft(60); break;
                    case 10: broadcastSecondsLeft(10); break;
                    case 9: broadcastSecondsLeft(9); break;
                    case 8: broadcastSecondsLeft(8); break;
                    case 7: broadcastSecondsLeft(7); break;
                    case 6: broadcastSecondsLeft(6); break;
                    case 5: broadcastSecondsLeft(5); break;
                    case 4: broadcastSecondsLeft(4); break;
                    case 3: broadcastSecondsLeft(3); break;
                    case 2: broadcastSecondsLeft(2); break;
                    case 1: broadcastSecondsLeft(1); break;

                    // no time left, cancel and swap
                    case 0: {
                        cancel();
                        swapPlayers();

                        // give 10s window before next round alert
                        delay = new BukkitRunnable() {
                            @Override
                            public void run() {
                                Bukkit.broadcastMessage("");
                                nextRound();
                                return;
                            }
                        }.runTaskLater(DeathSwap.getInstance(), 20 * 10);
                    }
                }
                // increment each second
                currentRoundSecs++;
            }
        }.runTaskTimer(DeathSwap.getInstance(), 20, 20);
    }

    // game accessor
    public Game getGame() {
        return game;
    }

    public boolean isGameRunning() {
        return game != null;
    }

    public int getSecondsLeftInRound() {
        return game.getRoundTimeSeconds() - currentRoundSecs;
    }
}
