package me.lioncraft.forceitembattle.utilities;

import me.lioncraft.forceitembattle.ForceItemBattle;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.Inventory;
import org.bukkit.scoreboard.Team;

import java.util.*;

public class data {
    public static HashMap<Team, Inventory> backpacks;
    public static List<Material> finalallowedItems;
    public static List<Material> notAllowedItems;
    public static List<EntityType> allowedEntities;
    public static boolean challengeisactive;
    public static HashMap<Team, Integer> ItemAmount;
    public static HashMap<Team, Integer> ItemAmountSave;
    public static List<Team> players;
    public static List<Team> notRestoredBackpacks;
    public static HashMap<UUID, Long> lastSkip;
    public static HashMap<Team, List<fibTask>> teamTasks;
    public static HashMap<Team, fibTask> currentTeamTask;
    public void InitializeItems(){
        Material[] mats = Material.values();
        List<Material> materialList = new ArrayList<>();
        List<String> names = new ArrayList<>();
        for(Material temp : mats){
            if(!temp.isEmpty()){
                if(!temp.isLegacy()) {
                    if (new forceItemUtils().isAllowedItem(temp)) {
                        materialList.add(temp);
                    }else{
                        names.add(temp.toString());
                    }
                }
            }
        }
        finalallowedItems = materialList;
        ForceItemBattle.getPlugin().getConfig().set("data.itemrace.denylist", names);
        ForceItemBattle.getPlugin().saveConfig();
    }
}
