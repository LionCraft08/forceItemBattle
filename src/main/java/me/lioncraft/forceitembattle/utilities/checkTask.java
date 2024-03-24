package me.lioncraft.forceitembattle.utilities;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;


public class checkTask extends BukkitRunnable {
    @Override
    public void run() {
        if(fibTask.isFinished){
            return;
        }
        if(fibTask.getBiome() != null){
            for(String playername : fibTask.getTeam().getEntries()){
                Player p = Bukkit.getPlayer(playername);
                if(p != null){
                    if (p.isOnline()) {
                        if(p.getWorld().getBiome(p.getLocation()).equals(fibTask.getBiome())){
                            new forceItemUtils().setNewTask(fibTask.getTeam(), false);
                            break;
                        }
                    }
                }
            }
        } else if (fibTask.getMaterial() != null) {
            for(String playername : fibTask.getTeam().getEntries()){
                Player p = Bukkit.getPlayer(playername);
                if(p != null){
                    if (p.isOnline()) {
                        for(ItemStack is : p.getInventory().getContents()){
                            if(is != null){
                                if(is.getType().equals(fibTask.getMaterial())){
                                    new forceItemUtils().setNewTask(fibTask.getTeam(), false);
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    fibTask fibTask;
    public checkTask(fibTask fibTask){
        this.fibTask = fibTask;
    }
}
