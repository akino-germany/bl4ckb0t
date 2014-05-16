package bl4ckscor3.bot.bl4ckb0tX.commands;

import java.io.IOException;

import org.pircbotx.exception.IrcException;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.output.OutputIRC;

import bl4ckscor3.bot.bl4ckb0tX.core.Core;
import bl4ckscor3.bot.bl4ckb0tX.core.Listener;
import bl4ckscor3.bot.bl4ckb0tX.util.Utilities;

public class Stop implements Command<MessageEvent>
{
	@Override
	public void exe(MessageEvent event) throws IOException, IrcException
	{
		if(Utilities.validUser(event))
		{
			boolean done = false;
			OutputIRC irc = new OutputIRC(Core.bot);
			
			if(event.getMessage().equalsIgnoreCase(event.getBot().getNick() + ", sleep!"))
			{
				Utilities.chanMsg(event, "k");
				irc.quitServer("My master sent me to sleep!");
				done = true;
			}

			if(!done)
			{
				String[] args = Utilities.toArgs(event.getMessage());

				if(args.length == 2)
				{
					switch(args[1])
					{
						case "yes":
							Utilities.chanMsg(event, "I will reboot, sir");
							irc.quitServer("My master sent me to sleep!");
							Core.main2(); //making another PircBotX
							break;
						case "no":
							Utilities.chanMsg(event, "You wished that I don't reboot. Do you still like me?");
							irc.quitServer("My master sent me to sleep!");
							Listener.stopped = true;
							break;
						default:
							Utilities.userMsg(event, "Should I reboot? I cannot disconnect if I don't know that :(");
					}
				}
				else
				{
					switch(args.length)
					{
						case 1:
							Utilities.respond(event, "please tell me if I should reboot. Example: -stop no", true);
							break;
						case 3:
							Utilities.respond(event, "please only tell me if I should reboot and nothing else. Example: -stop no", true);
					}
				}
			}
		}
		else
			Utilities.sorry(event);
	}
	
	@Override
	public String getAlias()
	{
		return "stop";
	}
}