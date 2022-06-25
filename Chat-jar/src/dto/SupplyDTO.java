package dto;

import java.io.Serializable;

import agents.AID;

public class SupplyDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	private AID aid;
	private String website;
	private String productName;
    private String productDescription;
    private Double minPrice;
    private Double maxPrice;
    private Integer minColorNumber;
    private Integer maxColorNumber;
	
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

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public Double getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(Double minPrice) {
		this.minPrice = minPrice;
	}

	public Double getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(Double maxPrice) {
		this.maxPrice = maxPrice;
	}

	public Integer getMinColorNumber() {
		return minColorNumber;
	}

	public void setMinColorNumber(Integer minColorNumber) {
		this.minColorNumber = minColorNumber;
	}

	public Integer getMaxColorNumber() {
		return maxColorNumber;
	}

	public void setMaxColorNumber(Integer maxColorNumber) {
		this.maxColorNumber = maxColorNumber;
	}
	
}
