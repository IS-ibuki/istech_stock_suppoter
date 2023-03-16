package com.app.istech.Form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Component
public class UploadForm implements Serializable{
	
	// ファイル受信
	private MultipartFile file;
	
	private String abc;
	
	// 製品結果リスト
	@Valid
	private List<ProductForm> productList = new ArrayList<ProductForm>();
	
	// 構成品リスト
	@Valid
	private List<CompositionForm> compositionList = new ArrayList<CompositionForm>();
	
	
}
