package com.app.istech.Mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.data.domain.Pageable;

import com.app.istech.Form.CompositionSearchForm;
import com.app.istech.Form.ProductSearchForm;
import com.app.istech.Model.Product;

@Mapper
public interface ProductMapper {
	
	

	// 全製品を取り出すクエリ発行
	List<Product> findAll();
	
	List<Product> findAll(RowBounds rowBounds);
	
	List<Product> findAllBySearchForm(@Param("rowBounds")RowBounds rowBounds,
			@Param("productSearchForm")ProductSearchForm productSearchForm);
	
	//製品数
	Long count(@Param("productSearchForm") ProductSearchForm productSearchForm);
	
	// １つの製品マスタ発行クエリ
	Product findById(Integer productId);

	// １つの製品マスタ発行クエリ
	Product findPageById(@Param("pageable")Pageable pageable,@Param("compositionSearchForm")CompositionSearchForm compositionSearchForm);

	
	Product findBySerialNum(String serialNum);

	// 指定した品番以外の製品取得
	List<Product> findBySerialNumNot(String serialNum);

	boolean updateCurrentNum(Product product);

	boolean updateChildCurrentNum(List<Product> productList);
	
	// 製品マスタ挿入クエリ(1データ)
	boolean insertProduct(Product product);
	
	// 製品一括登録
	void bulkInsertProduct(@Param("productList")List<Product> productList);
	
	// 製品表示切り替え
	void updateDeletedFlg(Product product);

	// 製品マスタ削除クエリ
	boolean deleteProduct(Integer productId);

	// 製品マスタ更新クエリ
	boolean updateProduct(Product product);

}
