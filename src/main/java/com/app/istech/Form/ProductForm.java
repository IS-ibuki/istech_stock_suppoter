package com.app.istech.Form;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

import org.hibernate.validator.constraints.Length;

import com.app.istech.Form.groups.CreateComposition;
import com.app.istech.Form.groups.CreateOrder;
import com.app.istech.Form.groups.CreateProduct;
import com.app.istech.Form.groups.UpdateOrder;
import com.app.istech.Form.groups.UpdateProduct;
import com.app.istech.Model.Client;
import com.app.istech.Model.Product;
import com.app.istech.Validator.CheckUpdateConstraint.CheckUpdateProduct;
import com.app.istech.Validator.DuplicateValidator.NotDuplicateSerialNum;
import com.app.istech.Validator.Register.NotRegisterProduct;

import lombok.Data;

@Data
@CheckUpdateProduct(id = "productId", serialNum = "serialNum",groups = {UpdateProduct.class})
public class ProductForm implements Serializable {
	
	// ID
	private Integer productId;
	
	// 品番
	@NotBlank(groups = {CreateProduct.class,UpdateProduct.class,CreateOrder.class,UpdateOrder.class})
	@Length(max=30,groups = {CreateProduct.class,UpdateProduct.class,CreateOrder.class,UpdateOrder.class})
	@NotRegisterProduct(groups = {CreateOrder.class,UpdateOrder.class,CreateComposition.class})
	@NotDuplicateSerialNum(groups = {CreateProduct.class})
	private String serialNum;
	
	//製品名
	@NotBlank(groups = {CreateProduct.class,UpdateProduct.class})
	@Length(max=50,groups = {CreateProduct.class,UpdateProduct.class})
	private String productName;
	
	// 単価
	@PositiveOrZero(groups = {CreateProduct.class,UpdateProduct.class})
	@NotBlank(groups = {CreateProduct.class,UpdateProduct.class})
	private String price;
	
	// 削除フラグ
	private boolean deleted;
	
	@Valid
	private ClientForm clientForm;
	
	private List<Client> clientList;
	
	//現在個数
	@NotBlank(groups = {CreateProduct.class,UpdateProduct.class})
	private String currentNum;
	
	// 作成日時
	private Timestamp createTs;
	
	// 更新日時
	private Timestamp updateTs;
	
	public ProductForm() {
		this.price = "0";
		this.currentNum = "0";
	}
	
	public ProductForm(Product product) {
		this.productId = product.getProductId();
		this.serialNum = product.getSerialNum();
		this.productName = product.getProductName();
		this.price = Objects.toString(product.getPrice());
		this.currentNum = Objects.toString(product.getCurrentNum());
		this.createTs = product.getCreateTs();
		this.updateTs = product.getUpdateTs();
		
		this.clientForm = new ClientForm(product.getClient());
	}
	
	public ProductForm(Map<String,String> map) { 
		this.serialNum = map.get("serialNum");
		this.productName = map.get("productName");
		this.currentNum = map.get("currentNum");
		this.price = map.get("price");
		
		this.clientForm = new ClientForm();
		this.clientForm.setClientNum(map.get("clientNum"));
	}
	

}
