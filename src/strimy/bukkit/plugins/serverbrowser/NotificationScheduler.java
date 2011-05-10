package strimy.bukkit.plugins.serverbrowser;
import java.util.TimerTask;


public class NotificationScheduler extends TimerTask 
{
	ServerSender sender;
	public NotificationScheduler(ServerSender sender) 
	{
		this.sender = sender;
	}
	@Override
	public void run() {
		sender.sendInfos(null);

	}

}
