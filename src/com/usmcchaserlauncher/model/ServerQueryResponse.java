package com.usmcchaserlauncher.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ServerQueryResponse
{
	  @JsonProperty("serverCount")
	  private String serverCount;

	  @JsonProperty("servers")
	  private List<ServerDetails> servers;

	  
	  
	public ServerQueryResponse()
	{
		//For Jackson
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((serverCount == null) ? 0 : serverCount.hashCode());
		result = prime * result + ((servers == null) ? 0 : servers.hashCode());
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
		ServerQueryResponse other = (ServerQueryResponse) obj;
		if (serverCount == null)
		{
			if (other.serverCount != null)
				return false;
		} else if (!serverCount.equals(other.serverCount))
			return false;
		if (servers == null)
		{
			if (other.servers != null)
				return false;
		} else if (!servers.equals(other.servers))
			return false;
		return true;
	}

	public String getServerCount()
	{
		return serverCount;
	}

	public void setServerCount(String serverCount)
	{
		this.serverCount = serverCount;
	}

	public List<ServerDetails> getServers()
	{
		return servers;
	}

	public void setServers(List<ServerDetails> servers)
	{
		this.servers = servers;
	}
	  
	  
}
