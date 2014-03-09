package bl4ckb0tX.commands.normal;

import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.output.OutputIRC;

import bl4ckb0tX.core.Core;
import bl4ckb0tX.util.Stuffz;

public class ChangeNick
{
	@SuppressWarnings("rawtypes")
	public static void exe(MessageEvent event)
	{
		String seperated = Stuffz.getMessage(event).substring(11);
		String nick = seperated.substring(1);
		
		if(Stuffz.validUser(event))
		{
			OutputIRC irc = new OutputIRC(Core.bot);
			
			irc.changeNick(nick);
		}
		else
		{
			Stuffz.sorry(event);
		}
	}
}
