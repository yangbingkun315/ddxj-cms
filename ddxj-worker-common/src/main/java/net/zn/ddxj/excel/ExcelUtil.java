package net.zn.ddxj.excel;
/** 
 *  
 */  
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;  
  
/** 
 * 
 * Description: Excel操作 
 *  
 * CreateTime: 2017年12月11日  下午3:08:09 
 * 
 * Change History: 
 * 
 *        Date             CR Number              Name              Description of change 
 * 
 */  
public class ExcelUtil {  
  
    private static final String EXCEL_XLS = "xls";    
    private static final String EXCEL_XLSX = "xlsx";    
    
    /**  
     * 判断Excel的版本,获取Workbook  
     * @param in  
     * @param filename  
     * @return  
     * @throws IOException  
     */    
    public static Workbook getWorkbok(InputStream in,File file) throws IOException{    
        Workbook wb = null;    
        if(file.getName().endsWith(EXCEL_XLS)){  //Excel 2003    
            wb = new HSSFWorkbook(in);    
        }else if(file.getName().endsWith(EXCEL_XLSX)){  // Excel 2007/2010    
            wb = new XSSFWorkbook(in);    
        }    
        return wb;    
    }    
    
    /**  
     * 判断文件是否是excel  
     * @throws Exception   
     */    
    public static void checkExcelVaild(File file) throws Exception{    
        if(!file.exists()){    
            throw new Exception("文件不存在");    
        }    
        if(!(file.isFile() && (file.getName().endsWith(EXCEL_XLS) || file.getName().endsWith(EXCEL_XLSX)))){    
            throw new Exception("文件不是Excel");    
        }    
    }    
     public static List<Map<String,Object>> queryExclData()
     {
    	 List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
    	 SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");    
         try {    
             // 同时支持Excel 2003、2007    
             File excelFile = new File("F:/sensitive.xlsx"); // 创建文件对象    
             FileInputStream in = new FileInputStream(excelFile); // 文件流    
             checkExcelVaild(excelFile);    
             Workbook workbook = getWorkbok(in,excelFile);    
             /**  
              * 设置当前excel中sheet的下标：0开始  
              */    
             Sheet sheet = workbook.getSheetAt(0);   // 遍历第1个Sheet    
             // 为跳过第一行目录设置count    
             int count = 0;  
             for (Row row : sheet) {  
                 try {  
                     // 跳过第一和第二行的目录    
                     if(count < 1 ) {  
                         count++;    
                         continue;    
                     }  
                       
                     //如果当前行没有数据，跳出循环    
                     if(row.getCell(0).toString().equals("")){    
                         return null;  
                     }  
                       
                     for (int i = 0; i < 1; i++) 
                     {  
                    	 Map<String,Object> param = new HashMap<String,Object>();
                         Cell cell1 = row.getCell(0); 
                         Cell cell2 = row.getCell(1); 
                         param.put("type", cell1);
                         param.put("content", cell2);
                         result.add(param);
                     }  
                 } catch (Exception e) {  
                     e.printStackTrace();  
                 }  
             }    
         } catch (Exception e) {    
             e.printStackTrace();    
         }  
         return result;
     }
    /**  
     * 读取Excel测试，兼容 Excel 2003/2007/2010  
     * @throws Exception   
     */    
    public static void main(String[] args) throws Exception {    
    	ExcelUtil.queryExclData();
//        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");    
//        try {    
//            // 同时支持Excel 2003、2007    
//            File excelFile = new File("F:/sensitive.xlsx"); // 创建文件对象    
//            FileInputStream in = new FileInputStream(excelFile); // 文件流    
//            checkExcelVaild(excelFile);    
//            Workbook workbook = getWorkbok(in,excelFile);    
//            //Workbook workbook = WorkbookFactory.create(is); // 这种方式 Excel2003/2007/2010都是可以处理的    
//    
//            int sheetCount = workbook.getNumberOfSheets(); // Sheet的数量    
//            /**  
//             * 设置当前excel中sheet的下标：0开始  
//             */    
////            Sheet sheet = workbook.getSheetAt(0);   // 遍历第一个Sheet    
//            Sheet sheet = workbook.getSheetAt(0);   // 遍历第三个Sheet    
//              
//            //获取总行数  
////          System.out.println(sheet.getLastRowNum());  
//              
//            // 为跳过第一行目录设置count    
//            int count = 0;  
//            for (Row row : sheet) {  
//                try {  
//                    // 跳过第一和第二行的目录    
//                    if(count < 1 ) {  
//                        count++;    
//                        continue;    
//                    }  
//                      
//                    //如果当前行没有数据，跳出循环    
//                    if(row.getCell(0).toString().equals("")){    
//                        return;  
//                    }  
//                      
//                    //获取总列数(空格的不计算)  
//                    int columnTotalNum = row.getPhysicalNumberOfCells();  
//                   // System.out.println("总列数：" + columnTotalNum);  
//                      
//                    //System.out.println("最大列数：" + row.getLastCellNum());  
//                      
//                    //for循环的，不扫描空格的列  
////                    for (Cell cell : row) {   
////                      System.out.println(cell);  
////                    }  
//                    int end = row.getLastCellNum(); 
//                    for (int i = 0; i < 1; i++) {  
//                        Cell cell1 = row.getCell(0); 
//                        Cell cell2 = row.getCell(1); 
//                        System.out.print(cell1 + "\t"+cell2 +"\n");  
//                    }  
//                } catch (Exception e) {  
//                    e.printStackTrace();  
//                }  
//            }    
//        } catch (Exception e) {    
//            e.printStackTrace();    
//        }  
    }  
      
    private static Object getValue(Cell cell) {  
        Object obj = null;  
        switch (cell.getCellTypeEnum()) {  
            case BOOLEAN:  
                obj = cell.getBooleanCellValue();   
                break;  
            case ERROR:  
                obj = cell.getErrorCellValue();   
                break;  
            case NUMERIC:  
                obj = cell.getNumericCellValue();   
                break;  
            case STRING:  
                obj = cell.getStringCellValue();   
                break;  
            default:  
                break;  
        }  
        return obj;  
    }  
    
	public static String exportAjaxExcelData(String excelFileName,
			HSSFWorkbook wb) {
		FileInputStream fis = null;
		FileOutputStream fos = null;
		String excelFilePath="";
		//输出文件
		String tmpZipDirectoryName = UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();
		String tmpZipDirectoryPath = System.getProperty("java.io.tmpdir") + File.separator + tmpZipDirectoryName + File.separator;
		File tmpZipDirectory = new File(tmpZipDirectoryPath);
		if (!tmpZipDirectory.exists()) {
			tmpZipDirectory.mkdirs();
		}
		try{
			excelFilePath = tmpZipDirectoryPath + tmpZipDirectoryName + ".xls";
			fos = new FileOutputStream(excelFilePath);
			wb.write(fos);
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			IOUtils.closeQuietly(fis);
			IOUtils.closeQuietly(fos);
		}
		return excelFilePath;
	}
}  