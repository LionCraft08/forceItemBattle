package me.lioncraft.forceitembattle.commands;

import me.lioncraft.forceitembattle.ForceItemBattle;
import me.lioncraft.forceitembattle.utilities.data;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.PatternReplacementResult;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.regex.MatchResult;

public class finish implements TabExecutor{
    public static List<Integer> intlist;
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(data.challengeisactive){
            data.challengeisactive = false;
        }
        if(strings.length == 1){
            if(strings[0].equals("results")){
                commandSender.sendMessage(ForceItemBattle.messagePrefix + " Remove args!");
            }
        }else{
            if(intlist == null){
                intlist = new ArrayList<>();
                 for(Team team : data.ItemAmount.keySet()){
                     intlist.add(data.ItemAmount.get(team));
                 }
            }
            intlist.sort(Comparator.naturalOrder());
            Team team = getUUIDbyValue(intlist.get(0));

            if(team == null){
                commandSender.sendMessage(ForceItemBattle.messagePrefix + "Alle Teams wurden bereits abgefragt!");
                return false;
            }
            if(team.getSize() == 0){
                while (team.getSize() == 0){
                    intlist.remove(0);
                    team = getUUIDbyValue(intlist.get(0));
                }
            }
                team.sendActionBar(Component.text("Platz " + intlist.size()));
                Component c = Component.text(ForceItemBattle.messagePrefix + " Platz " + intlist.size() + ": " + team.getName() + " (" + (data.ItemAmount.get(team) - 1)+ " Aufgaben)");
                c = c.clickEvent(ClickEvent.runCommand("/fib getresult " + team.getName() + " 0"));
                c = c.hoverEvent(HoverEvent.showText(Component.text("Click to open the overview of Team " + team.getName())));
                ForceItemBattle.getPlugin().getServer().broadcast(c);
                Component playerlist = Component.text(ForceItemBattle.messagePrefix + " Spieler: ");
                for(String string : team.getEntries()){
                    playerlist = playerlist.append(Component.text(string + ", "));
                }
                playerlist = playerlist.replaceText(TextReplacementConfig.builder().match(",").replacement("").condition(new TextReplacementConfig.Condition() {
                    @Override
                    public @NotNull PatternReplacementResult shouldReplace(@NotNull MatchResult result, int matchCount, int replaced) {
                        if(replaced == matchCount - 1){
                            return PatternReplacementResult.REPLACE;
                        }
                        return PatternReplacementResult.CONTINUE;
                    }
                }).build());
                ForceItemBattle.getPlugin().getServer().broadcast(playerlist);
                intlist.remove(0);
        }
        data.challengeisactive = false;
        return false;
    }

    public Team getUUIDbyValue(int value){
        Set<Team> teams = data.ItemAmountSave.keySet();
        for(Team id : teams){
            if(data.ItemAmountSave.get(id).equals(value)){
                data.ItemAmountSave.remove(id);
                return id;
            }
        }
        return null;
    }
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return new ArrayList<>(Collections.singleton(""));
    }
}
