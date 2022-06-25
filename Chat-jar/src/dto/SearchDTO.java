package dto;

import java.io.Serializable;

public class SearchDTO implements Serializable{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String productName;
    private String productDescription;
    private Double minPrice;
    private Double maxPrice;
    private Integer minColorNumber;
    private Integer maxColorNumber;
    
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
	public void setMaxPrice(Double maxPrice) {
		this.maxPrice = maxPrice;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
