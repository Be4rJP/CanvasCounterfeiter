package be4rfjp.canvascounterfeiter;

import net.minecraft.server.v1_15_R1.WorldMap;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_15_R1.map.CraftMapView;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ccCommand implements CommandExecutor, TabExecutor {
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (args == null) return false;
        if (args.length != 2) return false;
    
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "このコマンドはコンソールから実行できません。");
            return true;
        }
    
        Player player = (Player) sender;
        
        switch (args[0]){
            case "create":{
                ItemStack itemStack = player.getInventory().getItemInMainHand();
                if(itemStack.getType() != Material.FILLED_MAP){
                    return false;
                }
                MapMeta mapMeta = (MapMeta) itemStack.getItemMeta();
                MapView mapView = mapMeta.getMapView();
                if(mapView == null){
                    player.sendMessage("MapView NULL");
                    return true;
                }
    
                
                try {
                    Field field = CraftMapView.class.getDeclaredField("worldMap");
                    field.setAccessible(true);
                    WorldMap worldMap = ((org.bukkit.craftbukkit.v1_15_R1.CraftWorld) player.getWorld()).getHandle().a(((WorldMap) field.get(mapView)).getId());
                    if(worldMap == null){
                        player.sendMessage("WorldMap NULL");
                        return true;
                    }
                    worldMap.scale = MapView.Scale.valueOf(args[1]).getValue();
                    worldMap.centerX = player.getLocation().getBlockX();
                    worldMap.centerZ = player.getLocation().getBlockZ();
                    worldMap.track = true;
                    
                } catch (Exception e){e.printStackTrace();}
                
                mapMeta.setDisplayName("§aCanvas");
                itemStack.setItemMeta(mapMeta);
                
                player.getInventory().setItemInMainHand(itemStack);
                player.sendMessage("§a作成しました");
                return true;
            }
            
            case "save":{
                String name = args[1];
    
                ItemStack itemStack = player.getInventory().getItemInMainHand();
                if(itemStack.getType() != Material.FILLED_MAP){
                    return false;
                }
                MapMeta mapMeta = (MapMeta) itemStack.getItemMeta();
                MapView mapView = mapMeta.getMapView();
                if(mapView == null){
                    player.sendMessage("MapView NULL");
                    return true;
                }
    
                try {
                    Field field = CraftMapView.class.getDeclaredField("worldMap");
                    field.setAccessible(true);
                    WorldMap worldMap = ((org.bukkit.craftbukkit.v1_15_R1.CraftWorld) player.getWorld()).getHandle().a(((WorldMap) field.get(mapView)).getId());
                    if(worldMap == null){
                        player.sendMessage("WorldMap NULL");
                        return true;
                    }
    
                    File file = new File("plugins/CanvasCounterfeiter/canvas", name + ".yml");
                    file.getParentFile().mkdirs();
                    YamlConfiguration yml = new YamlConfiguration();
                    yml.set("bytes", new String(worldMap.colors));
    
                    try {
                        yml.save(file);
                        player.sendMessage("§a保存しました");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
        
                } catch (Exception e){e.printStackTrace();}
                
                return true;
            }
        }
        
        
        return true;
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
    
        List<String> list = new ArrayList<>();
    
        if (args.length == 1) {
            list.add("create");
            list.add("save");
        
            return list;
        }else if(args.length == 2){
            if(args[0].equals("create")){
                for(MapView.Scale scale : MapView.Scale.values()){
                    list.add(scale.toString());
                }
                return list;
            }else if(args[0].equals("save")){
                list.add("[name]");
                return list;
            }
        }
        
        return null;
    }
}
