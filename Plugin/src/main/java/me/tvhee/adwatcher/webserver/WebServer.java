package me.tvhee.adwatcher.webserver;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import me.tvhee.adwatcher.AdWatcherPlugin;

public class WebServer
{
	private final int port;
	private HttpServer server;

	public WebServer(AdWatcherPlugin plugin)
	{
		this.port = plugin.getConfig().getInt("plugin.webserver", 0);
	}

	public int getPort()
	{
		return port;
	}

	public boolean start()
	{
		try
		{
			this.server = HttpServer.create(new InetSocketAddress(this.port), 0);
			this.server.createContext("/", new WebRequestHandler());
			this.server.setExecutor(null);
			this.server.start();
			return true;
		}
		catch(IOException exception)
		{
			if(this.server != null)
			{
				this.server.stop(0);
				return this.start();
			}
		}

		return false;
	}

	public void stop()
	{
		if(this.server != null)
			this.server.stop(0);
	}
}
