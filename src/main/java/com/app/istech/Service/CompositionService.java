package com.app.istech.Service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.app.istech.Form.CompositionForm;
import com.app.istech.Form.CompositionSearchForm;
import com.app.istech.Mapper.CompositionMapper;
import com.app.istech.Mapper.ProductMapper;
import com.app.istech.Model.Composition;
import com.app.istech.Model.Product;

@Service
public class CompositionService {
	
	private final CompositionMapper compositionMapper;
	
	private final ProductMapper productMapper;
	
	@Autowired
	public CompositionService(CompositionMapper compositionMapper,ProductMapper productMapper) {
		this.compositionMapper = compositionMapper;
		this.productMapper = productMapper;
	}
	
	// 全件数取得
	public Long count() {
		return compositionMapper.count();
	}

	// 構成別件数取得
	public Long count(CompositionSearchForm compositionSearchForm,Integer parentId) {		
		compositionSearchForm.setParentId(parentId);
		return compositionMapper.count(compositionSearchForm);
	}
	
	
	public List<Composition> findByParentId(int parentId){
		
		List<Composition> resultList = compositionMapper.findByParentId(parentId);
		
		for(Composition composition : resultList) {
			
			int childId = composition.getChild().getProductId();
			//子なし製品
			if(composition.getParent().getProductId() == childId) {
				break;
			}
			
			findByParentId(childId);
			
		}

		return resultList;
		
	}
	
	// 子製品の再帰的な探索
	public List<Composition> findCompositionByProductId(Integer productId){
		return compositionMapper.findCompositionByProductId(productId);
	}
	
	
	/*
	 * 構成要素の登録
	 */
	@Transactional
	public void insertComposition(Composition composition) {
		compositionMapper.insertComposition(composition);
	}
	
	
	/*
	 * 構成要素の更新
	 */
	@Transactional
	public boolean updateComposition(Composition composition,int previousId) {
		return compositionMapper.updateComposition(composition, previousId);
	}

	
	/*
	 * 構成要素の削除 
	 */
	@Transactional
	public boolean deleteComposition(int childId) {
		return compositionMapper.deleteComposition(childId);
	}
	
	@Transactional
	public void bulkInsert(List<Composition> list){
		for(Composition composition : list) {
			
			if(composition.getParent().getProductId() == null) {	
				Product parent = productMapper.findBySerialNum(composition.getParent().getSerialNum());
				composition.setParent(parent);
			}
			Product child = productMapper.findBySerialNum(composition.getChild().getSerialNum());
			composition.setChild(child);
		}
		compositionMapper.bulkInsertComposition(list);
	}
	
	//CSV形式ファイルをrecordListに変換
	public static List<CSVRecord> parseCSV(MultipartFile file, String encode) throws UnsupportedEncodingException, IOException{
		
		InputStreamReader in = new InputStreamReader(file.getInputStream(),encode);
		//CSVParser parse = CSVFormat.EXCEL.parse(in);
		CSVParser parse = CSVFormat.Builder
				.create()
				.setHeader(CompositionEnum.class)
				.setSkipHeaderRecord(true)
				.build()
				.parse(in);
		
		System.out.println(parse.getHeaderNames());
		List<CSVRecord> recordList = parse.getRecords();
		
		return recordList;
		
	}
	
	
	public static List<Composition> csvRecordToList(List<CSVRecord> csvRecordList){
		List<Composition> compositionList = new ArrayList<Composition>();
		for(CSVRecord record : csvRecordList) {

			Composition composition = new Composition(record.toMap());
			compositionList.add(composition);
		}
		
		return compositionList;
	}
	
	
	/*
	 * フォームオブジェクトをモデルに変換
	 * 
	 * @param  compositionFormList 
	 * ファイル形式から変換されたフォームリスト
	 * 
	 */
	public static List<Composition> formToCompositionList(List<CompositionForm> compositionFormList){
		List<Composition> compositionList = new ArrayList<Composition>();
		for(CompositionForm compositionForm : compositionFormList) {
			compositionList.add(new Composition(compositionForm));	
		}
		return compositionList;
	}

	public static List<Composition> formToCompositionList(List<CompositionForm> compositionFormList, Integer parentId){
		List<Composition> compositionList = new ArrayList<Composition>();
		for(CompositionForm compositionForm : compositionFormList) {
			//親製品IDをセット
			compositionForm.getParent().setProductId(parentId);
			Composition composition = new Composition(compositionForm);
			compositionList.add(composition);	
		}
		return compositionList;
	}
	
	
	public static List<CompositionForm> csvRecordToFormList(List<CSVRecord> csvRecordList){
		
		List<CompositionForm> formList = new ArrayList<CompositionForm>();
		for(CSVRecord record : csvRecordList) {
		
			CompositionForm compositionForm = new CompositionForm(record.toMap());
			formList.add(compositionForm);
		}
		
		return formList;
		
	}
	
	// 内部クラスENUM
	private enum CompositionEnum {
		parentSerialNum,
		childSerialNum,
		quantity;
	}
	
	
}
