package dto;

import java.io.Serializable;

import agents.AID;

public class SupplyDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	public AID aid;
	public String website;
	
	public SupplyDTO() {}
	
	public AID getAid() {
		return aid;
	}
	
	public void setAid(AID aid) {
		this.aid = aid;
	}
	
	public String getWebsite() {
		return website;
	}
	
	public void setWebsite(String website) {
		this.website = website;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
