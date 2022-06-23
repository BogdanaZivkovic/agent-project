package models;

import java.io.Serializable;

public class ClothingItem implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String productName;
	private String productPrice;
	private String productDescription;
	private String productColorsNumber;
	
	public ClothingItem() {}
	
	public ClothingItem(String productName, String productPrice, String productDescription, String productColorsNumber) {
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
	
	public String getProductPrice() {
		return productPrice;
	}
	
	public void setProductPrice(String productPrice) {
		this.productPrice = productPrice;
	}
	
	public String getProductColorsNumber() {
		return productColorsNumber;
	}
	
	public void setProductColorsNumber(String productColorsNumber) {
		this.productColorsNumber = productColorsNumber;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
