package me.xxben.deathswap.commands;

import me.xxben.deathswap.DeathSwap;
import me.xxben.deathswap.game.GameManager;
import me.xxben.deathswap.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DeathSwapCMD implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] a) {

        GameManager gameManager = DeathSwap.getGameManager();

        // need to be opped to use death swap's commands
        if (sender.isOp()) {
            if (a.length == 3 && a[0].equalsIgnoreCase("start")) {
                // check if game is running already
                if (!gameManager.isGameRunning()) {
                    Player player1 = Bukkit.getPlayer(a[1]);
                    Player player2 = Bukkit.getPlayer(a[2]);

                    // check each player individually
                    if (player1 == null) {
                        sender.sendMessage(Utils.color("&c&l " + a[1] + " &cis not online"));
                        return true;
                    }

                    if (player2 == null) {
                        sender.sendMessage(Utils.color("&c&l" + a[2] + " &cis not online"));
                        return true;
                    }
                    // start game if both not null
                    gameManager.startGame(player1, player2);
                } else {
                    sender.sendMessage(Utils.color("&cYou cannot start a game if one is running"));
                }
            } else if (a.length == 1 && a[0].equalsIgnoreCase("stop")) {
                // check if game is not running
                if (gameManager.isGameRunning())
                    gameManager.endGame(null, true); // safe to null winner if true
                else
                    sender.sendMessage(Utils.color("&cYou cannot stop a game if one is not running"));
            } else if (a.length == 1 && a[0].equalsIgnoreCase("timeleft")) {
                if (gameManager.isGameRunning())
                    sender.sendMessage(Utils.color("&aThere is &a&l" + gameManager.getSecondsLeftInRound() + " &aseconds left"));
                else
                    sender.sendMessage(Utils.color("&aThere is no game running"));
            } else {
                sender.sendMessage(Utils.color("&a&lDeathSwap &aHelp"));
                sender.sendMessage(Utils.color("&8> &a/deathswap start (player1) (player2) &8- &7Start game"));
                sender.sendMessage(Utils.color("&8> &a/deathswap stop &8- &7Forcefully stop game"));
                sender.sendMessage(Utils.color("&8> &a/deathswap timeleft &8- &7Sends time left in seconds"));
                sender.sendMessage(Utils.color("&8> &a/deathswap help &8- &7Displays this help menu"));
            }
        } else {
            sender.sendMessage(Utils.color("&cYou do not have permission to do this"));
        }
        return false;
    }
}
