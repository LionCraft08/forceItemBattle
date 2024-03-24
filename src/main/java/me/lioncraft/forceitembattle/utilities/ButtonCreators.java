package me.lioncraft.forceitembattle.utilities;

import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class ButtonCreators {
    public ItemStack CloseButton(){
        ItemStack close = new ItemStack(Material.BARRIER);
        ItemMeta closemeta = close.getItemMeta();
        closemeta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "CLOSE");
        List<String> closelore = new ArrayList<>();
        closelore.add("Click to close");
        closelore.add("the current GUI");
        closemeta.setLore(closelore);
        closemeta.setUnbreakable(true);
        close.setItemMeta(closemeta);

        return close;
    }
    public static ItemStack skip;
    public ItemStack Joker(int amount){
        ItemStack close = new ItemStack(Material.BARRIER);
        ItemMeta closemeta = close.getItemMeta();
        closemeta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "SKIP");
        close.setAmount(amount);
        List<String> closelore = new ArrayList<>();
        closelore.add("JOKER: ");
        closelore.add("Use to skip the");
        closelore.add("current Item.");
        closemeta.setLore(closelore);
        closemeta.setUnbreakable(true);
        close.setItemMeta(closemeta);
        return close;
    }

    public ItemStack WayPoints(){
        ItemStack wp = new ItemStack(Material.RECOVERY_COMPASS);
        ItemMeta wpmeta = wp.getItemMeta();
        wpmeta.setDisplayName(ChatColor.GOLD +"Server Waypoints");
        List<String> wplore = new ArrayList<>();
        wplore.add("Opens a Gui to");
        wplore.add("manage Serverwaypoints. ");
        wplore.add("Requires the plugin LionWaypoints");
        wpmeta.setLore(wplore);
        wp.setItemMeta(wpmeta);

        return wp;
    }
    public ItemStack RecipeGenerator(){
        ItemStack ct = new ItemStack(Material.CRAFTING_TABLE);
        ItemMeta ctmeta = ct.getItemMeta();
        ctmeta.setDisplayName(ChatColor.DARK_PURPLE + "Recipe generator");
        List<String> ctlore = new ArrayList<>();
        ctlore.add("Generate recipes ingame!");
        ctmeta.setLore(ctlore);
        ctmeta.setUnbreakable(true);
        ct.setItemMeta(ctmeta);
        return ct;
    }
    public ItemStack[] block(int size){
        ItemStack[] blocked = new ItemStack[size];
        ItemStack placeholder = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta plmeta = placeholder.getItemMeta();
        plmeta.setDisplayName(" ");
        plmeta.setUnbreakable(true);
        placeholder.setItemMeta(plmeta);
        for (int i = 0; i < size; i++){
            blocked[i] = placeholder;

        }
        return blocked;
    }
    public ItemStack Confirm(){
        ItemStack gw = new ItemStack(Material.GREEN_WOOL);
        ItemMeta gwmeta = gw.getItemMeta();
        gwmeta.setDisplayName(ChatColor.GREEN + "Click to confirm");
        gwmeta.setUnbreakable(true);
        gw.setItemMeta(gwmeta);
        return gw;
    }
    public ItemStack ButtonON(String name){
        ItemStack button = new ItemStack(Material.LIME_DYE);
        ItemMeta bmeta = button.getItemMeta();
        bmeta.setDisplayName(name);
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GREEN+"Currently enabled!");
        lore.add(ChatColor.GREEN+"Click here to disable.");
        bmeta.setLore(lore);
        button.setItemMeta(bmeta);
        return button;
    }
    public ItemStack ButtonOFF(String name){
        ItemStack button = new ItemStack(Material.GRAY_DYE);
        ItemMeta bmeta = button.getItemMeta();
        bmeta.setDisplayName(name);
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY+"Currently disabled!");
        lore.add(ChatColor.GRAY+"Click here to enable.");
        bmeta.setLore(lore);
        button.setItemMeta(bmeta);
        return button;
    }
    public ItemStack Timer(){
        ItemStack clock = new ItemStack(Material.CLOCK);
        ItemMeta clockmeta = clock.getItemMeta();
        clockmeta.setDisplayName(ChatColor.GOLD + "Timer");
        List<String> clocklore = new ArrayList<>();
        clocklore.add(ChatColor.AQUA + "Click to open the ");
        clocklore.add(ChatColor.AQUA + "Timer-Settings");
        clockmeta.setLore(clocklore);
        clock.setItemMeta(clockmeta);
        return clock;
    }
    public ItemStack Timerlive(){
        ItemStack timer = new ItemStack(Material.CLOCK);

        return timer;
    }

    public ItemStack Plus;
    public ItemStack PlusButton(){
        ItemStack button = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta buttonmeta = (SkullMeta) button.getItemMeta();
        buttonmeta.setPlayerProfile(Bukkit.createProfile("LionK08"));
        PlayerProfile pp = buttonmeta.getPlayerProfile();
        PlayerTextures pt = buttonmeta.getPlayerProfile().getTextures();
        try {
            pt.setSkin(new URI("https://textures.minecraft.net/texture/5d8604b9e195367f85a23d03d9dd503638fcfb05b0032535bc43734422483bde").toURL());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        pp.setTextures(pt);
        buttonmeta.setOwnerProfile(pp);
        buttonmeta.setDisplayName("plus");
        button.setItemMeta(buttonmeta);
        return button;
    }
    public ItemStack MinusButton(){
        ItemStack button = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta buttonmeta = (SkullMeta) button.getItemMeta();
        buttonmeta.setPlayerProfile(Bukkit.createProfile("LionK08"));
        PlayerProfile pp = buttonmeta.getPlayerProfile();
        PlayerTextures pt = buttonmeta.getPlayerProfile().getTextures();
        try {
            pt.setSkin(new URI("https://textures.minecraft.net/texture/482c23992a02725d9ed1bcd90fd0307c8262d87e80ce6fac8078387de18d0851").toURL());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        pp.setTextures(pt);
        buttonmeta.setOwnerProfile(pp);
        buttonmeta.setDisplayName("minus");
        button.setItemMeta(buttonmeta);
        return button;
    }
    public ItemStack counter(String displayname){
        ItemStack clock = new ItemStack(Material.CLOCK);
        ItemMeta clockmeta = clock.getItemMeta();
        clockmeta.setDisplayName(displayname);
        clock.setItemMeta(clockmeta);
        return clock;
    }
    public ItemStack nextPage(){
        ItemStack item = new ItemStack(Material.SPECTRAL_ARROW);
        ItemMeta itemmeta = item.getItemMeta();
        itemmeta.displayName(Component.text(ChatColor.GOLD + "next page"));
        item.setItemMeta(itemmeta);
        return item;
    }
}
