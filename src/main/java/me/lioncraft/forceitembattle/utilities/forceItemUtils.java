package me.lioncraft.forceitembattle.utilities;

import me.lioncraft.forceitembattle.ForceItemBattle;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class forceItemUtils {
    public ItemStack getRandomItem(){
        ItemStack item = new ItemStack(Material.COBBLESTONE);
        int random = new Random().nextInt(data.finalallowedItems.size()-1);
        item.setType(data.finalallowedItems.get(random));
        return item;
    }
    public boolean isAllowedItem(Material material){
        if(data.notAllowedItems.isEmpty()){
            return true;
        }
        return !data.notAllowedItems.contains(material);
    }
    public Biome getRandomBiome(){
        Biome[] biom = Biome.values();
        int i = new Random().nextInt(biom.length-1);
        return biom[i];
    }
    public EntityType getRandomEntity(){
        int random = new Random().nextInt(data.allowedEntities.size()-1);
        return data.allowedEntities.get(random);


    }
    public static void openResultGUI(Team team, int page, Player viewer){
        Inventory inv = Bukkit.createInventory(viewer, 54, ChatColor.GOLD + "RESULTS");
        inv.setContents(new ButtonCreators().block(54));
        ItemStack phead = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta phmeta = (SkullMeta) phead.getItemMeta();
        List<Component> lore2 = new ArrayList<>();
        lore2.add(Component.text("|----------"));
        for(String s : team.getEntries()){
            OfflinePlayer p = Bukkit.getOfflinePlayer(s);
            phmeta.setOwningPlayer(p);
            lore2.add(Component.text("|-" + s));
        }
        lore2.add(Component.text("|----------"));
        phmeta.setDisplayName(String.valueOf(page+1));
        phmeta.lore(lore2);
        phead.setItemMeta(phmeta);
        inv.setItem(4, phead);
        for(int i = page*36 ; i < data.ItemAmount.get(team) - 1; i++) {
            try {
                ItemStack itemStack = new ItemStack(Material.COBBLESTONE);
                ItemMeta ismeta = itemStack.getItemMeta();
                fibTask fibTask = data.teamTasks.get(team).get(i);
                List<Component> lore = new ArrayList<>();
                if(fibTask.isFinished) {
                    if (fibTask.isSkipped) {
                        lore.add(Component.text("Skipped Item.", TextColor.color(255, 0, 0)));
                        ismeta.addEnchant(Enchantment.VANISHING_CURSE, 1, true);
                        ismeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    }
                    if (fibTask.taskType.equals(TaskType.Item)) {
                        ismeta.displayName(Component.translatable(fibTask.material.translationKey(), TextColor.color(255, 10, 255)));
                        itemStack.setType(fibTask.material);
                    } else if (fibTask.taskType.equals(TaskType.Entity)) {
                        String value = fibTask.entityType.toString();
                        ismeta.displayName(Component.translatable(fibTask.entityType.translationKey(), TextColor.color(255, 255, 0)));
                        if (value.contains("SNOWMAN")) {
                            value = value.replace("SNOWMAN", "SNOW_GOLEM");
                        }
                        itemStack.setType(Material.valueOf(value + "_SPAWN_EGG"));
                    } else if (fibTask.taskType.equals(TaskType.Biome)) {
                        itemStack.setType(Material.DIAMOND_BOOTS);
                        ismeta.displayName(Component.translatable(fibTask.biome.translationKey(), TextColor.color(0, 255, 0)));
                    }
                }

                if (fibTask.finishTime != null) {
                    lore.add(fibTask.getFinishTime());
                }
                ismeta.lore(lore);
                itemStack.setItemMeta(ismeta);
                inv.setItem(i + 9 - (page * 36), itemStack);
                if (i == page * 36 + 35) {
                    inv.setItem(53, new ButtonCreators().nextPage());
                    break;
                }
            }catch (Exception ignored){
            }
        }
        viewer.openInventory(inv);
    }
    public void setNewTask(Team team, boolean skipped){
        if(data.currentTeamTask.containsKey(team)){
            data.teamTasks.get(team).get(data.teamTasks.get(team).indexOf(data.currentTeamTask.get(team))).setFinished(true);
            data.teamTasks.get(team).get(data.teamTasks.get(team).indexOf(data.currentTeamTask.get(team))).setSkipped(skipped);
        }
        data.ItemAmount.put(team, data.ItemAmount.get(team)+1);
        if(skipped){
            if(data.currentTeamTask.get(team).getMaterial() != null){
                for(String s : team.getEntries()){
                    Player p = Bukkit.getPlayer(s);
                    if (p != null) {
                        p.getInventory().addItem(new ItemStack(data.currentTeamTask.get(team).getMaterial()));
                        break;
                    }
                }
            }
        }
        int i = new Random().nextInt(10);
        String tablisttask;
        Component c = null;
        BossBar.Color bc;
        Component Message = null;
        if(i<=7){
            Material m;
            do {
                m = getRandomItem().getType();
            }while (!m.isItem());
            fibTask fibTask = new fibTask(team, m);
            data.teamTasks.get(team).add(fibTask);
            data.currentTeamTask.put(team, fibTask);
            tablisttask = m.getItemTranslationKey();
            c = Component.translatable(tablisttask);
            Message = Component.text(ForceItemBattle.messagePrefix).append(Component.text(ChatColor.AQUA + " NEW TASK: Habe ")).append(c).append(Component.text(ChatColor.AQUA + " im Inventar"));
            bc = BossBar.Color.PURPLE;
            new checkTask(fibTask).runTaskLater(ForceItemBattle.getPlugin(), 5);
        } else if (i<=8) {
            fibTask fibTask = new fibTask(team, getRandomBiome());
            data.teamTasks.get(team).add(fibTask);
            data.currentTeamTask.put(team, fibTask);
            tablisttask = fibTask.getBiome().translationKey();
            c = Component.translatable(tablisttask);
            Message = Component.text(ForceItemBattle.messagePrefix).append(Component.text(ChatColor.AQUA + " NEW TASK: Betrete ")).append(c);
            bc = BossBar.Color.BLUE;
            new checkTask(fibTask).runTaskLater(ForceItemBattle.getPlugin(), 5);
        } else {
            fibTask fibTask = new fibTask(team, getRandomEntity());
            data.teamTasks.get(team).add(fibTask);
            data.currentTeamTask.put(team, fibTask);
            tablisttask = fibTask.getEntityType().translationKey();
            c = Component.translatable(tablisttask);
            Message = Component.text(ForceItemBattle.messagePrefix).append(Component.text(ChatColor.AQUA + " NEW TASK: Töte ")).append(c);
            bc = BossBar.Color.YELLOW;
        }
        team.sendMessage(Message);
        Component prefix = Component.text(ChatColor.GRAY + "[").append(c).append(Component.text(ChatColor.GRAY + "] "));
        team.prefix(prefix);
        for(String s : team.getEntries()){
            Player p = Bukkit.getPlayer(s);
            if (p != null) {
                for(BossBar b : p.activeBossBars()){
                    b.color(bc);
                    b.name(c);
                }
                if(skipped){
                    p.playSound(p, Sound.ENTITY_ILLUSIONER_MIRROR_MOVE, 1.0f, 1.0f);
                }else{
                    p.playSound(p, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                }
            }
        }
    }
}
