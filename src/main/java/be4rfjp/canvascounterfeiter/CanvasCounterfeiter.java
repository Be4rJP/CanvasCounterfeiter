package be4rfjp.canvascounterfeiter;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class CanvasCounterfeiter extends JavaPlugin {
    
    @Override
    public void onEnable() {
        // Plugin startup logic
        Objects.requireNonNull(getCommand("cc")).setExecutor(new ccCommand());
        Objects.requireNonNull(getCommand("cc")).setTabCompleter(new ccCommand());
    }
    
    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
