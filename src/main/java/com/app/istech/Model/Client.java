package com.app.istech.Model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import com.app.istech.Form.ClientForm;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Client implements Serializable{
	
	// ID
	private Integer clientId;
	
	// 顧客コード
	private String clientNum;
	
	// 顧客名
	private String clientName;
	
	// 削除フラグ
	private boolean deleted;
	
	private List<Order> orders;
	
	// 作成日時
	private Timestamp createTs;
	
	// 更新日時
	private Timestamp updateTs;

	public Client(ClientForm clientForm) {
		this.clientId = clientForm.getClientId();
		this.clientNum = clientForm.getClientNum();
		this.clientName = clientForm.getClientName();
		this.deleted = clientForm.isDeleted();
	}
	
}
