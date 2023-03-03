package com.app.istech.Model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import com.app.istech.Form.OrderForm;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Order implements Serializable{
	
	// ID
	private Integer orderId;
	
	// 注文番号
	private String orderNum;
	
	//出庫日
	private LocalDateTime deliveryDate;
	
	//納期
	private LocalDateTime deadLine;
	
	// 0=出庫,1=入庫
	private int type;
	
	//注文数
	private int deliveryNum;
	
	private boolean compleated;
	
	// 削除フラグ
	private boolean deleted;
	
	private Timestamp createTs;
	
	private Timestamp updateTs;
	
	private Integer clientId;
	
	private Client client; 
	
	private Product product;
	
	private List<Composition> compositions;
	
	public Order(OrderForm orderForm) {
		this.orderId = orderForm.getOrderId();
		this.orderNum = orderForm.getOrderNum();
		this.type = orderForm.getType();
		this.deliveryNum = Integer.valueOf(orderForm.getDeliveryNum());
		this.compleated = orderForm.isCompleated();
		this.deliveryDate = orderForm.getDeliveryDate();
		this.deadLine = orderForm.getDeadLine();
		this.updateTs = orderForm.getUpdateTs();
		this.createTs = orderForm.getCreateTs();

	}
	
	
}
