package com.funding.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * Excel 导出工具（Apache POI XSSFWorkbook）
 */
public class ExcelUtils {

    /**
     * 通用 Excel 写出方法
     *
     * @param response HttpServletResponse
     * @param fileName 文件名（含中文）
     * @param headers 表头
     * @param dataRows 数据行（每行为一个 List<Object>）
     */
    public static void writeWorkbook(HttpServletResponse response, String fileName,
                                     List<String> headers, List<List<Object>> dataRows) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        try {
            Sheet sheet = workbook.createSheet("Sheet1");
            // 表头
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.size(); i++) {
                headerRow.createCell(i).setCellValue(headers.get(i));
            }
            // 数据行
            for (int i = 0; i < dataRows.size(); i++) {
                Row row = sheet.createRow(i + 1);
                List<Object> rowData = dataRows.get(i);
                for (int j = 0; j < rowData.size(); j++) {
                    Cell cell = row.createCell(j);
                    Object val = rowData.get(j);
                    setCellValue(cell, val);
                }
            }
            // 自动列宽
            for (int i = 0; i < headers.size(); i++) {
                sheet.autoSizeColumn(i);
            }

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition",
                    "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
            response.setCharacterEncoding("UTF-8");
            workbook.write(response.getOutputStream());
            response.getOutputStream().flush();
        } finally {
            workbook.close();
        }
    }

    private static void setCellValue(Cell cell, Object val) {
        if (val == null) {
            cell.setCellValue("");
        } else if (val instanceof Number) {
            cell.setCellValue(((Number) val).doubleValue());
        } else if (val instanceof java.util.Date) {
            cell.setCellValue(val.toString());
        } else {
            cell.setCellValue(val.toString());
        }
    }
}
