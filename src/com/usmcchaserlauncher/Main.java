package com.usmcchaserlauncher;

import java.awt.EventQueue;
import java.io.InputStream;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.usmcchaserlauncher.view.LauncherView;

public class Main
{
	private final static Logger logger = Logger.getLogger(Main.class);

	public static void main(final String[] args)
	{
	    InputStream res = Main.class.getResourceAsStream("/log4j.properties");
		PropertyConfigurator.configure(res);
		
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					LauncherView window = new LauncherView(args);
					window.setVisible();
				} catch (Exception e)
				{
					logger.error("Error starting application.", e);
				}
			}
		});
	}

	
}
