package com.usmcchaserlauncher.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VersionInfoResponse
{
	@JsonProperty("version")
	private String version;
	
	@JsonProperty("description")
	private String description;
	
	@JsonProperty("forceUpdate")
	private Boolean forceUpdate;
	
	public VersionInfoResponse()
	{
		//Jackson
	}
	public String getVersion()
	{
		return version;
	}
	public void setVersion(String version)
	{
		this.version = version;
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}
	public Boolean getForceUpdate()
	{
		return forceUpdate;
	}
	public void setForceUpdate(Boolean forceUpdate)
	{
		this.forceUpdate = forceUpdate;
	}


	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((forceUpdate == null) ? 0 : forceUpdate.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
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
		VersionInfoResponse other = (VersionInfoResponse) obj;
		if (description == null)
		{
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (forceUpdate == null)
		{
			if (other.forceUpdate != null)
				return false;
		} else if (!forceUpdate.equals(other.forceUpdate))
			return false;
		if (version == null)
		{
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		return true;
	}
	
	
}
