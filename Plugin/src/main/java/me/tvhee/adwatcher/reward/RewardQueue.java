package me.tvhee.adwatcher.reward;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class RewardQueue implements Listener
{
	private final Map<UUID, Reward> queuedRewards = new HashMap<>();

	public void queueReward(PendingReward pendingReward)
	{
		queuedRewards.put(pendingReward.getPlayer(), pendingReward.getReward());
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e)
	{
		Player player = e.getPlayer();

		if(queuedRewards.containsKey(player.getUniqueId()))
		{
			Reward reward = queuedRewards.get(player.getUniqueId());
			reward.give(player);
			queuedRewards.remove(player.getUniqueId());
		}
	}
}
