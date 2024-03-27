package me.lioncraft.forceitembattle.commands;

import de.lioncraft.lionapi.messageHandling.defaultMessages;
import me.lioncraft.forceitembattle.ForceItemBattle;
import me.lioncraft.forceitembattle.utilities.ButtonCreators;
import me.lioncraft.forceitembattle.utilities.checkTask;
import me.lioncraft.forceitembattle.utilities.data;
import me.lioncraft.forceitembattle.utilities.forceItemUtils;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class forceItemBattledefaultCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length > 0) {
            switch (strings[0]) {
                case "getexact":
                    if (data.challengeisactive) {
                        Player p = (Player) commandSender;
                        Team team = Bukkit.getScoreboardManager().getMainScoreboard().getPlayerTeam(p);
                        if (data.currentTeamTask.get(team).getMaterial() != null) {
                            String name = data.currentTeamTask.get(team).getMaterial().toString();
                            p.sendMessage(ForceItemBattle.messagePrefix + ChatColor.AQUA + " Finde " + name + data.currentTeamTask.get(team).getMaterial().toString());

                        }else{
                            p.sendMessage(defaultMessages.messagePrefix.append(Component.text("You can only use this Command while your Task is an Item!")));
                        }
                    }
                    break;
                case "skip":
                    if (commandSender.isOp()) {
                        if (strings.length > 1) {
                            Player p = Bukkit.getPlayer(strings[1]);
                            if (p == null) {
                                commandSender.sendMessage("Could not find this Player");
                            } else {
                                Team team = Bukkit.getScoreboardManager().getMainScoreboard().getPlayerTeam(p);
                                new forceItemUtils().setNewTask(team, true);
                            }
                        }
                    } else {
                        commandSender.sendMessage("You are not allowed to perform this command!");
                    }
                    break;
                case "getresult":
                    if (strings.length > 2) {
                        forceItemUtils.openResultGUI(Bukkit.getScoreboardManager().getMainScoreboard().getTeam(strings[1]), Integer.parseInt(strings[2]), (Player) commandSender);
                    } else {
                        commandSender.sendMessage("Specify a Team!");
                    }
                    break;
                case "hidebossbar":
                    Player p = (Player) commandSender;
                    for (BossBar b : p.activeBossBars()) {
                        b.removeViewer(p);
                    }
                    break;
                case "showbossbar":
                    Player pl = (Player) commandSender;
                    BossBar bb = BossBar.bossBar(Component.text("nothing to do"), 1, BossBar.Color.PURPLE, BossBar.Overlay.NOTCHED_6);
                    bb.addViewer(pl);
                    Team t = Bukkit.getScoreboardManager().getMainScoreboard().getPlayerTeam(pl);
                    if(data.currentTeamTask.containsKey(t)){
                        bb.name(data.currentTeamTask.get(t).getTaskName());
                    }
                    break;
                case "givejoker":
                    if (commandSender.isOp()) {
                        if (strings.length > 2) {
                            try {
                                Player player = Bukkit.getPlayer(strings[1]);
                                if(player == null){
                                    commandSender.sendMessage(ForceItemBattle.messagePrefix + "Could not find that Player!");
                                    break;
                                }
                                player.getInventory().addItem(new ButtonCreators().Joker(Integer.parseInt(strings[2])));
                            }catch (NumberFormatException e){
                                commandSender.sendMessage(ForceItemBattle.messagePrefix + "Usage: /fib givejoker <player> <amount>");
                            }
                        } else {
                            commandSender.sendMessage("Usage: /fib givejoker <player> <amount>");
                        }
                    }
                    break;
                case "resume":
                    if (commandSender.isOp()) {
                        data.challengeisactive = true;
                    }

                    break;
                case "team":
                    if (strings.length >= 3) {
                        if (commandSender.isOp()) {
                            Player player = Bukkit.getPlayer(strings[1]);
                            if (player == null) {
                                commandSender.sendMessage("Could not find the Player " + strings[1]);
                            } else {
                                for (Team team : Bukkit.getScoreboardManager().getMainScoreboard().getTeams()) {
                                    if (team.hasPlayer(player)) {
                                        team.removePlayer(player);
                                        commandSender.sendMessage(ForceItemBattle.messagePrefix + " Successfully removed " + player.getName() + " from the Team " + team.getName());
                                    }
                                    if (team.getName().equals(strings[2])) {
                                        team.addPlayer(player);
                                        player.sendMessage(ForceItemBattle.messagePrefix + " You are now in team " + team.getName());
                                        commandSender.sendMessage(ForceItemBattle.messagePrefix + " Successfully added " + player.getName() + " to the Team " + team.getName());
                                    }
                                }
                            }
                        }
                    }
                    break;
                case "check":
                    Team team = Bukkit.getScoreboardManager().getMainScoreboard().getPlayerTeam((OfflinePlayer) commandSender);
                    if(data.currentTeamTask.get(team)!=null){
                        new checkTask(data.currentTeamTask.get(team)).runTaskLater(ForceItemBattle.getPlugin(), 5);
                    }
                    break;
                default:
                    commandSender.sendMessage(defaultMessages.wrongArgs);
            }
        }else{
            commandSender.sendMessage(defaultMessages.wrongArgs);
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        List<String> list = new ArrayList<>();
        if (strings.length == 1) {
            list.add("getexact");
            if (commandSender.isOp()) {
                list.add("skip");
                list.add("givejoker");
                list.add("resume");
                list.add("team");
            }

            if (!data.challengeisactive) {
                list.add("getresult");
            }
            list.add("hidebossbar");
            list.add("showbossbar");
        } else if (strings.length == 2) {
            return null;
        } else if (strings.length == 3) {
            for (Team team : Bukkit.getScoreboardManager().getMainScoreboard().getTeams()) {
                list.add(team.getName());
            }
        }

        return list;
    }
}
