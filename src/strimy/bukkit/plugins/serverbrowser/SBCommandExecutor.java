package strimy.bukkit.plugins.serverbrowser;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SBCommandExecutor implements CommandExecutor 
{
	ServerBrowser plugin = null;
	
	public SBCommandExecutor(ServerBrowser plugin)
	{
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandStr, String[] args) 
	{
		boolean returnVal = false;
		String returnMessage = "Unknown error...";
		if(!sender.isOp())
		{
			returnMessage = "You are not Op";
			returnVal = true;
		}
		
		if(args.length > 0)
		{
			if(args[0].equals("delay"))
			{
				if(args.length == 1)
				{
					returnMessage = "Command usage is : /sb delay {notification_delay} (in seconds)";
					returnVal = true;
				}
				else
				{
					try
					{
						plugin.config.setNotifDelay(Integer.parseInt(args[1]));
						plugin.config.savePluginConfiguration();
						returnMessage = ChatColor.GOLD + "["+plugin.getDescription().getName()+"] Notification delay changed to " + args[1];
						returnVal = true;
					}
					catch(NumberFormatException e)
					{
						returnMessage = ChatColor.RED + "The value entered is not a number";
						returnVal = true;
					}
				}
			}
			else if(args[0].equals("verbose"))
			{
				if(args.length == 1)
				{
					sender.sendMessage("Command usage is : /sb verbose true|false");
					
					return true;
				}
				else
				{
					try
					{
						plugin.config.setVerbose(Boolean.parseBoolean(args[1]));
						plugin.config.savePluginConfiguration();
						returnMessage = ChatColor.GOLD + "["+plugin.getDescription().getName()+"] Verbose changed to "+plugin.config.isVerbose();
						returnVal = true;
					}
					catch(NumberFormatException e)
					{
						returnMessage = ChatColor.RED + "["+plugin.getDescription().getName()+"]The value entered is not a boolean";
						returnVal = true;
					}
				}
			}
		}
		sender.sendMessage(returnMessage);
		return returnVal;
	}

}
