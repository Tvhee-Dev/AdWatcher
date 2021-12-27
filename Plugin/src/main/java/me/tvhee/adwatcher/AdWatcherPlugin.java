package me.tvhee.adwatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import me.tvhee.adwatcher.command.AdCommand;
import me.tvhee.adwatcher.reward.Reward;
import me.tvhee.adwatcher.reward.RewardQueue;
import me.tvhee.adwatcher.webserver.WebServer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

public class AdWatcherPlugin extends JavaPlugin
{
	private WebServer webServer;
	private RewardQueue rewardQueue;

	@Override
	public void onEnable()
	{
		saveDefaultConfig();

		ConfigurationSection rewardsSection = getConfig().getConfigurationSection("plugin.rewards");

		if(rewardsSection != null)
		{
			for(String rewardName : rewardsSection.getKeys(false))
			{
				String inGameName = getConfig().getString("plugin.rewards." + rewardName + ".displayname");
				String command = getConfig().getString("plugin.rewards." + rewardName + ".command");

				List<UUID> alreadyReceived = new ArrayList<>();

				ConfigurationSection alreadyReceivedSection = getConfig().getConfigurationSection("storage.rewards-given");

				if(alreadyReceivedSection != null)
				{
					for(String uuid : alreadyReceivedSection.getKeys(false))
					{
						List<String> rewardsReceived = getConfig().getStringList("storage.rewards-given." + uuid);

						if(rewardsReceived.contains(rewardName))
							alreadyReceived.add(UUID.fromString(uuid));
					}
				}

				new Reward(this, rewardName, inGameName, command, alreadyReceived);
			}
		}

		this.webServer = new WebServer(this);
		this.rewardQueue = new RewardQueue();

		if(!webServer.start())
		{
			getLogger().log(new LogRecord(Level.SEVERE, "Could not start webserver on port " + webServer.getPort() + "!"));
			getLogger().log(new LogRecord(Level.SEVERE, "Disabling plugin!"));
			getServer().getPluginManager().disablePlugin(this);
			return;
		}

		getServer().getPluginManager().registerEvents(rewardQueue, this);
		getServer().getPluginCommand("ad").setExecutor(new AdCommand(this));

		getLogger().info("has been enabled!");
		getLogger().info("made by Tvhee!");
	}

	public RewardQueue getRewardQueue()
	{
		return rewardQueue;
	}

	@Override
	public void onDisable()
	{
		this.webServer.stop();

		getLogger().info("has been disabled!");
		getLogger().info("made by Tvhee!");
	}
}
