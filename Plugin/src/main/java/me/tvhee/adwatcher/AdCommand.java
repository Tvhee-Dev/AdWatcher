package me.tvhee.adwatcher;

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
			String site = plugin.getConfig().getString("plugin.ad-website", "").replaceAll("%uuid%", player.getUniqueId().toString()).replaceAll("%name%", player.getName());

			if(site.equals(""))
			{
				player.sendMessage(ChatColor.RED + "There is not website defined!");
				return true;
			}

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
