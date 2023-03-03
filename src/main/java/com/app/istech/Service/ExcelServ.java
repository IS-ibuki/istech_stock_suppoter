package com.app.istech.Service;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ExcelServ {

	@Value("classpath:template.xlsk")
	private Resource templateResource;

	public Workbook getExcel() throws ResponseStatusException {

		Workbook workbook;
		try {
			workbook = WorkbookFactory.create(templateResource.getInputStream());
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		Sheet sheet = workbook.getSheetAt(0);
		getCell(sheet, 0, 0).setCellValue("1行1列");
		getCell(sheet, 1, 0).setCellValue("2行1列");
		getCell(sheet, 0, 1).setCellValue("1行2列");

		return workbook;
	}

	/**
	 * シート、行番号、列番号をしてセル情報を取得
	 * 
	 * @param sheet    シート
	 * @param rowIndex 行番号（0から開始）
	 * @param colIndex 列番号（0から開始）
	 * @return セル
	 */
	private Cell getCell(Sheet sheet, int rowIndex, int colIndex) {
		Row row = sheet.getRow(rowIndex);
		if (row == null) {
			row = sheet.createRow(rowIndex);
		}
		Cell cell = row.getCell(colIndex);
		if (cell == null) {
			cell = row.createCell(colIndex);
		}
		return cell;
	}
}
