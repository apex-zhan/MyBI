package com.yupi.springbootinit.utils;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
@Slf4j
/**
 * Excel 相关工具类
 */
public class ExcelUtils {
    /**
     * Excel 转 CSV
     * @param multipartFile
     * @return
     */
    public static String ExceltoCSV(MultipartFile multipartFile) {
//        File file = null;
//        try {
//            file = ResourceUtils.getFile("classpath:test_excel.xlsx");
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        }
        //读取数据
        List<Map<Integer, String>> list = null;
        try {
            list = EasyExcel.read(multipartFile.getInputStream())
                    .excelType(ExcelTypeEnum.XLSX)
                    .sheet()
                    .headRowNumber(0)
                    .doReadSync();
        } catch (IOException e) {
            log.error("读取Excel失败", e);
        }
        if (CollUtil.isEmpty(list)) {
            return "";
        }
        //转化csv,并将这些字符串进行完整拼接
        StringBuilder StringBuilder = new StringBuilder();
        //1）获取表头第一行,读取的数据是线性的
        LinkedHashMap<Integer, String> headerMap = (LinkedHashMap<Integer, String>) list.get(0);
        //2）过滤为null的数据
        List<String> headerList = headerMap.values().stream().filter(ObjectUtils::isNotEmpty).collect(toList());
        StringBuilder.append(StringUtils.join(headerList, ",")).append("\n");
        //2)读取数据
        for (int i = 1; i < list.size(); i++) {
            LinkedHashMap<Integer, String> dataMap = (LinkedHashMap<Integer, String>) list.get(i);
            List<String> dataList = dataMap.values().stream().filter(ObjectUtils::isNotEmpty).collect(toList());
            StringBuilder.append(StringUtils.join(dataList, ",")).append("\n");
//            System.out.println(StringUtils.join(dataList, ","));
        }
//        System.out.println(list);
            return StringBuilder.toString() ;
        }

    /**
     * 测试
     * @param args
     */
    public static void main(String[] args) {
        ExceltoCSV(null);
    }
}
