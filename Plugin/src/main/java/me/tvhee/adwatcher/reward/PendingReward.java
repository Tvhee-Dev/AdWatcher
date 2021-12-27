package me.tvhee.adwatcher.reward;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import me.tvhee.adwatcher.AdWatcherPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PendingReward
{
	private static final Map<Integer, PendingReward> byId = new HashMap<>();
	private final AdWatcherPlugin plugin;
	private final int id;
	private final UUID player;
	private final Reward reward;

	public PendingReward(AdWatcherPlugin plugin, Player player, Reward reward)
	{
		this(plugin, getRandomWithExclusion(), player, reward);
	}

	public PendingReward(AdWatcherPlugin plugin, int id, Player player, Reward reward)
	{
		this.plugin = plugin;
		this.id = id;
		this.player = player.getUniqueId();
		this.reward = reward;
		byId.put(id, this);
	}

	public static PendingReward getReward(int id)
	{
		return byId.get(id);
	}

	public boolean isRegistered()
	{
		return byId.containsKey(id);
	}

	public int getId()
	{
		return id;
	}

	public UUID getPlayer()
	{
		return player;
	}

	public Reward getReward()
	{
		return reward;
	}

	public void cancel()
	{
		byId.remove(id);
	}

	public void giveReward()
	{
		Player player = Bukkit.getPlayer(this.player);

		if(player == null || !player.isOnline())
			plugin.getRewardQueue().queueReward(this);
		else
			this.reward.give(player);

		cancel();
	}

	private static int getRandomWithExclusion()
	{
		Set<Integer> exclude = byId.keySet();

		int random = 1 + new Random().nextInt(999999 - 1 + 1 - exclude.size());

		for(int ex : exclude)
		{
			if(random < ex)
				break;

			random++;
		}

		return random;
	}
}
