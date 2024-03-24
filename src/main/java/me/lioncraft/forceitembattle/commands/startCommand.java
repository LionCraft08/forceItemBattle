package me.lioncraft.forceitembattle.commands;

import me.lioncraft.forceitembattle.ForceItemBattle;
import me.lioncraft.forceitembattle.utilities.ButtonCreators;
import me.lioncraft.forceitembattle.utilities.data;
import me.lioncraft.forceitembattle.utilities.forceItemUtils;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class startCommand implements TabExecutor {
    private final ForceItemBattle plugin;

    public startCommand(ForceItemBattle plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        plugin.getServer().broadcast(Component.text(ForceItemBattle.messagePrefix).append(Component.text(" Challange started!", TextColor.color(28, 198, 206))));
        data.challengeisactive = true;
        for(Team team  : Bukkit.getScoreboardManager().getMainScoreboard().getTeams()){
            BossBar kb = BossBar.bossBar(Component.text("nothing to do"), 1, BossBar.Color.PURPLE, BossBar.Overlay.NOTCHED_6);
            boolean temp = true;
            data.teamTasks.put(team, new ArrayList<>());
            for(String playerName : team.getEntries()){
                Player p = Bukkit.getPlayer(playerName);
                if (p != null) {
                    p.showBossBar(kb);
                    if(temp){
                        p.getInventory().addItem(new ButtonCreators().Joker(Integer.parseInt(strings[0])));
                        temp = false;
                    }

                }
            }

            data.ItemAmount.put(team, 0);
            new forceItemUtils().setNewTask(team, false);
            data.players.add(team);
        }
        Player p = (Player) commandSender;
        p.performCommand("timer start " + strings[1] + " " + strings[2] + " " + strings[3] + " " + strings[4]);

        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        List<String> list = new ArrayList<>();
        switch (strings.length){
            case 1:
                list.add("<joker-amount>");
                list.add("1");
                list.add("2");
                list.add("3");
                break;
            case 2:
                list.add("<days>");
                break;
            case 3:
                list.add("<hours>");
                break;
            case 4:
                list.add("<minutes>");
                break;
            case 5:
                list.add("<seconds>");
                break;
        }

        return list;
    }
}
