package com.zxw.springbootinit.model.vo;

import lombok.Data;

/**
 * bi的返回给前端的结果
 */
@Data
public class BiResponse {

    /**
     * 生成图表
     */
    private String genChart;

    /**
     * 生成图表结果
     */
    private String genResult;

    /**
     * 新生成的图表id
     */
    private Long chartId;
}
