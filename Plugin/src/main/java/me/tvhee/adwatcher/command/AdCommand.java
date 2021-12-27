package me.tvhee.adwatcher.command;

import me.tvhee.adwatcher.AdWatcherPlugin;
import me.tvhee.adwatcher.reward.PendingReward;
import me.tvhee.adwatcher.reward.Reward;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdCommand implements CommandExecutor
{
	private final AdWatcherPlugin plugin;

	public AdCommand(AdWatcherPlugin plugin)
	{
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(sender instanceof Player player)
		{
			String site = plugin.getConfig().getString("plugin.ad-website", "");

			if(site.equals(""))
			{
				player.sendMessage(ChatColor.RED + "There is no website defined!");
				return true;
			}

			Reward reward = Reward.getNextReward(player);

			if(reward == null)
			{
				player.sendMessage(ChatColor.RED + "There are no more rewards available!");
				return true;
			}

			PendingReward pendingReward = new PendingReward(plugin, player, reward);

			if(!pendingReward.isRegistered())
			{
				player.sendMessage(ChatColor.RED + "Something went wrong registering your reward! Please try again");
				return true;
			}

			site = site.replaceAll("%reward-id%", String.valueOf(pendingReward.getId()));
			player.sendMessage(ChatColor.GOLD + "Warning: " + ChatColor.YELLOW + "You must be online in this server to claim your reward!");
			TextComponent websiteComponent = new TextComponent(ChatColor.GREEN + "Click" + ChatColor.GOLD + " here" + ChatColor.GREEN + " to watch your ad!");
			websiteComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, site));
			player.spigot().sendMessage(websiteComponent);
			return true;
		}
		else
		{
			sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
			return true;
		}
	}
}
