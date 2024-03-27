package me.lioncraft.forceitembattle;

import me.lioncraft.forceitembattle.commands.*;
import me.lioncraft.forceitembattle.listeners.listeners;
import me.lioncraft.forceitembattle.utilities.ButtonCreators;
import me.lioncraft.forceitembattle.utilities.data;
import me.lioncraft.forceitembattle.utilities.fibTask;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Team;


import java.util.*;
import java.util.stream.Collectors;

public final class ForceItemBattle extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        getServer().getPluginManager().registerEvents(new listeners(), this);
        getCommand("settings").setExecutor(new settingsCommand());
        getCommand("start").setExecutor(new startCommand(this));
        getCommand("finish").setExecutor(new finish());
        getCommand("forceitembattle").setExecutor(new forceItemBattledefaultCommand());
        getCommand("backpack").setExecutor(new backpack());
        saveDefaultConfig();
        new delayedStartup(plugin).runTaskLater(plugin, 2);
    }
    public static void saveBackpacks(){
        for(Team team : data.backpacks.keySet()){
            Inventory inv = data.backpacks.get(team);
            for(int i = 0; i < inv.getContents().length; i++){
                plugin.getConfig().set("data.itemrace.backpacks." + team.getName() + "." + i, inv.getItem(i));
            }
            plugin.getConfig().set("data.itemrace.backpacks." + team.getName() + ".amount", inv.getContents().length);

        }
        plugin.saveConfig();
    }
    public static void restoreBackpacks(){
        for(Team team : Bukkit.getScoreboardManager().getMainScoreboard().getTeams()){
            if(plugin.getConfig().getInt("data.itemrace.backpacks." + team.getName() + ".amount") > 0){
                Inventory inv = null;
                boolean b = true;
                for(String s : team.getEntries()){
                    b = false;
                    OfflinePlayer p = Bukkit.getOfflinePlayer(s);
                    if (p.getPlayer() != null) {
                        inv = Bukkit.createInventory(p.getPlayer(), 54, Component.text("Backpack", TextColor.color(255, 128, 0)));
                        break;
                    }else{
                        b = true;
                    }
                }
                if(b){
                    data.notRestoredBackpacks.add(team);
                }
                if (inv == null) {
                    plugin.getLogger().warning("Could not create Backpack (Teamname: " + team.getName() + ")");
                    break;
                }
                for(int i = 0; i < plugin.getConfig().getInt("data.itemrace.backpacks." + team.getName() + ".amount"); i++){
                    inv.setItem(i, plugin.getConfig().getItemStack("data.itemrace.backpacks." + team.getName() + "." + i));
                }
                data.backpacks.put(team, inv);
            }
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if(data.players != null) {
            if (!data.players.isEmpty()) {
                fibTask.saveData();
                saveBackpacks();
            }
        }

    }
    public static String messagePrefix;
    private static ForceItemBattle plugin;

    public static ForceItemBattle getPlugin() {
        return plugin;
    }
}
