package strimy.bukkit.plugins.serverbrowser;

import java.util.logging.Logger;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;

public class ServerBrowser extends JavaPlugin 
{
	Logger log;
	ServerSender sender;
	ServerConfiguration config;
	
	@Override
	public void onDisable() {
		// TODO Auto-generated method stub
		log.info("ServerBrowser unloaded");
		sender.unload();
	}

	@Override
	public void onEnable() 
	{
		log = getServer().getLogger();
		
		log.info("["+getDescription().getName()+" "+ getDescription().getVersion()+"] Plugin loaded !");
		log.info("["+getDescription().getName()+" "+ getDescription().getVersion()+"] This server will send a notification each time a player join or quit !");
		
		config = new ServerConfiguration(this);
		sender = new ServerSender(this, config);
		SBPlayerListener playerListener = new SBPlayerListener(this);
		
		
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Event.Type.PLAYER_JOIN, playerListener, Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_QUIT, playerListener, Priority.Normal, this);
		

	}

}
