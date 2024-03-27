package me.lioncraft.forceitembattle;

import de.lioncraft.lionapi.mainClass;
import me.lioncraft.forceitembattle.utilities.ButtonCreators;
import me.lioncraft.forceitembattle.utilities.data;
import me.lioncraft.forceitembattle.utilities.fibTask;
import me.yourname.lionsystems.LionSystems;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

public class delayedStartup extends BukkitRunnable {

    @Override
    public void run() {
        try {
            if (Bukkit.getPluginManager().isPluginEnabled(mainClass.getPlugin())) {
                if (Bukkit.getPluginManager().isPluginEnabled(LionSystems.getPlugin())) {
                    data.notAllowedItems = new ArrayList<>();
                    data.finalallowedItems = new ArrayList<>();
                    ForceItemBattle.messagePrefix = ChatColor.translateAlternateColorCodes('&', "<&l&9Lion&5Systems&r>");
                    data.allowedEntities = Arrays.stream(EntityType.values()).collect(Collectors.toList());
                    data.allowedEntities.remove(EntityType.AREA_EFFECT_CLOUD);
                    data.allowedEntities.remove(EntityType.ARROW);
                    data.allowedEntities.remove(EntityType.BLOCK_DISPLAY);
                    data.allowedEntities.remove(EntityType.DRAGON_FIREBALL);
                    data.allowedEntities.remove(EntityType.DROPPED_ITEM);
                    data.allowedEntities.remove(EntityType.EGG);
                    data.allowedEntities.remove(EntityType.ENDER_CRYSTAL);
                    data.allowedEntities.remove(EntityType.ENDER_PEARL);
                    data.allowedEntities.remove(EntityType.ENDER_SIGNAL);
                    data.allowedEntities.remove(EntityType.EVOKER_FANGS);
                    data.allowedEntities.remove(EntityType.EXPERIENCE_ORB);
                    data.allowedEntities.remove(EntityType.FALLING_BLOCK);
                    data.allowedEntities.remove(EntityType.FIREBALL);
                    data.allowedEntities.remove(EntityType.FIREWORK);
                    data.allowedEntities.remove(EntityType.FISHING_HOOK);
                    data.allowedEntities.remove(EntityType.GIANT);
                    data.allowedEntities.remove(EntityType.GLOW_ITEM_FRAME);
                    data.allowedEntities.remove(EntityType.ILLUSIONER);
                    data.allowedEntities.remove(EntityType.INTERACTION);
                    data.allowedEntities.remove(EntityType.ITEM_DISPLAY);
                    data.allowedEntities.remove(EntityType.ITEM_FRAME);
                    data.allowedEntities.remove(EntityType.LEASH_HITCH);
                    data.allowedEntities.remove(EntityType.LLAMA_SPIT);
                    data.allowedEntities.remove(EntityType.LIGHTNING);
                    data.allowedEntities.remove(EntityType.MARKER);
                    data.allowedEntities.remove(EntityType.MINECART);
                    data.allowedEntities.remove(EntityType.MINECART_CHEST);
                    data.allowedEntities.remove(EntityType.MINECART_COMMAND);
                    data.allowedEntities.remove(EntityType.MINECART_FURNACE);
                    data.allowedEntities.remove(EntityType.MINECART_HOPPER);
                    data.allowedEntities.remove(EntityType.MINECART_MOB_SPAWNER);
                    data.allowedEntities.remove(EntityType.MINECART_TNT);
                    data.allowedEntities.remove(EntityType.PAINTING);
                    data.allowedEntities.remove(EntityType.PLAYER);
                    data.allowedEntities.remove(EntityType.PRIMED_TNT);
                    data.allowedEntities.remove(EntityType.SHULKER_BULLET);
                    data.allowedEntities.remove(EntityType.SMALL_FIREBALL);
                    data.allowedEntities.remove(EntityType.SNOWBALL);
                    data.allowedEntities.remove(EntityType.SPECTRAL_ARROW);
                    data.allowedEntities.remove(EntityType.SPLASH_POTION);
                    data.allowedEntities.remove(EntityType.TEXT_DISPLAY);
                    data.allowedEntities.remove(EntityType.THROWN_EXP_BOTTLE);
                    data.allowedEntities.remove(EntityType.UNKNOWN);
                    data.allowedEntities.remove(EntityType.ZOMBIE_HORSE);
                    data.allowedEntities.remove(EntityType.WITHER_SKULL);
                    data.allowedEntities.remove(EntityType.TRIDENT);
                    data.allowedEntities.remove(EntityType.BOAT);
                    data.allowedEntities.remove(EntityType.CHEST_BOAT);
                    data.allowedEntities.remove(EntityType.ARMOR_STAND);
                    data.ItemAmount = new HashMap<>();
                    data.lastSkip = new HashMap<>();
                    data.players = new ArrayList<>();
                    data.teamTasks = new HashMap<>();
                    data.backpacks = new HashMap<>();
                    data.currentTeamTask = new HashMap<>();
                    data.notRestoredBackpacks = new ArrayList<>();
                    ButtonCreators.skip = new ButtonCreators().Joker(1);
                    if (!plugin.getConfig().getStringList("data.itemrace.denylist").isEmpty()) {
                        for (String s : plugin.getConfig().getStringList("data.itemrace.denylist")) {
                            data.notAllowedItems.add(Material.valueOf(s));
                        }
                        new data().InitializeItems();
                    }
                    fibTask.restoreData();
                    ForceItemBattle.restoreBackpacks();
                    return;
                } else {
                    Bukkit.getLogger().warning("<ForceItemBattle> This Plugin requires me.yourname.LionSystems to work");
                }
            } else {
                Bukkit.getLogger().warning("<ForceItemBattle> This Plugin requires de.lioncraft.lionapi");
            }
        }catch (NoClassDefFoundError e){
            Bukkit.getLogger().warning("<ForceItemBattle> This Plugin requires de.lioncraft.lionapi");
            Bukkit.getPluginManager().disablePlugin(ForceItemBattle.getPlugin());
        }
        Bukkit.getPluginManager().disablePlugin(ForceItemBattle.getPlugin());
    }
    Plugin plugin;
    public delayedStartup(Plugin plugin){
        this.plugin = plugin;
    }
}
