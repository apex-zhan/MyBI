package com.zxw.springbootinit.bimq;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.util.StringUtils;
import com.rabbitmq.client.Channel;

import com.zxw.springbootinit.common.ErrorCode;
import com.zxw.springbootinit.constant.CommonConstant;
import com.zxw.springbootinit.exception.BusinessException;
import com.zxw.springbootinit.manager.AiManager;
import com.zxw.springbootinit.model.entity.Chart;
import com.zxw.springbootinit.model.enums.QueueStatusEnum;
import com.zxw.springbootinit.service.ChartService;
import com.zxw.springbootinit.service.impl.ChartServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

import static com.zxw.springbootinit.common.ErrorCode.NOT_FOUND_ERROR;

/**
 * bi消息队列的消费者
 */
@Component
@Slf4j
public class biMessageConsumer {
    /**
     * 接受信息的方法
     * 指定程序的监听的消息队列和确认机制
     */
    @Resource
    private AiManager aiManager;
    @Resource
    private ChartService chartService;
    @Resource
    private ChartServiceImpl chartServiceImpl;

    @RabbitListener(queues = {biconstant.BI_QUEUE_NAME}, ackMode = "MANUAL")
    public void receiveMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) Long deliveryTag) throws IOException {
        if (StrUtil.isBlank(message)) {
            //如果失败，拒绝消息
            channel.basicNack(deliveryTag, false, false);
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "消息为空");
        }
        //message就是chartId
        Long chartId = Long.parseLong(message);
        Chart chart = chartService.getById(chartId);
        if (chart == null) {
            // 拒绝还是确认这个消息（结合实际业务考虑，这里先拒绝）
            channel.basicNack(deliveryTag, false, false);
            throw new BusinessException(NOT_FOUND_ERROR, "图表不存在");

        }

        // 先修改图表任务状态为 “执行中”。等执行成功后，修改为 “已完成”、保存执行结果；执行失败后，状态修改为 “失败”，记录任务失败信息。
        Chart updateChart = new Chart();
        updateChart.setId(chart.getId());
        updateChart.setStatus(QueueStatusEnum.RUNNING.getValue());
        boolean b = chartService.updateById(updateChart);
        if (!b) {
            channel.basicNack(deliveryTag, false, false);
            handleChartUpdateError(chart.getId(), "更新图表执行中状态失败");
            return;
        }
        // 调用 AI
        String result = aiManager.doChat(CommonConstant.Ai_Model_ID, buildUserInput(chart));
        String[] splits = result.split("【【【【【");
        if (splits.length < 3) {
            handleChartUpdateError(chart.getId(), "AI 生成错误");
            return;
        }
        String genChart = splits[1].trim();
        String genResult = splits[2].trim();
        Chart updateChartResult = new Chart();
        updateChartResult.setId(chart.getId());
        updateChartResult.setGenChart(genChart);
        updateChartResult.setGenResult(genResult);
        updateChartResult.setStatus(QueueStatusEnum.SUCCEED.getValue());
        boolean updateResult = chartService.updateById(updateChartResult);
        if (!updateResult) {
            channel.basicNack(deliveryTag, false, false);
            handleChartUpdateError(chart.getId(), "更新图表成功状态失败");
        }
        log.info("receive message: {}", message);
        // 手动确认,向mq发送确认消息
        channel.basicAck(deliveryTag, false);
    }

    /**
     * 构建用户输入
     *
     * @param chart
     * @return
     */
    private String buildUserInput(Chart chart) {
        String goal = chart.getGoal();
        String chartType = chart.getChartType();
        String csvData = chart.getChartData();
        // 构造用户输入
        StringBuilder userInput = new StringBuilder();
        userInput.append("分析需求：").append("\n");

        // 拼接分析目标
        String userGoal = goal;
        if (StringUtils.isNotBlank(chartType)) {
            userGoal += "，请使用" + chartType;
        }
        userInput.append(userGoal).append("\n");
        userInput.append("原始数据：").append("\n");
        // 压缩后的数据
        userInput.append(csvData).append("\n");

        return userInput.toString();
    }

    // 上面的接口很多用到异常,直接定义一个工具类
    public void handleChartUpdateError(long chartId, String execMessage) {
        Chart updateChart = new Chart();
        updateChart.setId(chartId);
        updateChart.setStatus(QueueStatusEnum.FAILED.getValue());
        updateChart.setExecMessage(execMessage);
        boolean updateResult = chartService.updateById(updateChart);
        if (!updateResult) {
            log.error("更新图表状态失败" + chartId + "," + execMessage);
        }
    }
}