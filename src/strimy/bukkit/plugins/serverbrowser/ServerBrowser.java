package strimy.bukkit.plugins.serverbrowser;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.PluginCommand;

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
		pm.registerEvents(playerListener,this);
		
		PluginCommand sbCommand = getCommand("sb");
		if(sbCommand == null)
		{
			log.info(ChatColor.RED + "Command are disabled");
		}
		else
		{
			log.info(ChatColor.GOLD + "["+getDescription().getName()+"] Command /sb enabled to configure the plugin (op only)");
			sbCommand.setExecutor(new SBCommandExecutor(this));
		}
	}

}
