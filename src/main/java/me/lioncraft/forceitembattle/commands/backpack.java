package me.lioncraft.forceitembattle.commands;

import me.lioncraft.forceitembattle.ForceItemBattle;
import me.lioncraft.forceitembattle.utilities.data;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class backpack implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(commandSender instanceof Player){
            if(ForceItemBattle.getPlugin().getConfig().getBoolean("settings.backpack")){
                Player p = (Player) commandSender;
                Team team = Bukkit.getScoreboardManager().getMainScoreboard().getPlayerTeam(p);
                if(team != null){
                    if(data.backpacks.containsKey(team)){
                        p.openInventory(data.backpacks.get(team));
                        p.playSound(p, Sound.BLOCK_AMETHYST_BLOCK_STEP, 1.0F, 1.0F);
                    }else{
                        Inventory inv = Bukkit.createInventory(p, 54, Component.text("Backpack", TextColor.color(255, 128, 0)));
                        data.backpacks.put(team, inv);
                        p.openInventory(inv);
                        p.playSound(p, Sound.BLOCK_AMETHYST_BLOCK_STEP, 1.0F, 1.0F);
                    }
                }else{
                    commandSender.sendMessage(ForceItemBattle.messagePrefix + " You are not in a Team!");
                }
            }else{
                commandSender.sendMessage(ForceItemBattle.messagePrefix + " Backpacks are not enabled in forceItemBattle.yml!");
            }
        }else{
            commandSender.sendMessage(ForceItemBattle.messagePrefix + " This Command can only be executed by a Player!");
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return new ArrayList<>();
    }
}
