package models;

import java.io.Serializable;

public class Host implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String address;
	private String alias;
	
	public Host() { }
	
	public Host(String address, String alias) {
		super();
		this.address = address;
		this.alias = alias;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
