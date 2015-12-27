package bl4ckscor3.bot.bl4ckb0t.commands.channel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.pircbotx.hooks.events.MessageEvent;

import bl4ckscor3.bot.bl4ckb0t.core.Bot;
import bl4ckscor3.bot.bl4ckb0t.exception.IncorrectCommandExecutionException;
import bl4ckscor3.bot.bl4ckb0t.localization.L10N;
import bl4ckscor3.bot.bl4ckb0t.logging.Logging;
import bl4ckscor3.bot.bl4ckb0t.util.Passwords;
import bl4ckscor3.bot.bl4ckb0t.util.Utilities;

public class Calculate extends BaseCommand<MessageEvent<Bot>>
{
	@Override
	public void exe(MessageEvent<Bot> event) throws IncorrectCommandExecutionException, IOException
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(new URL("http://api.wolframalpha.com/v2/query?input=" + event.getMessage().split("-calc ")[1].replace("+", "plus").replace(" ", "") + "&appid=" + Passwords.WOLFRAMAPIKEY.getPassword()).openStream()));
		String line = "";
		
		try
		{
			//skipping lines until wanted line is reached
			while(!((line = reader.readLine()).contains("title='Result'")))
			{
				if(line.contains("Appid missing"))
				{
					Utilities.chanMsg(event, "appid: " + L10N.getString("calc.fail", event));
					Logging.severe("Appid of WolframAlpha is missing. Something went horribly wrong.");
					return;
				}
			}
		}
		catch(NullPointerException e)
		{
			Utilities.chanMsg(event, "title: " + L10N.getString("calc.fail", event));
			Logging.warn("-calc: Result line could not be found.");
			reader.close();
			return;
		}
		
		try
		{
			//skipping lines then saving the wanted line into the string
			while(!((line = reader.readLine()).contains("plaintext"))){}
		}
		catch(NullPointerException e)
		{
			Utilities.chanMsg(event, "plaintext: " + L10N.getString("calc.fail", event));
			Logging.warn("-calc: Actual result not found.");
			reader.close();
			return;
		}
		
		reader.close();
		Utilities.chanMsg(event, line.split(">")[1].split("<")[0]);
	}

	@Override
	public String getAlias()
	{
		return "calc";
	}

	@Override
	public String getSyntax(MessageEvent<Bot> event) 
	{
		return "-calc <" + L10N.getString("calc.calculation", event) + ">";
	}

	@Override
	public String[] getUsage(MessageEvent<Bot> event) 
	{
		return new String[]{"-calc <" + L10N.getString("calc.calculation", event) + "> || " + L10N.getString("calc.explanation", event).replace("#link", "http://wolframalpha.com/")};
	}
}
