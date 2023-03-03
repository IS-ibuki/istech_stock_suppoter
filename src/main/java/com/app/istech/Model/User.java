package com.app.istech.Model;

import java.io.Serializable;
import java.sql.Timestamp;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class User implements Serializable{
	
	// ID
	private String userId;
	
	// Pass
	private String password;
	
	//データ作成日時
	private Timestamp createTs;
		
	//データ更新日時
	private Timestamp updateTs;
}
