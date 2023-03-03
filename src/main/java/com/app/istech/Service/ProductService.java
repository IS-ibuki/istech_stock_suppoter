package com.app.istech.Service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.app.istech.Form.CompositionSearchForm;
import com.app.istech.Form.ProductForm;
import com.app.istech.Form.ProductSearchForm;
import com.app.istech.Mapper.CompositionMapper;
import com.app.istech.Mapper.ProductMapper;
import com.app.istech.Model.Composition;
import com.app.istech.Model.Product;

@Service
@Transactional
public class ProductService {
	
	@Autowired
	ProductMapper productMapper;

	@Autowired
	CompositionMapper compositionMapper;
	
	@Autowired
	CompositionService compositionService;
	
	@Autowired
	ClientService clientService;
	
	public List<Product> findAll(){
		List<Product> productList = productMapper.findAll();
		return productList;
	}
	
	/*
	 * 製品の全表示
	 */
	public Page<Product> findAll(Pageable pageable,ProductSearchForm productSearchForm) {
		RowBounds rowBounds = new RowBounds((int)pageable.getOffset(),pageable.getPageSize());
		List<Product> productList = productMapper.findAll(rowBounds);
		Long total = productMapper.count(productSearchForm);
		return new PageImpl<Product>(productList,pageable,total);
	}

	/*
	 * 製品の絞り込み検索
	 */
	public Page<Product> findAllBySearchForm(Pageable pageable,ProductSearchForm productSearchForm) {
		RowBounds rowBounds = new RowBounds((int)pageable.getOffset(),pageable.getPageSize());
		List<Product> productList = productMapper.findAllBySearchForm(rowBounds,productSearchForm);
		Long total = productMapper.count(productSearchForm);
		return new PageImpl<Product>(productList,pageable,total);
	}

	
	public Product findById(Integer productId) {
		Product product = productMapper.findById(productId);
		return product;
	}
	
	public Product findPageById(Integer productId,Pageable pageable,CompositionSearchForm compositionSearchForm) {
		compositionSearchForm.setParentId(productId);
		Product product = productMapper.findPageById(pageable,compositionSearchForm);
		return product;
	}
	

	public Product findBySerialNum(String serialNum) {
		Product product = productMapper.findBySerialNum(serialNum);
		return product;
	}

	public List<Product> findBySerialNumNot(String serialNum) {
		List<Product> productList = productMapper.findBySerialNumNot(serialNum);
		return productList;
	}

	
	public void insertProduct(Product product) throws DuplicateKeyException{
		productMapper.insertProduct(product);
	}
	
	public void bulkInsert(List<Product> productList){
		productMapper.bulkInsertProduct(productList);
	}
	
	public void updateDeletedFlg(Product product) {
		product.setDeleted(!product.isDeleted());
		productMapper.updateDeletedFlg(product);
	}

	public void deleteProduct(Integer productId) {
		productMapper.deleteProduct(productId);
	}

	public void updateProduct(Product product){
		productMapper.updateProduct(product);
	}
	
	/**
	 * 現在個数の更新メソッド
	 * 
	 * @param product 更新対象製品
	 * @param orderType 入出庫管理ステータス 
	 * @param deliveryNum 注文数
	 * 
	 * @return boolean
	 * 
	 */
	public boolean updateCurrentNum(Product product,int orderType, int deliveryNum) {
		
		int total = 0;
		switch(orderType){
			//出庫
			case (0):
				total = product.getCurrentNum() - deliveryNum;
				product.setCurrentNum(total);
				break;
			//入庫
			case(1):
				total = product.getCurrentNum() + deliveryNum;
				product.setCurrentNum(total);	
				break;
		}
		
		return productMapper.updateCurrentNum(product);
	}
	
	/**
	 * 子製品の現在個数を更新する
	 * 
	 * @param compositionList　更新対象の子製品要素
	 * @param orderType 入出庫ステータス
	 * @param deliveryNum 注文数
	 * 
	 * @return boolean値
	 */
	public boolean updateChildCurrentNum(List<Composition> compositionList,int orderType,int deliveryNum) {
		
		List<Product> updateProductList = new ArrayList<Product>();
		
		switch(orderType) {
			
			// 出庫
			case(0):
				
				for (Composition composition : compositionList) {
					Product child = composition.getChild();
					//出庫後個数 = 現在個数 - 子入数 * 注文数
					
					int total = child.getCurrentNum() - composition.getQuantity() * deliveryNum;
					child.setCurrentNum(total);
					updateProductList.add(composition.getChild());
				}
			
				break;
			// 入庫
			case(1):
				
				for(Composition composition : compositionList) {
					Product child = composition.getChild();
					
					//入庫後個数 = 現在個数 + 子入数 * 注文数
					int total = child.getCurrentNum() + composition.getQuantity() * deliveryNum;
					child.setCurrentNum(total);
					updateProductList.add(composition.getChild());
				}
				break;
		}
		
		return productMapper.updateChildCurrentNum(updateProductList);
	}
	
	public static List<CSVRecord> parseCSV(MultipartFile file, String encode) throws UnsupportedEncodingException, IOException{
		
		InputStreamReader in = new InputStreamReader(file.getInputStream(),encode);
		//CSVParser parse = CSVFormat.EXCEL.parse(in);
		CSVParser parse = CSVFormat.Builder
				.create()
				.setHeader(ProductEnum.class)
				.setSkipHeaderRecord(true)
				.build()
				.parse(in);

		List<CSVRecord> recordList = parse.getRecords();
		
		return recordList;
		
	}
	
	public static List<Product> csvRecordToList(List<CSVRecord> csvRecordList){
		List<Product> productList = new ArrayList<Product>();
		for(CSVRecord record : csvRecordList) {

			Product product = new Product(record.toMap());
			productList.add(product);
		}
		
		return productList;
	}

	public static List<ProductForm> csvRecordToFormList(List<CSVRecord> csvRecordList){
		
		List<ProductForm> formList = new ArrayList<ProductForm>();
		for(CSVRecord record : csvRecordList) {
		
			ProductForm productForm = new ProductForm(record.toMap());
			formList.add(productForm);
		}
		
		return formList;
		
	}
	
	/*
	 * フォームオブジェクトをモデルに変換
	 * 
	 * @param  productFormList 
	 * ファイル形式から変換されたフォームリスト
	 * 
	 */
	public static List<Product> formToProductList(List<ProductForm> productFormList){
		List<Product> productList = new ArrayList<Product>();
		
		for(ProductForm productForm : productFormList) {
			productList.add(new Product(productForm));	
		}
		return productList;
	}
	
	
	public static List<ProductForm> createFormList(int n){
		List<ProductForm> formList = new ArrayList<ProductForm>();
		
		int count = 0;
		while(count < n) {
			formList.add(new ProductForm());
			count++;
		}
		
		return formList;
	}
	
	// 列挙型内部クラス
	private enum ProductEnum {
		serialNum,
		productName,
		currentNum,
		price,
		clientNum;
	}
	
	
}