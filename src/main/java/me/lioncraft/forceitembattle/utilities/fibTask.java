package me.lioncraft.forceitembattle.utilities;

import me.lioncraft.forceitembattle.ForceItemBattle;
import me.yourname.lionsystems.LionSystems;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Biome;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class fibTask {
    boolean isFinished, isSkipped;
    Team team;
    TaskType taskType;
    EntityType entityType;
    Biome biome;
    Material material;
    Component chatMessage, messagePrefix, bossBarMessage;
    BossBar.Color barColor;
    Component finishTime;

    public Component getFinishTime() {
        return finishTime;
    }

    public fibTask(Team team, TaskType taskType) {
        biome = null;
        material = null;
        entityType = null;
        this.team = team;
        this.taskType = taskType;
        onGenerate();
    }

    public fibTask(Team team, EntityType entityType) {
        this.team = team;
        this.entityType = entityType;
        biome = null;
        material = null;
        taskType = TaskType.Entity;
        onGenerate();
    }

    public fibTask(Team team, Material material) {
        biome = null;
        entityType = null;
        this.team = team;
        this.material = material;
        taskType = TaskType.Item;
        onGenerate();
    }

    public fibTask(Team team, Biome biome) {
        material = null;
        entityType = null;
        this.team = team;
        this.biome = biome;
        taskType = TaskType.Biome;
        onGenerate();
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
        if(finished){
            finishTime = LionSystems.finaltimerMessage;
        }
    }

    public void setSkipped(boolean skipped) {
        isSkipped = skipped;
    }

    public String getPersistentData(){
        String string = taskType.toString() + ":";
        if(taskType.equals(TaskType.Entity)){
            string = string + entityType.toString();
        } else if (taskType.equals(TaskType.Item)) {
            string = string + material.toString();
        } else if (taskType.equals(TaskType.Biome)) {
            string = string + biome.toString();
        }
        string = string + ":" + isSkipped + ":" + isFinished;
        //taskType:Entity/Material/Biome:skipped:finished
        return string;
    }
    private void onGenerate() {
        finishTime = null;
        isFinished = false;
    }

    public static void saveData(){
        List<String> list = new ArrayList<>();
        for(Team team : Bukkit.getScoreboardManager().getMainScoreboard().getTeams()){
            List<String> taskStrings = new ArrayList<>();
            if(data.teamTasks.containsKey(team)) {
                for (fibTask fibTask : data.teamTasks.get(team)) {
                    taskStrings.add(fibTask.getPersistentData());
                }
            }
            ForceItemBattle.getPlugin().getConfig().set("forceitembattle.data." + team.getName(), taskStrings);
            list.add(team.getName());
        }
        ForceItemBattle.getPlugin().getConfig().set("forceitembattle.teams", list);
        ForceItemBattle.getPlugin().saveConfig();
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public Team getTeam() {
        return team;
    }

    @Nullable
    public EntityType getEntityType() {
        return entityType;
    }
    @Nullable
    public Biome getBiome() {
        return biome;
    }
    @Nullable
    public Material getMaterial() {
        return material;
    }
    public Component getTaskName(){
        Component c = null;
        if(material != null){
            c = Component.translatable(material.translationKey());
        } else if (entityType != null) {
            c = Component.translatable(entityType.translationKey());
        } else if (biome != null) {
            c = Component.translatable(biome.translationKey());
        }
        return c;
    }

    public static void restoreData(){
        for(String s : ForceItemBattle.getPlugin().getConfig().getStringList("forceitembattle.teams")){
            Team team = Bukkit.getScoreboardManager().getMainScoreboard().getTeam(s);
            data.teamTasks.put(team, new ArrayList<>());
            int i = 0;
            for(String task : ForceItemBattle.getPlugin().getConfig().getStringList("forceitembattle.data." + s)){
                i++;
                String type = task.substring(task.indexOf(':') + 1, task.indexOf(':', task.indexOf(':') + 1));
                fibTask fibTask;
                if(task.startsWith(TaskType.Item.toString())){
                    fibTask = new fibTask(team, Material.getMaterial(type));

                } else if (task.startsWith(TaskType.Entity.toString())) {
                    fibTask = new fibTask(team, EntityType.valueOf(type));
                } else {
                    fibTask = new fibTask(team, Biome.valueOf(type));
                }
                fibTask.setSkipped(Boolean.parseBoolean(task.substring(task.indexOf(':', task.indexOf(':' + 1)), task.lastIndexOf(':'))));
                fibTask.setFinished(Boolean.parseBoolean(task.substring(task.lastIndexOf(':')+1)));
                if(!fibTask.isFinished){
                    data.currentTeamTask.put(team, fibTask);
                }
                //taskType:Entity/Material/Biom:skipped:finished
                data.teamTasks.get(team).add(fibTask);
            }
            data.ItemAmount.put(team, i);
        }
    }
}
