package me.tvhee.adwatcher;

import org.bukkit.plugin.java.JavaPlugin;

public class AdWatcherPlugin extends JavaPlugin
{
	@Override
	public void onEnable()
	{
		saveDefaultConfig();

		getServer().getPluginCommand("ad").setExecutor(new AdCommand(this));

		getLogger().info("has been enabled!");
		getLogger().info("made by Tvhee!");
	}

	@Override
	public void onDisable()
	{
		getLogger().info("has been disabled!");
		getLogger().info("made by Tvhee!");
	}
}
