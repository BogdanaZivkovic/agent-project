package dto;

import agents.AID;

public class SupplyDTO {
	
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
}
