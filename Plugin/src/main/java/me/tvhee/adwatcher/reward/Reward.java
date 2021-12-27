package me.tvhee.adwatcher.reward;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import me.tvhee.adwatcher.AdWatcherPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Reward
{
	private static final Map<String, Reward> rewardsByName = new HashMap<>();
	private final AdWatcherPlugin plugin;
	private final String name;
	private final String displayName;
	private final String command;
	private final List<UUID> received;

	public Reward(AdWatcherPlugin plugin, String name, String displayName, String command, List<UUID> received)
	{
		this.plugin = plugin;
		this.name = name;
		this.displayName = displayName;
		this.command = command;
		this.received = received;
		rewardsByName.put(name, this);
	}

	public static Reward getNextReward(Player player)
	{
		return getNextReward(player.getUniqueId());
	}

	public static Reward getNextReward(UUID uuid)
	{
		for(Reward reward : rewardsByName.values())
		{
			if(!reward.hasReceived(uuid))
				return reward;
		}

		return null;
	}

	public String getName()
	{
		return name;
	}

	public String getDisplayName()
	{
		return displayName;
	}

	public boolean hasReceived(Player player)
	{
		return hasReceived(player.getUniqueId());
	}

	public boolean hasReceived(UUID player)
	{
		return received.contains(player);
	}

	public void give(Player player)
	{
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replaceAll("%player%", player.getName()));
		received.add(player.getUniqueId());

		List<String> rewardsReceived = plugin.getConfig().getStringList("storage.rewards-given." + player.getUniqueId());
		rewardsReceived.add(this.name);
		plugin.getConfig().set("storage.rewards-given." + player.getUniqueId(), rewardsReceived);
		plugin.saveConfig();

		player.sendMessage(ChatColor.GREEN + "You have successfully received reward " + ChatColor.GOLD + this.displayName + ChatColor.GREEN + "!");
	}
}
