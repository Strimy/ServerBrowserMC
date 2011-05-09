package strimy.bukkit.plugins.serverbrowser;

import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;

public class SBPlayerListener extends PlayerListener 
{
	ServerBrowser plugin;
	public SBPlayerListener(ServerBrowser plugin) 
	{
		this.plugin = plugin;
	}
	
	@Override
	public void onPlayerJoin(PlayerJoinEvent event) 
	{
		plugin.sender.sendInfos(null);
		super.onPlayerJoin(event);
	}
	
	@Override
	public void onPlayerQuit(PlayerQuitEvent event) 
	{
		plugin.sender.sendInfos(event.getPlayer());
		super.onPlayerQuit(event);
	}
}
