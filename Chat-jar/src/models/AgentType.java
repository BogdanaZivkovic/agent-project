package models;

import java.io.Serializable;

public class AgentType implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String name;
	private String hostAlias;
	
	public AgentType() {}

	public AgentType(String name, String hostAlias) {
		super();
		this.name = name;
		this.hostAlias = hostAlias;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getHostAlias() {
		return hostAlias;
	}

	public void setHostAlias(String hostAlias) {
		this.hostAlias = hostAlias;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
