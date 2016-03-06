package bl4ckscor3.bot.bl4ckb0t.misc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.pircbotx.Colors;
import org.pircbotx.hooks.events.MessageEvent;

import bl4ckscor3.bot.bl4ckb0t.Core;
import bl4ckscor3.bot.bl4ckb0t.localization.L10N;
import bl4ckscor3.bot.bl4ckb0t.logging.Logging;
import bl4ckscor3.bot.bl4ckb0t.util.Utilities;

public class LinkTitle
{
	public static void checkForLinkAndSendTitle(MessageEvent event) throws MalformedURLException, IOException
	{
		String[] args = Utilities.toArgs(event.getMessage());

		for(String s : args)
		{
			s = Colors.removeFormattingAndColors(s);

			if(s.contains("www.") || s.contains("http://") || s.contains("https://"))
			{
				if(Core.bot.getConfig().isEnabled("allowBlacklistWebpages") && isWebsiteBlacklisted(s))
				{
					Logging.info("Website blacklisted: " + s);
					continue;
				}

				if(Core.bot.getConfig().isEnabled("showTweets") && s.contains("twitter"))
					ShowTweet.show(event, s);
				else if(Core.bot.getConfig().isEnabled("showGitHubCommitInfo") && (s.contains("git.io") || (s.contains("github.com") && s.contains("commit"))))
					GitHub.showCommit(event, s);
				else if(Core.bot.getConfig().isEnabled("showGitHubRepoInfo") && s.contains("github.com"))
					GitHub.showRepo(event, s);
				else if(Core.bot.getConfig().isEnabled("kickOnBannedImgurLink") && event.getChannel().getName().equals("#bl4ckscor3") && (s.contains("imgur") && !s.contains("i.imgur") && !s.contains("gallery")))
					Core.bot.kick(event.getChannel().getName(), event.getUser().getNick(), "Only use i.imgur.com links.");
				else if(Core.bot.getConfig().isEnabled("showLinkTitles"))
				{
					WebDriver driver = new HtmlUnitDriver();
					String title = "";

					if(s.startsWith("www."))
						s = "http://" + s;

					driver.get(s);
					title = driver.getTitle();
					driver.close();
					
					if(s.startsWith("http://"))
						s = s.substring(7);
					else if(s.startsWith("https://"))
						s = s.substring(8);

					if(s.length() > 21)
					{
						s = s.substring(0, 21);
						s += "...";
					}

					if(title == null || title == "null" || title == "")
						Utilities.chanMsg(event, L10N.getString("linkTitle.notFound", event).replace("#link", s));
					else
						Utilities.chanMsg(event, L10N.getString("linkTitle.available", event).replace("#link", s).replace("#title", title));
				}
			}
		}
	}

	private static boolean isWebsiteBlacklisted(String website) throws MalformedURLException, IOException
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(new URL("https://www.dropbox.com/s/x95z98frbt1r2ne/blacklistedWebsites.txt?dl=1").openStream()));
		List<String> blacklistedWebsites = new ArrayList<String>();
		String line = "";

		while((line = reader.readLine()) != null)
		{
			blacklistedWebsites.add(line);
		}

		reader.close();
		
		for(String s : blacklistedWebsites)
		{
			if(website.contains(s))
				return true;
		}

		return false;
	}
}
