package dev.proflare.snowflakecannon;

import org.bukkit.plugin.java.JavaPlugin;

public class SnowflakeCannon extends JavaPlugin {

    private static SnowflakeCannon instance;

    @Override
    public void onEnable() {
        instance = this;
        getServer().getPluginManager().registerEvents(new SnowflakeListener(this), this);
        getCommand("give-snowflake").setExecutor(new GiveCommand());
    }

    @Override
    public void onDisable() {}

    public static SnowflakeCannon getInstance() {
        return instance;
    }
}
