package me.lioncraft.forceitembattle.commands;

import me.lioncraft.forceitembattle.utilities.ButtonCreators;
import me.lioncraft.forceitembattle.utilities.data;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class settingsCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(strings.length==0){
            Inventory inv = Bukkit.createInventory((Player) commandSender, 54, "FIB Settings");
            inv.setContents(new ButtonCreators().block(54));
            inv.setItem(49, new ButtonCreators().CloseButton());
            ItemStack is = new ItemStack(Material.CHAIN_COMMAND_BLOCK);
            is.getItemMeta().setDisplayName("click items in your inv to add them to the deny-list");
            inv.setItem(13, is);
            ((Player) commandSender).openInventory(inv);

        } else if (strings[0].equals("initialize")) {
            new data().InitializeItems();
            commandSender.sendMessage(ChatColor.GOLD + "the following items are not allowed:");
            for(Material notAllowed : data.notAllowedItems){
                commandSender.sendMessage(ChatColor.AQUA + notAllowed.name());
            }
            commandSender.sendMessage(ChatColor.GOLD + "successfully initialized the list.");
        } else if (strings[0].equals("temp")) {
            Player p = (Player) commandSender;
            commandSender.sendMessage(String.valueOf(data.ItemAmount.get(p.getUniqueId())));
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    List<String> list = new ArrayList<>();
    list.add("initialize");
    list.add("temp");
    return list;
    }
}
