package net.xiaoxiangshop.util.ExcelUtil;

import org.apache.poi.hssf.usermodel.HSSFDataFormatter;
//import org.apache.poi.ooxml.util.SAXHelper;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class ReadExcelUtil {
    protected static final Logger LOG = LoggerFactory.getLogger(ReadExcelUtil.class);
    private final OPCPackage xlsxPackage;
    private LinkedHashMap<Integer,Object> datas;
    private Map<String,String> rowDatas;
    private String colum;
    private Long count;
    private Class clazz;
    private Map<Field,Excel> fieldExcelMap;
    private ReadExcelSevice readExcelSevice;
    public static boolean suspension =false;
    public ReadExcelUtil(OPCPackage xlsxPackage) {
        this.xlsxPackage = xlsxPackage;
        datas  = new LinkedHashMap<>();
    }
    public class  SheetRead implements XSSFSheetXMLHandler.SheetContentsHandler {
        @Override
        public void startRow(int i) {
            rowDatas = new HashMap<>();
            if(count == null){
                count = 0L;
            }
            if(count %10000==0){
                LOG.info("读取行{}",count);
            }
            count++;
        }

        @Override
        public void endRow(int i) {

            try {
                Excel annotation = (Excel) clazz.getAnnotation(Excel.class);
                if(count-1< annotation.row()){
                    return;
                }
                Map<String,String> newData = new HashMap<>();
                newData.putAll(rowDatas);
                Object object = clazz.newInstance();
                for(Map.Entry<Field,Excel> entry:fieldExcelMap.entrySet()){
                    String cell = newData.get(entry.getValue().cell());
                    ImportBig.setValue(cell,entry,object);
                }
                if(readExcelSevice != null){
                    readExcelSevice.callback(Integer.parseInt(count+""),object);
                } else {
                    datas.put(i,object);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }



        }

        @Override
        public void cell(String cellReference, String formattedValue,
                         XSSFComment comment) {
            String key = cellReference.replaceAll("\\d+","");
            rowDatas.put(key,formattedValue);
        }

        @Override
        public void headerFooter(String s, boolean b, String s1) {

        }

    }
    /**
     * Initiates the processing of the XLS workbook file to CSV.
     *
     * @throws IOException
     * @throws OpenXML4JException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    public void process()
            throws IOException, OpenXML4JException, ParserConfigurationException, SAXException {
        ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(this.xlsxPackage);
        XSSFReader xssfReader = new XSSFReader(xlsxPackage);
        StylesTable styles = xssfReader.getStylesTable();
        XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
        int index = 0;
        while (iter.hasNext()) {
            InputStream stream = iter.next();
            String sheetName = iter.getSheetName();
            processSheet(styles, strings, new SheetRead(), stream);
            stream.close();
            ++index;
        }
    }

    /**
     * Parses and shows the content of one sheet
     * using the specified styles and shared-strings tables.
     *
     * @param styles
     * @param strings
     * @param sheetInputStream
     */
    public void processSheet(
            StylesTable styles,
            ReadOnlySharedStringsTable strings,
            XSSFSheetXMLHandler.SheetContentsHandler sheetHandler,
            InputStream sheetInputStream)
            throws IOException, ParserConfigurationException, SAXException {
        DataFormatter formatter = new HSSFDataFormatter();
        if(this.getFieldExcelMap() != null){
            for(Map.Entry<Field,Excel> entry:this.getFieldExcelMap().entrySet()){
                if(!entry.getValue().dataFormatter().equals("")){
                    formatter.addFormat(entry.getValue().dataFormatter(),new SimpleDateFormat(entry.getValue().dateFromat()));
                }
            }
        }
        Locale locale = Locale.SIMPLIFIED_CHINESE;
        formatter.addFormat("General",new GeneralFormat(locale));
        formatter.addFormat("@",new GeneralFormat(locale));
        InputSource sheetSource = new InputSource(sheetInputStream);
//        try {
//            XMLReader sheetParser = SAXHelper.newXMLReader();
//            ContentHandler handler = new XSSFSheetXMLHandler(
//                    styles, null, strings, sheetHandler, formatter, false);
//            sheetParser.setContentHandler(handler);
//            sheetParser.parse(sheetSource);
//        } catch (ParserConfigurationException e) {
//            throw new RuntimeException("SAX parser appears to be broken - " + e.getMessage());
//        }
    }

    public LinkedHashMap<Integer, Object> getDatas() {
        return datas;
    }

    public void setDatas(LinkedHashMap<Integer, Object> datas) {
        this.datas = datas;
    }

    public Map<String, String> getRowDatas() {
        return rowDatas;
    }

    public void setRowDatas(Map<String, String> rowDatas) {
        this.rowDatas = rowDatas;
    }

    public String getColum() {
        return colum;
    }

    public void setColum(String colum) {
        this.colum = colum;
    }

    public static void main(String[] args) {
        try {
            OPCPackage p = OPCPackage.open(new FileInputStream(new File("E:\\doc\\主数据\\123.xlsx")));
            ReadExcelUtil readExcelUtil = new ReadExcelUtil(p);
            readExcelUtil.process();
        //    LinkedHashMap<Integer, Map<String, String>> datas = readExcelUtil.getDatas();
//            for(Map.Entry<Integer, Map<String, String>> entryRow : datas.entrySet()){
//                for(Map.Entry<Field,Excel> entry:fieldExcelMap.entrySet()){
//                    String cell = row.get(entry.getValue().cell());
//                    System.out.println(entry.getValue().cell()+"----"+cell);
//                }
//            }
            System.out.println( readExcelUtil.getRowDatas().size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<Field, Excel> getFieldExcelMap() {
        return fieldExcelMap;
    }

    public void setFieldExcelMap(Map<Field, Excel> fieldExcelMap) {
        this.fieldExcelMap = fieldExcelMap;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public ReadExcelSevice getReadExcelSevice() {
        return readExcelSevice;
    }

    public void setReadExcelSevice(ReadExcelSevice readExcelSevice) {
        this.readExcelSevice = readExcelSevice;
    }
}
