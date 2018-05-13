package com.usmcchaserlauncher.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.usmcchaserlauncher.model.serializers.NumericBooleanDeserializer;
import com.usmcchaserlauncher.model.serializers.NumericBooleanSerializer;

public class ServerDetails
{
	@JsonProperty("ip") 
	private String ip; 
	@JsonProperty("port") 
	private Integer port; 
	@JsonProperty("hostname") 
	private String hostname;
	@JsonProperty("gameVersion") 
	private String gameVersion; 
	@JsonProperty("map") 
	private String map; 
	@JsonProperty("gameType") 
	private String gameType; 
	@JsonProperty("playerCount") 
	private Integer playerCount;
	@JsonProperty("maxPlayerCount") 
	private Integer maxPlayerCount;
	@JsonProperty("password")
    @JsonSerialize(using=NumericBooleanSerializer.class)
    @JsonDeserialize(using=NumericBooleanDeserializer.class)
	private Boolean password;
	@JsonProperty("game") 
	private String game;
	
	public ServerDetails()
	{
		//Used by JACKSON
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((game == null) ? 0 : game.hashCode());
		result = prime * result + ((gameType == null) ? 0 : gameType.hashCode());
		result = prime * result + ((gameVersion == null) ? 0 : gameVersion.hashCode());
		result = prime * result + ((hostname == null) ? 0 : hostname.hashCode());
		result = prime * result + ((ip == null) ? 0 : ip.hashCode());
		result = prime * result + ((map == null) ? 0 : map.hashCode());
		result = prime * result + ((maxPlayerCount == null) ? 0 : maxPlayerCount.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((playerCount == null) ? 0 : playerCount.hashCode());
		result = prime * result + ((port == null) ? 0 : port.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ServerDetails other = (ServerDetails) obj;
		if (game == null)
		{
			if (other.game != null)
				return false;
		} else if (!game.equals(other.game))
			return false;
		if (gameType == null)
		{
			if (other.gameType != null)
				return false;
		} else if (!gameType.equals(other.gameType))
			return false;
		if (gameVersion == null)
		{
			if (other.gameVersion != null)
				return false;
		} else if (!gameVersion.equals(other.gameVersion))
			return false;
		if (hostname == null)
		{
			if (other.hostname != null)
				return false;
		} else if (!hostname.equals(other.hostname))
			return false;
		if (ip == null)
		{
			if (other.ip != null)
				return false;
		} else if (!ip.equals(other.ip))
			return false;
		if (map == null)
		{
			if (other.map != null)
				return false;
		} else if (!map.equals(other.map))
			return false;
		if (maxPlayerCount == null)
		{
			if (other.maxPlayerCount != null)
				return false;
		} else if (!maxPlayerCount.equals(other.maxPlayerCount))
			return false;
		if (password == null)
		{
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (playerCount == null)
		{
			if (other.playerCount != null)
				return false;
		} else if (!playerCount.equals(other.playerCount))
			return false;
		if (port == null)
		{
			if (other.port != null)
				return false;
		} else if (!port.equals(other.port))
			return false;
		return true;
	}



	public Boolean getPassword()
	{
		return password;
	}
	public void setPassword(Boolean password)
	{
		this.password = password;
	}
	public String getGame()
	{
		return game;
	}
	public void setGame(String game)
	{
		this.game = game;
	}
	public String getIp()
	{
		return ip;
	}
	public void setIp(String ip)
	{
		this.ip = ip;
	}
	public Integer getPort()
	{
		return port;
	}
	public void setPort(Integer port)
	{
		this.port = port;
	}
	public String getHostname()
	{
		return hostname;
	}
	public void setHostname(String hostname)
	{
		this.hostname = hostname;
	}
	public String getGameVersion()
	{
		return gameVersion;
	}
	public void setGameVersion(String gameVersion)
	{
		this.gameVersion = gameVersion;
	}
	public String getMap()
	{
		return map;
	}
	public void setMap(String map)
	{
		this.map = map;
	}
	public String getGameType()
	{
		return gameType;
	}
	public void setGameType(String gameType)
	{
		this.gameType = gameType;
	}
	public Integer getPlayerCount()
	{
		return playerCount;
	}
	public void setPlayerCount(Integer playerCount)
	{
		this.playerCount = playerCount;
	}
	public Integer getMaxPlayerCount()
	{
		return maxPlayerCount;
	}
	public void setMaxPlayerCount(Integer maxPlayerCount)
	{
		this.maxPlayerCount = maxPlayerCount;
	}

    
}
