package me.lioncraft.forceitembattle.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class teamsCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(strings.length == 2){
            switch (strings[0]){
                case "leave":

                    break;
                case "join":

                    break;
            }
        }

        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        List<String> TabCompleter = new ArrayList<>();
        if(strings.length==1){
            TabCompleter.add("join");
            TabCompleter.add("leave");
        } else if (strings.length == 2) {
            for(int amount = 1; amount <= Bukkit.getOnlinePlayers().size(); amount++){
                TabCompleter.add(String.valueOf(amount));
            }
        }
        return TabCompleter;
    }
}
