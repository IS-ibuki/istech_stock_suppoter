package com.app.istech.Form;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import com.app.istech.Form.groups.CreateClient;
import com.app.istech.Form.groups.CreateOrder;
import com.app.istech.Form.groups.CreateProduct;
import com.app.istech.Form.groups.UpdateClient;
import com.app.istech.Form.groups.UpdateOrder;
import com.app.istech.Form.groups.UpdateProduct;
import com.app.istech.Model.Client;
import com.app.istech.Validator.CheckUpdateConstraint.CheckUpdateClient;
import com.app.istech.Validator.DuplicateValidator.NotDuplicateClientNum;
import com.app.istech.Validator.Register.NotRegisterClient;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@CheckUpdateClient(id = "clientId",clientNum = "clientNum", groups= {UpdateClient.class})
public class ClientForm {
	
	// ID
	private Integer clientId;
	
	// 顧客先コード
	@NotBlank(groups = {CreateProduct.class,UpdateProduct.class,CreateClient.class,UpdateClient.class,
			CreateOrder.class,UpdateOrder.class})
	@Length(max=10,groups = {CreateClient.class,UpdateClient.class,CreateOrder.class,UpdateOrder.class})
	@NotRegisterClient(groups = {CreateProduct.class,UpdateProduct.class,CreateOrder.class,UpdateOrder.class})
	@NotDuplicateClientNum(groups = {CreateClient.class})
	private String clientNum;
	
	// 顧客先名
	@NotBlank(groups = {CreateClient.class,UpdateClient.class})
	@Length(max=50,groups = {CreateClient.class,UpdateClient.class})
	private String clientName;
	
	private boolean deleted;
	
	public ClientForm(Client client) {
		this.clientId = client.getClientId();
		this.clientNum = client.getClientNum();
		this.clientName = client.getClientName();
		this.deleted = client.isDeleted();
	}
	
}
