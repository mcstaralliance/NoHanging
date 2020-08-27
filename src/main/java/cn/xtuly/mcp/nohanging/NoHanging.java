package cn.xtuly.mcp.nohanging;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.hanging.HangingEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class NoHanging extends JavaPlugin implements Listener {
    private static FileConfiguration config;

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getPluginManager().registerEvents(this, this);

        config = this.getConfig();
        initDefaultConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


    public void initDefaultConfig() {
        //初始化默认设置
        config.options().copyDefaults(true);
        config.addDefault("Option.items", "14,15,16,21,56,73,127,153,256,291,292,308,809,832,833,359,1058,1496,1504,1931");
        config.addDefault("Option.debug", false);
        this.saveConfig();
    }

    @EventHandler
    public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent e){
        getLogger().log(Level.ALL, "onPlayerInteractEntityEvent");
        if (e.getPlayer().isOp() && config.getBoolean("Option.debug")) {
            e.getPlayer().sendMessage("-------------------------------------------");
            e.getPlayer().sendMessage("Name:" + e.getRightClicked().getName());
            if(e.getRightClicked() instanceof ItemFrame){
                ItemFrame itemFrame = (ItemFrame)e.getRightClicked();
                e.getPlayer().sendMessage("ItemName:" + itemFrame.getItem().getType().name());
            }
            e.getPlayer().sendMessage("-------------------------------------------");
        }

        if(e.getRightClicked() instanceof ItemFrame){
            ItemFrame itemFrame = (ItemFrame)e.getRightClicked();
            if(config.getString("Option.items").contains(String.valueOf(itemFrame.getItem().getTypeId()))){
                itemFrame.setItem(new ItemStack(Material.AIR));
                getLogger().log(Level.ALL, "Clean: " + e.getPlayer().getName() + " Item:" + itemFrame.getItem().getTypeId());
            }else if(config.getString("Option.items").contains(String.valueOf(e.getPlayer().getInventory().getItemInMainHand().getTypeId()))){
                e.setCancelled(true);
            }
        }
    }
}
