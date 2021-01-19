package net.xiaoxiangshop.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.inject.Inject;

import net.sf.json.JSONArray;
import net.xiaoxiangshop.dao.ErpResultDao;
import net.xiaoxiangshop.entity.*;
import net.xiaoxiangshop.service.ErpResultService;
import org.springframework.stereotype.Service;

/**
 * Service - 售后
 */
@Service
public class ErpResultServiceImpl extends BaseServiceImpl<ErpResult> implements ErpResultService {

    @Inject
    private ErpResultDao erpResultDao;
    private String fileName;

    public void add(HashMap map) {

        String type = String.valueOf(map.get("type"));

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ErpResult result = new ErpResult();
        result.setErpType(String.valueOf(map.get("erpType")));
        try {
            result.setSendTime(simpleDateFormat.parse(String.valueOf(map.get("sendTime"))));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        result.setSendText(String.valueOf(map.get("sendText")));
        try {
            result.setResultTime(simpleDateFormat.parse(String.valueOf(map.get("resultTime"))));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        result.setResultText(String.valueOf(map.get("resultText")));
        result.setResultCode(String.valueOf(map.get("resultCode")));
        result.setOrderSn(String.valueOf(map.get("sn")));
        if ("1".equals(type)) {
            JSONArray jsonArray1 = JSONArray.fromObject(map);
            appendMethodB(simpleDateFormat.format(new Date()) + ":" + jsonArray1.toString());
        } else {
            erpResultDao.save(result);
        }
    }

    public void writeTxt(String msg) throws IOException {
        //设置日期格式
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String date = df.format(new Date());
        //将写入转化为流的形式
        File targetFile = new File("/www/upload/txt/upload/");
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        BufferedWriter bw = new BufferedWriter(new FileWriter("/www/upload/txt/upload/" + date + ".txt"));
        //一次写一行
        bw.write(msg);
        bw.newLine();  //换行用
        //关闭流
        bw.close();
        System.out.println("写入成功");
    }

    public void appendMethodB(String content) {

        try {

            String fileName = "";
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String date = df.format(new Date());
            fileName = "/www/upload/txt/upload/" + date + ".txt";
            FileWriter writer = new FileWriter(fileName, true);
            writer.write(content + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void appendMethodC(String content, String erp_type) {
        try {
            String fileName = "";
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String date = df.format(new Date());
            fileName = "/www/upload/txt/upload/" + erp_type + "_" + date + ".txt";
            FileWriter writer = new FileWriter(fileName, true);
            writer.write(content + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}