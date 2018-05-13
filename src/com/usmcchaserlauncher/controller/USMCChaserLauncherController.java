package com.usmcchaserlauncher.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.swing.filechooser.FileSystemView;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.usmcchaserlauncher.model.ServerDetails;
import com.usmcchaserlauncher.model.ServerQueryResponse;
import com.usmcchaserlauncher.model.VersionInfoResponse;

public class USMCChaserLauncherController
{
	private final static Logger logger = Logger.getLogger(USMCChaserLauncherController.class);
	private static final String CONFIG_FILE_NAME = "config.properties";
	private static Properties props;
	
	
	public Properties loadAppProperties() throws IOException
	{
		if(props!= null)
			return props;
		
		props = new Properties();
		InputStream input = null;

		try 
		{
			// if file already exists will do nothing 
			new File(CONFIG_FILE_NAME).createNewFile(); 
			input = new FileInputStream(CONFIG_FILE_NAME);
			props.load(input);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					logger.error("Unable to close config.properties", e);
				}
			}
		}
		return props;
	}
	
	public void setAppProperty(String key, String value) throws IOException
	{
		
		if(props == null)
			loadAppProperties();
		
		InputStream input = null;
		try 
		{
			props.setProperty(key, value);
			props.store(new FileOutputStream(CONFIG_FILE_NAME), null);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					logger.error("Unable to close config.properties", e);
				}
			}
		}
	}
	
	public String getAppProperty(String key) throws IOException
	{
		if(props == null)
			loadAppProperties();
		
		InputStream input = null;
		try 
		{
			return props.getProperty(key);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					logger.error("Unable to close config.properties", e);
				}
			}
		}
	}
	
	public List<String> getLocalRootDriveNames()
	{
		File[] paths = File.listRoots();
		FileSystemView fsv = FileSystemView.getFileSystemView();

		List<String> localRootDriveNames = new ArrayList<String>();
		for(File path: paths)
		{
			if(fsv.getSystemTypeDescription(path).equalsIgnoreCase("Local Disk"))
				localRootDriveNames.add(path.toString());
		} 
		return localRootDriveNames;
	}
	
	public String findExecutable(List<String> endPaths)
	{
		List<String> drives = getLocalRootDriveNames();
		for(String endPath : endPaths)
		{
			for(String drive : drives)
			{
				String commonPath = drive + endPath;
				File f = new File(commonPath);
				if (f.exists()) {
				    return commonPath;
				}
			}
		}
		return null;
	}
	
	public List<ServerDetails> loadChaserServers(String endpointURL) throws Exception
	{
		for(int i = 0; i < 3; i++)
		{
			HttpURLConnection conn = null;
			BufferedReader br = null;
			try
			{
				URL url = new URL(endpointURL);
				conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				conn.setRequestProperty("Accept", "application/json");
	
				br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
	
				String output;
				StringBuilder sb = new StringBuilder();
				while ((output = br.readLine()) != null)
				{
					sb.append(output);
				}
				String jsonStr = sb.toString();
				ObjectMapper mapper = new ObjectMapper();
				ServerQueryResponse serverQueryResponse = mapper.readValue(jsonStr, ServerQueryResponse.class);
				return serverQueryResponse.getServers();
			}catch(Exception ex){
				if(i == 2){
					throw ex;
				}
			}finally{
				if(conn!= null)
					conn.disconnect();
				if (br != null)
					br.close();
			}
		}
		throw new Exception("Unable to load Chaser servers. Please check your internet connection.");
	}
	
	public VersionInfoResponse getVersionInfo(String endpointURL) throws Exception
	{
		for(int i = 0; i < 3; i++)
		{
			HttpURLConnection conn = null;
			BufferedReader br = null;
			try
			{
				URL url = new URL(endpointURL);
				conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				conn.setRequestProperty("Accept", "application/json");
				br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
	
				String output;
				StringBuilder sb = new StringBuilder();
				while ((output = br.readLine()) != null)
				{
					sb.append(output);
				}
				
				String jsonStr = sb.toString();
				ObjectMapper mapper = new ObjectMapper();
				VersionInfoResponse versionInfo = mapper.readValue(jsonStr, VersionInfoResponse.class);
				return versionInfo;
	
			}catch(Exception ex){
				if(i == 2)
					throw ex;
			}finally
			{
				if(conn!= null)
					conn.disconnect();
				if (br != null)
					br.close();
			}
		}
		throw new Exception("Unable to get application version information. Please check your internet connection.");
	}
}
