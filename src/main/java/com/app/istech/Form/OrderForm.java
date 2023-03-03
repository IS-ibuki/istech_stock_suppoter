package com.app.istech.Form;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import com.app.istech.Form.groups.CreateOrder;
import com.app.istech.Form.groups.UpdateOrder;
import com.app.istech.Model.Client;
import com.app.istech.Model.Order;
import com.app.istech.Model.Product;
import com.app.istech.Validator.CheckUpdateConstraint.CheckUpdateOrder;
import com.app.istech.Validator.DuplicateValidator.NotDuplicateOrderNum;

import lombok.Data;

@Data
@CheckUpdateOrder(id = "orderId",orderNum="orderNum", groups= {UpdateOrder.class})
public class OrderForm {
	
	// ID
	private Integer orderId;
	
	// 注文番号
	@NotBlank(groups = {CreateOrder.class,UpdateOrder.class})
	@Length(max=30,groups = {CreateOrder.class,UpdateOrder.class})
	@NotDuplicateOrderNum(groups = {CreateOrder.class})
	private String orderNum;
	
	private int type;
	
	// 注文数
	@NotBlank(groups = {CreateOrder.class,UpdateOrder.class})
	@PositiveOrZero(groups = {CreateOrder.class,UpdateOrder.class})
	private String deliveryNum;
	
	private boolean deleted;

	private HashMap<String,Boolean> radioCompleate;
	
	private HashMap<String,Integer> radioType;
	
	private List<Client> clientList;

	private List<Product> productList;
	
	private boolean compleated;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")	
	private LocalDateTime deliveryDate;

	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")	
	private LocalDateTime deadLine;
	

	private Timestamp createTs;

	private Timestamp updateTs;
	
	private int productId;
	
	@Valid
	private ProductForm productForm;
	
	@Valid
	private ClientForm clientForm;
	
	public OrderForm() {
		setRadioCompleateItem();
		setRadioTypeItem();
	}
	
	public OrderForm(Order order) {
		this.orderId = order.getOrderId();
		this.orderNum = order.getOrderNum();
		this.compleated = order.isCompleated();
		this.type = order.getType();
		this.deliveryNum = Objects.toString(order.getDeliveryNum());
		this.deliveryDate = order.getDeliveryDate();
		this.deleted = order.isDeleted();
		this.deadLine = order.getDeadLine();
		this.createTs = order.getCreateTs();
		this.updateTs = order.getUpdateTs();
		this.productForm = new ProductForm(order.getProduct());
		this.clientForm = new ClientForm(order.getClient());
		
		setRadioCompleateItem();
		setRadioTypeItem();		
	}
	
	private void setRadioCompleateItem() {
		this.radioCompleate = new HashMap<String,Boolean>();
		radioCompleate.put("未完了",false);
		radioCompleate.put("完了",true);
	}
	
	private void setRadioTypeItem() {
		this.radioType  = new HashMap<String,Integer>();
		radioType.put("出庫", 0);
		//radioType.put("入庫", 1);
	}
	
}
