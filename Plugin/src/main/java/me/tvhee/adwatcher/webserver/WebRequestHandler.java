package me.tvhee.adwatcher.webserver;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import me.tvhee.adwatcher.reward.PendingReward;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class WebRequestHandler implements HttpHandler
{
	@Override
	public void handle(final HttpExchange httpExchange)
	{
		String method = httpExchange.getRequestMethod();
		String url = httpExchange.getRequestURI().toString();

		if(method.equals("GET"))
		{
			if(url.endsWith("/online"))
			{
				sendData(httpExchange, true);
			}
			else if(url.contains("/ad-information/"))
			{
				int id = Integer.parseInt(url.split("/ad-information/")[1]);
				PendingReward pendingReward = PendingReward.getReward(id);

				if(pendingReward == null)
				{
					sendData(httpExchange, "null");
					return;
				}

				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("ad-id", pendingReward.getId());
				jsonObject.addProperty("reward", pendingReward.getReward().getName());
				jsonObject.addProperty("player", pendingReward.getPlayer().toString());
				sendData(httpExchange, jsonObject);
			}
			else
				sendError(httpExchange);
		}
		else if(method.equals("POST"))
		{
			if(url.contains("/ad-cancelled/"))
			{
				String[] content = url.split("/ad-cancelled/")[1].split("/");
				PendingReward pendingReward = PendingReward.getReward(Integer.parseInt(content[0]));
				String message = content[1];

				if(pendingReward == null)
					return;

				pendingReward.cancel();
				Player player = Bukkit.getPlayer(pendingReward.getPlayer());

				if(player == null || !player.isOnline())
					return;

				player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
			}
			else if(url.contains("/ad-finished/"))
			{
				String[] content = url.split("/ad-finished/")[1].split("/");
				PendingReward pendingReward = PendingReward.getReward(Integer.parseInt(content[0]));

				if(pendingReward == null)
					return;

				pendingReward.giveReward();
			}
		}
	}

	private void sendError(HttpExchange httpExchange)
	{
		sendData(404, httpExchange, "");
	}

	private void sendData(HttpExchange httpExchange, Object object)
	{
		sendData(httpExchange, object.toString());
	}

	private void sendData(HttpExchange httpExchange, String content)
	{
		sendData(200, httpExchange, content);
	}

	private void sendData(int httpCode, HttpExchange httpExchange, String content)
	{
		try
		{
			httpExchange.sendResponseHeaders(httpCode, content.getBytes().length);
			OutputStream outputStream = new BufferedOutputStream(httpExchange.getResponseBody(), 1024);
			outputStream.write(content.getBytes());
			outputStream.flush();
			outputStream.close();
		}
		catch(IOException ignored)
		{

		}
	}
}
