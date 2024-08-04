package com.zxw.springbootinit.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class ChartMapperTest {

    @Test
    void queryChartData() {
        String chartId = "1111";
        String querySql = String.format("select * from chart_%s", chartId);
//        System.out.println(ChartMapper.queryChartData(querySql));
    }
}