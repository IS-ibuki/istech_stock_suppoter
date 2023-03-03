package com.app.istech.Form;

import java.util.HashMap;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CompositionSearchForm {
	
	private Integer parentId;
	
	// 品番
	private String serialNum;
	
	// 品名
	private String productName;
	
	// 子入数
	private String quantity;
	
	private String clientNum;
	
	// ソート順
	private HashMap<String,Integer> sortItems;
	
	private Integer sortValue;
	
	// 並び替え方法
	private HashMap<String,Integer> orderItems;
	
	
	private Integer orderValue;
	
	public CompositionSearchForm(){
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
		this.sortItems.put("子品番",0);
		this.sortItems.put("子品名",1);
		this.sortItems.put("子入数",2);
		this.sortItems.put("仕入先コード",3);
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
