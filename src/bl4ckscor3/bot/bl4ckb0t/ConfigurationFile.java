package bl4ckscor3.bot.bl4ckb0t;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;

import bl4ckscor3.bot.bl4ckb0t.logging.Logging;
import bl4ckscor3.bot.bl4ckb0t.util.Utilities;
import bl4ckscor3.bot.bl4ckb0t.util.android.ArrayMap;

/**
 * Use the -update command to let a new configuration take place
 */
public class ConfigurationFile
{
	private File config;
	public static ArrayMap<String,String> values = new ArrayMap<String,String>();
	private String[] defaultContent = new String[]{
			"################################",
			"#       Channel Commands       #",
			"################################",
			"caps=true",
			"changelog=true",
			"changenick=true",
			"decide=true",
			"disable=true",
			"enable=true",
			"evaluate=true",
			"forge=true",
			"help=true",
			"info=true",
			"join=true",
			"kick=true",
			"language=true",
			"leave=true",
			"leet=true",
			"listchans=true",
			"longurl=true",
			"mcstatus=true",
			"ping=true",
			"remind=true",
			"select=true",
			"source=true",
			"stop=true",
			"trello=true",
			"tv=true",
			"tw=true",
			"unleet=true",
			"update=true",
			"w=true",
			"xcolor=true",
			"yt=true",
			"",
			"################################",
			"#       Private Commands       #",
			"################################",
			"action=true",
			"chanmsg=true",
			"privatejoin=true",
			"privateleave=true",
			"privatestop=true",
			"usermsg=true",
			"",
			"################################",
			"#            Other             #",
			"################################",
			"showCmdColorsInHelpMenu=true",
			"allowBlacklistWebpages=true",
			"allowIgnoringUsers=true",
			"allowCommandAliases=true",
			"showGitHubRepoInfo=true",
			"showTweets=true",
			"showLinkTitles=true",
			"showYouTubeStats=true",
			"showGitHubCommitInfo=true",
			"showWelcomeBackMsg=true",
			"shrugs=true"
	};

	/**
	 * Sets up the configuration file, if not already done
	 */
	public ConfigurationFile()
	{
		try
		{
			Logging.info("Checking for custom config file...");
			config = new File(Utilities.getJarLocation() + "/config.txt");

			if(!config.exists())
			{
				Logging.info("Custom config file doesn't exist, creating one...");
				config.createNewFile();
				writeDefaultValues();
				populateArrayMap();
				Logging.info("Config values successfully written into config and memory...");
			}
			else
			{
				Logging.info("Custom config file already existed...");
				populateArrayMap();
				Logging.info("Config values successfully written into memory, checking for new values to add...");

				int i = 0;
				List<String> currentContent = FileUtils.readLines(config);
				List<String> newContent = new ArrayList<String>();
				List<String> restoredContent = new ArrayList<String>();

				for(String s : defaultContent)
				{
					if(i >= currentContent.size()) //if the line is null, just add the new line
						newContent.add(s);
					else if(currentContent.get(i).split("=").length != 0) //if it's a config value
					{
						if(s.startsWith(currentContent.get(i).split("=")[0]) && (!currentContent.get(i).equals("") || s.equals(""))) //if the config values are equal add current one to keep settings
							newContent.add(currentContent.get(i));
						else //if not, add the new default value
							newContent.add(s);
					}
					else
						newContent.add(s);

					i++;
				}

				i = 0;

				//to prevent a ConcurrentModificationException
				for(String s : newContent)
				{
					restoredContent.add(s);
				}

				//checking for anything that got reset
				for(String s : newContent)
				{
					if(s.split("=").length != 0 && values.containsKey(s.split("=")[0]))
					{
						if(((String)values.get(s.split("=")[0])).equals(s.split("=")[1]))
						{
							i++;
							continue;
						}
						else
						{
							restoredContent.remove(i);
							restoredContent.add(i, s.split("=")[0] + "=" + (String)values.get(s.split("=")[0]));
						}
					}

					i++;
				}

				Logging.info("Restored values that got reset during any possible config additions...");
				clear();
				FileUtils.writeLines(config, restoredContent);
				populateArrayMap();
				Logging.info("Configuration setup complete!");
			}
		}
		catch(URISyntaxException | IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Gets the value from the given option as a boolean
	 * @param option The option to get the value from
	 * @return The value
	 */
	public boolean getBoolean(String option)
	{
		return Boolean.parseBoolean(values.get(option));
	}

	/**
	 * Gets the value from the given option as a string
	 * @param option The option to get the value from
	 * @return The value
	 */
	public String getString(String option)
	{
		return values.get(option).split("'")[1];
	}
	
	/**
	 * Gets the value from the given option as an integer
	 * @param option The option to get the value from
	 * @return The value
	 */
	public int getInt(String option)
	{
		return Integer.parseInt(values.get(option));
	}
	
	/**
	 * Writes the default values to the file
	 */
	public void writeDefaultValues()
	{
		try
		{
			FileUtils.writeLines(config, Arrays.asList(defaultContent));
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Writes the current values into the 'values' HashMap
	 */
	public void populateArrayMap()
	{		
		try
		{
			List<String> content = FileUtils.readLines(config);

			values.clear();

			for(String s : content)
			{
				if(s.contains("="))
					values.put(s.split("=")[0], s.split("=")[1]);
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Clears the file
	 */
	public void clear()
	{
		try
		{
			RandomAccessFile raf = new RandomAccessFile(config, "rw");

			raf.setLength(0);
			raf.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}
