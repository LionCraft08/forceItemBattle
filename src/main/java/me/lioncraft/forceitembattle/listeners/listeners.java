package me.lioncraft.forceitembattle.listeners;

import io.papermc.paper.event.player.PlayerInventorySlotChangeEvent;
import me.lioncraft.forceitembattle.ForceItemBattle;
import me.lioncraft.forceitembattle.utilities.ButtonCreators;
import me.lioncraft.forceitembattle.utilities.data;
import me.lioncraft.forceitembattle.utilities.fibTask;
import me.lioncraft.forceitembattle.utilities.forceItemUtils;
import me.yourname.lionsystems.events.TimerTickEvent;
import me.yourname.lionsystems.events.timerFinishEvent;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scoreboard.Team;

import java.util.*;

import static org.bukkit.Bukkit.getScoreboardManager;

public class listeners implements Listener {

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        if (e.getView().getTitle().equals("FIB Settings")) {
            if (!(e.getCurrentItem() == null)) {
                e.setCancelled(true);
                if (e.getCurrentItem().equals(new ButtonCreators().CloseButton())) {
                    e.getWhoClicked().closeInventory();
                } else {
                    if (!data.notAllowedItems.contains(e.getCurrentItem().getType())) {
                        data.notAllowedItems.add(e.getCurrentItem().getType());
                        e.getWhoClicked().sendMessage("Successfully added " + e.getCurrentItem().getType().name() + "to the deny-list!");
                    } else {
                        data.notAllowedItems.remove(e.getCurrentItem().getType());
                        e.getWhoClicked().sendMessage("Successfully removed " + e.getCurrentItem().getType().name() + "from the deny-list!");
                    }
                }
            }
        } else if (e.getCurrentItem() != null) {
            if (e.getView().getTitle().equals(ChatColor.GOLD + "RESULTS")) {
                e.setCancelled(true);
                if (e.getCurrentItem().equals(new ButtonCreators().nextPage())) {
                    SkullMeta sm = (SkullMeta) e.getClickedInventory().getItem(4).getItemMeta();
                    new forceItemUtils().openResultGUI(getScoreboardManager().getMainScoreboard().getPlayerTeam(sm.getOwningPlayer()), Integer.parseInt(sm.getDisplayName()), (Player) e.getWhoClicked());
                }
            }


        }
    }

    @EventHandler
    public void onBiomeEnter(PlayerMoveEvent e) {
        Team team = getScoreboardManager().getMainScoreboard().getPlayerTeam(e.getPlayer());
        if (data.currentTeamTask.containsKey(team)) {
            if (data.currentTeamTask.get(team).getBiome() != null) {
                if (Objects.equals(e.getPlayer().getWorld().getBiome(e.getPlayer().getLocation()), data.currentTeamTask.get(team).getBiome())) {
                    new forceItemUtils().setNewTask(team, false);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Component c = Component.text(ForceItemBattle.messagePrefix + " Your task is: ");
        Component c2 = Component.text("");
        Team team = null;

        for (Team t : getScoreboardManager().getMainScoreboard().getTeams()) {
            if (t.hasPlayer(e.getPlayer())) {
                team = t;
                break;
            }
        }
        if (team == null) {
            team = getScoreboardManager().getMainScoreboard().registerNewTeam(String.valueOf(getScoreboardManager().getMainScoreboard().getTeams().size()));
            team.addPlayer(e.getPlayer());
        }
        if (!data.ItemAmount.containsKey(team)) {
            data.ItemAmount.put(team, 0);
        }
        if (data.currentTeamTask.containsKey(team)) {
            if (data.currentTeamTask.get(team).getEntityType() != null) {
                c2 = c2.append(Component.translatable(data.currentTeamTask.get(team).getEntityType().translationKey()));
                c = c.append(Component.text("Töte "));
                c = c.append(c2);
                e.getPlayer().sendMessage(c);
            } else if (data.currentTeamTask.get(team).getMaterial() != null) {
                c2 = c2.append(Component.translatable(data.currentTeamTask.get(team).getMaterial().translationKey()));
                c = c.append(Component.text("Finde "));
                c = c.append(c2);
                e.getPlayer().sendMessage(c);
            } else if (data.currentTeamTask.get(team).getBiome() != null) {
                c = c.append(Component.text("Betrete "));
                c2 = c2.append(Component.translatable(data.currentTeamTask.get(team).getBiome().translationKey()));
                c = c.append(c2);
                e.getPlayer().sendMessage(c);
            } else {
                if (data.challengeisactive) {

                }
            }
        } else if (data.challengeisactive) {
            new forceItemUtils().setNewTask(team, false);
        }
        if (data.challengeisactive) {
            Component prefix = Component.text(ChatColor.GRAY + "[").append(c2).append(Component.text(ChatColor.GRAY + "] ")).append(e.getPlayer().displayName());
            e.getPlayer().playerListName(prefix);
            BossBar bb = BossBar.bossBar(c2, 1.0F, BossBar.Color.BLUE, BossBar.Overlay.NOTCHED_6);
            bb.addViewer(e.getPlayer());
        }
        e.getPlayer().sendPlayerListHeader(Component.text(ForceItemBattle.messagePrefix));
        if(data.notRestoredBackpacks.contains(team)){
            Inventory inv = Bukkit.createInventory(e.getPlayer(), 54, Component.text("Backpack", TextColor.color(255, 128, 0)));
            for(int i = 0; i < ForceItemBattle.getPlugin().getConfig().getInt("data.itemrace.backpacks." + team.getName() + ".amount"); i++){
                inv.setItem(i, ForceItemBattle.getPlugin().getConfig().getItemStack("data.itemrace.backpacks." + team.getName() + "." + i));
            }
            data.backpacks.put(team, inv);
        }
    }

    @EventHandler
    public void onKill(EntityDeathEvent e) {
        if (data.challengeisactive) {
            if (e.getEntity().getKiller() != null) {
                Team team = getScoreboardManager().getMainScoreboard().getPlayerTeam(e.getEntity().getKiller());
                if (data.currentTeamTask.containsKey(team)) {
                    if (Objects.equals(data.currentTeamTask.get(team).getEntityType(), e.getEntityType())) {
                        new forceItemUtils().setNewTask(team, false);
                    }
                }
            }
        }

    }

    @EventHandler
    public void itemUse(PlayerInteractEvent e) {
        if (data.challengeisactive) {
            if (e.getItem() == null) {
                return;
            }
            if (Objects.requireNonNull(e.getItem()).isSimilar(ButtonCreators.skip)) {
                if (data.lastSkip.containsKey(e.getPlayer().getUniqueId())) {
                    if ((data.lastSkip.get(e.getPlayer().getUniqueId()) + 5000) > System.currentTimeMillis()) {
                        e.getPlayer().sendMessage(ForceItemBattle.messagePrefix + ChatColor.RED + " The skip is on cool down!");
                        return;
                    }
                }
                if (e.getAction().isRightClick()) {
                    Team team = getScoreboardManager().getMainScoreboard().getPlayerTeam(e.getPlayer());
                    data.lastSkip.put(e.getPlayer().getUniqueId(), System.currentTimeMillis());

                    if (team != null) {
                        e.getPlayer().sendMessage(ForceItemBattle.messagePrefix + ChatColor.RED + " You successfully skipped this Task!");
                        for (String s : team.getEntries()) {
                            OfflinePlayer p = Bukkit.getOfflinePlayer(s);
                            if (p.isOnline()) {
                                Objects.requireNonNull(p.getPlayer()).sendMessage(Component.text(ForceItemBattle.messagePrefix + " ").append(e.getPlayer().displayName()).append(Component.text(" skipped your current Task.")));
                            }
                        }
                        new forceItemUtils().setNewTask(team, true);
                        e.setCancelled(true);
                        e.getItem().setAmount(e.getItem().getAmount() - 1);
                    } else {
                        e.getPlayer().sendMessage(ForceItemBattle.messagePrefix + " Couldn't skip the Task. Make sure you are in a Team.");
                    }
                }


            }
        }
    }
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e){
        if(e.getItemInHand().isSimilar(ButtonCreators.skip)){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void timerFinish(timerFinishEvent e) {
        data.challengeisactive = false;
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.setInvulnerable(true);
            p.setAllowFlight(true);
            p.setCanPickupItems(false);
            p.playSound(p, Sound.ENTITY_ENDER_DRAGON_DEATH, 1.0f, 1.0f);
        }
        data.ItemAmountSave = new HashMap<>();
        for (Team team : data.ItemAmount.keySet()) {
            data.ItemAmountSave.put(team, data.ItemAmount.get(team));
        }
    }

    @EventHandler
    public void onTimerTick(TimerTickEvent e) {
        if(e.getTimerCurrent() > e.getTimerAtStart()){
            return;
        }
        for (Player p : Bukkit.getOnlinePlayers()) {
            for (BossBar b : p.activeBossBars()) {
                b.progress((float) e.getTimerCurrent() / e.getTimerAtStart());
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        ItemStack temp = new ButtonCreators().Joker(1);
        List<ItemStack> templist = new ArrayList<>();
        for(ItemStack is : event.getDrops()){
            if(is != null){
                if(is.isSimilar(temp)){
                    templist.add(is);
                }
            }
        }
        try {
            for (ItemStack is : templist) {
                if (is != null) {
                    try {
                        if (is.isSimilar(temp)) {
                            event.getItemsToKeep().add(is);
                            event.getDrops().remove(is);
                        }
                    } catch (ConcurrentModificationException e) {
                        Bukkit.getServer().getLogger().warning(ForceItemBattle.messagePrefix + "An error occurred in forceItemBattle.DeathEvent!");
                        Bukkit.getServer().getLogger().warning(ForceItemBattle.messagePrefix + " Do not report this unless items disappeared / got duplicated!");
                    }
                }
            }
        } catch (ConcurrentModificationException e) {
            Bukkit.getServer().getLogger().warning(ForceItemBattle.messagePrefix + "An error occurred in forceItemBattle.DeathEvent!");
            Bukkit.getServer().getLogger().warning(ForceItemBattle.messagePrefix + " Do not report this unless items disappeared / got duplicated!");
        }
    }

    @EventHandler
    public void onInvChange(PlayerInventorySlotChangeEvent e) {
        if (data.challengeisactive) {
            Team team = getScoreboardManager().getMainScoreboard().getPlayerTeam(e.getPlayer());
            if (data.currentTeamTask.containsKey(team)) {
                if (Objects.equals(data.currentTeamTask.get(team).getMaterial(), e.getNewItemStack().getType())) {
                    new forceItemUtils().setNewTask(team, false);
                }
            }
        }
    }

    @EventHandler
    public void worldSave(WorldSaveEvent e) {
        fibTask.saveData();
        ForceItemBattle.saveBackpacks();
    }
}
