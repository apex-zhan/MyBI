package com.yupi.springbootinit.model.dto.chart;

import lombok.Data;

@Data
public class GenChartByAiRequest {
    /**
     * chart名称
     *
     */
    private String name;

    /**
     *  分析目标
     */
    private String goal;

    /**
     * 图标类型
     */
    private String chatType;

    private static final long serialVersionUID = 1L;
}
