package com.app.istech.Form;

import java.util.HashMap;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductSearchForm {
	
	// 品番
	private String serialNum;
	
	// 品名
	private String productName;

	// 顧客コード
	private String clientNum;
	
	// ソート順
	private HashMap<String,Integer> sortItems;
	
	private int sortValue;
	
	// 並び替え方法
	private HashMap<String,Integer> orderItems;
	
	private int orderValue;
	
	private boolean deleted;
	
	public ProductSearchForm(){
		this.deleted = true;
		setSortItem();
		setOrderByItem();
	}
	
	
	/*
	 * ソート順の初期化
	 */
	private void setSortItem() {
		// 初期値のセット
		this.sortValue = 0;
		
		this.sortItems = new HashMap<String,Integer>();
		this.sortItems.put("品番",0);
		this.sortItems.put("品名",1);
		this.sortItems.put("現在個数",2);
		this.sortItems.put("顧客名",3);
	}
	
	
	/*
	 * 並び替え方法の初期化
	 */
	private void setOrderByItem() {
		// 初期値の設定
		this.orderValue = 0;
		
		this.orderItems = new HashMap<String,Integer>();
		this.orderItems.put("昇順", 0);
		this.orderItems.put("降順", 1);
	}
	
}
