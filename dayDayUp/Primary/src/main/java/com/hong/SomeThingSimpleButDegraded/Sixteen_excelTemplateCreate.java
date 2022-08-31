package com.hong.SomeThingSimpleButDegraded;

import org.apache.catalina.security.SecurityUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.apache.poi.xssf.usermodel.XSSFDataValidationConstraint;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author wanghong
 * @date 2022/8/31
 * @apiNote
 */
public class Sixteen_excelTemplateCreate{

    @RequestMapping(params = "action=exportProductTemplate")
    public void exportProductTemplate(HttpServletRequest request,HttpServletResponse response) {
        response.reset();
        response.setContentType("application/vnd.ms-excel"); // 改成输出excel文件

        try {
            Long userId = 0l;//SecurityUtil.getCurrentUserId();
            if(userId == null){
                throw new Exception("用户未登录！");
            }

            ServletContext servletContext=RequestContextUtils.findWebApplicationContext(request).getServletContext();
            String path=servletContext.getRealPath("/template");
            path=path+"/"+"product.xlsx";
            File f=new File(path);
            FileInputStream in=new FileInputStream(f);
            Workbook wb = WorkbookFactory.create(in);
            Sheet sheet = wb.getSheet("Sheet1");

//            generateRangeList((XSSFSheet)sheet,BoothInfo.class.getSimpleName(),1,wb,"B");
//            generateRangeList((XSSFSheet)sheet,BolongtoTypeEnum.supplier.name(),3, wb, "D");//供应商
//            generateRangeList((XSSFSheet)sheet,BolongtoTypeEnum.client.name(),4, wb, "E");//委托方
//            generateRangeList((XSSFSheet)sheet,BusinessProduct.class.getSimpleName(),5, wb, "F");//产品名称
            generateRangeList((XSSFSheet)sheet,"Country",14, wb, "O");//原产国

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String fileName = URLEncoder.encode("批量导入商品入库模板", "UTF-8")+sdf.format(new Date());
            // 将文件输出
            response.setHeader("Content-disposition",
                    "attachment; filename="+fileName+".xlsx");

            OutputStream ouputStream = response.getOutputStream();
            wb.write(ouputStream);
            ouputStream.flush();
            ouputStream.close();
        } catch (Exception e) {
            System.out.println("导出Excel失败！");
            e.printStackTrace();
        }
    }

    /**
     * 构造有效性数据约束
     */
    private void generateRangeList(XSSFSheet sheet, String type, int column, Workbook wb, String colStr) {
        String[] options = getOptions(type);
        if (options.length == 0){
            return;
        }
        if(String.join(",",options).length()>255){
            //获取所有sheet页个数
            int sheetTotal = wb.getNumberOfSheets();
            String hiddenSheetName = "hiddenSheet" + sheetTotal;
            XSSFSheet hiddenSheet = (XSSFSheet)wb.createSheet(hiddenSheetName);
            Row row;
            //写入下拉数据到新的sheet页中
            for (int i = 0; i < options.length; i++) {
                row = hiddenSheet.createRow(i);
                Cell cell = row.createCell(column);
                cell.setCellValue(options[i]);
            }
            //获取新sheet页内容
            String strFormula = hiddenSheetName + "!$"+colStr+"$1:$"+colStr+"$65535";
            XSSFDataValidationConstraint constraint = new XSSFDataValidationConstraint(DataValidationConstraint.ValidationType.LIST,strFormula);
            // 设置数据有效性加载在哪个单元格上,四个参数分别是：起始行、终止行、起始列、终止列
            CellRangeAddressList regions = new CellRangeAddressList(1,65535, column, column);
            // 数据有效性对象
            DataValidationHelper help = new XSSFDataValidationHelper((XSSFSheet) sheet);
            DataValidation validation = help.createValidation(constraint, regions);
            sheet.addValidationData(validation);
            //将新建的sheet页隐藏掉
            wb.setSheetHidden(sheetTotal, true);
        }else{
            XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheet);
            XSSFDataValidationConstraint dvConstraint = (XSSFDataValidationConstraint) dvHelper
                    .createExplicitListConstraint(options);
            CellRangeAddressList addressList = new CellRangeAddressList(1, 65535, column, column);
            XSSFDataValidation validation = (XSSFDataValidation) dvHelper.createValidation(dvConstraint, addressList);
            sheet.addValidationData(validation);
        }
    }

    /**
     * 构造有效性数据
     */
    private String[] getOptions(String type) {
        List<String> options=new ArrayList<String>();
        int size=0;
    /*    Long userId = SecurityUtil.getCurrentUserId();
        if(BoothInfo.class.getSimpleName().equals(type)){
            options = exportProductTemplateApplicationService.findBoothInfoByUserId(userId);
        }else if(BolongtoTypeEnum.supplier.name().equals(type)){
            options = exportProductTemplateApplicationService.findBusinessmanContactByUserIdAndType(userId,BolongtoTypeEnum.supplier.name());
        }else if(BolongtoTypeEnum.client.name().equals(type)){
            options = exportProductTemplateApplicationService.findBusinessmanContactByUserIdAndType(userId,BolongtoTypeEnum.client.name());
        }else if(BusinessProduct.class.getSimpleName().equals(type)){
            options = exportProductTemplateApplicationService.findBusinessProductByUserId(userId);
        }else if("Country".equals(type)){
            options = exportProductTemplateApplicationService.findProductCountry(type);
        }
        size = CollectionUtils.isEmpty(options)?0:options.size();*/
        return options.toArray(new String[size]);
    }
}
