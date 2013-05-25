package strimy.bukkit.plugins.serverbrowser;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class SBPlayerListener implements Listener 
{
	ServerBrowser plugin;
	public SBPlayerListener(ServerBrowser plugin) 
	{
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) 
	{
		plugin.sender.sendInfos(null);
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) 
	{
		plugin.sender.sendInfos(event.getPlayer());
	}
}
