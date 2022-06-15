package models;

import java.io.Serializable;

public class AgentType implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String name;
	
	public AgentType() {}

	public AgentType(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
