package models;

import java.io.Serializable;

public class ClothingItem implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String productName;
	private Double productPrice;
	private String productDescription;
	private Integer productColorsNumber;
	
	public ClothingItem() {}
	
	public ClothingItem(String productName, Double productPrice, String productDescription, Integer productColorsNumber) {
		super();
		this.productDescription = productDescription;
		this.productPrice = productPrice;
		this.productName = productName;
		this.productColorsNumber = productColorsNumber;
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

	public Double getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(Double productPrice) {
		this.productPrice = productPrice;
	}

	public Integer getProductColorsNumber() {
		return productColorsNumber;
	}

	public void setProductColorsNumber(Integer productColorsNumber) {
		this.productColorsNumber = productColorsNumber;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
