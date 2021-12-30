package me.xxben.deathswap;

import me.xxben.deathswap.commands.DeathSwapCMD;
import me.xxben.deathswap.game.GameManager;
import me.xxben.deathswap.listeners.DeathListener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class DeathSwap extends JavaPlugin {

    private static Plugin plugin;
    private static GameManager gameManager;

    @Override
    public void onEnable() {
        plugin = this;
        gameManager = new GameManager();

        // init event
        getServer().getPluginManager().registerEvents(new DeathListener(), this);
        // init command
        getCommand("deathswap").setExecutor(new DeathSwapCMD());

        getLogger().info("DeathSwap Enabled");
    }

    @Override
    public void onDisable() {
        gameManager = null;
        plugin = null;

        getLogger().info("DeathSwap Disabled");
    }

    // instance getters
    public static Plugin getInstance() {
        return plugin;
    }
    public static GameManager getGameManager() {
        return gameManager;
    }
}
