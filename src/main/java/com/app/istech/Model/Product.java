package com.app.istech.Model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.app.istech.Form.ProductForm;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Product implements Serializable{
	
	private Integer productId;
	
	private String serialNum;
	
	private String productName;
	
	private double price;
	
	private int currentNum;
	
	private boolean deleted;
	
	private Integer supplierId;
	
	private Timestamp createTs;
	
	private Timestamp updateTs;
	
	private Client client;
	
	private List<Composition> childs;
	
	public Product(ProductForm productForm) {
		this.productId = productForm.getProductId();
		this.serialNum = productForm.getSerialNum();
		this.productName = productForm.getProductName();
		this.price = Double.valueOf(productForm.getPrice());
		this.currentNum = Integer.valueOf(productForm.getCurrentNum());
		this.createTs = productForm.getCreateTs();
		this.updateTs = productForm.getUpdateTs();
		
	}
	
	public Product(Map<String,String> map) {
		this.serialNum = map.get("productId");
		this.productName = map.get("productName");
		this.currentNum = Integer.valueOf(map.get("currentNum"));
		this.price = Double.valueOf(map.get("price"));
		
		this.client = new Client();
		this.client.setClientNum(map.get("supplierId"));
	}
	
	
}
