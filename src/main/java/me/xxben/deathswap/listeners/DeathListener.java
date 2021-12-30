package me.xxben.deathswap.listeners;

import me.xxben.deathswap.DeathSwap;
import me.xxben.deathswap.game.GameManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathListener implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        GameManager gameManager = DeathSwap.getGameManager();

        // if game is running and they are in a game, end it!
        if (gameManager.isGameRunning() && gameManager.ifPlayerDied(player))
            gameManager.endGame(gameManager.getGame().getWinner(player), false);
    }
}
