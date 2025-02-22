package com.zxw.springbootinit.manager;

import com.tencentcloudapi.common.AbstractModel;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.lkeap.v20240522.models.ChatCompletionsRequest;
import com.tencentcloudapi.lkeap.v20240522.models.ChatCompletionsResponse;
import com.tencentcloudapi.lkeap.v20240522.models.Message;
import com.zxw.springbootinit.common.ErrorCode;
import com.zxw.springbootinit.config.DeepSeekClientConfig;
import com.zxw.springbootinit.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 用于对接 AI 平台
 */
@Service
@Slf4j
public class AiManager {
    @Resource
    private DeepSeekClientConfig deepSeekClientConfig;


    /**
     * AI 对话
     *
     * @param modelId
     * @param message
     * @return
     */
    public String doChat(long modelId, String message) {
        // 系统预设
        final String SYSTEM_PROMPT = "你是一个数据分析师和前端开发专家，接下来我会按照以下固定格式给你提供内容：" +
                "分析需求：" +
                "{数据分析的需求或者目标}" +
                "原始数据：" +
                "{csv格式的原始数据，用,作为分隔符}" +
                "请根据这两部分内容，按照以下指定格式生成内容（此外不要输出任何多余的开头、结尾、注释）" +
                "【【【【【" +
                "{前端 Echarts V5 的 option 配置对象代码，合理地将数据进行可视化，并且是严格的json格式，不要生成任何多余的内容，比如注释和单引号}" +
                "【【【【【" +
                "{明确的数据分析结论、越详细越好，不要生成多余的注释和单引号}";
        try {
            // 实例化一个请求对象,每个接口都会对应一个request对象
            ChatCompletionsRequest req = new ChatCompletionsRequest();
            req.setModel("deepseek-v3");
            req.setStream(false);

            //信息
            Message[] messages = new Message[2];

            Message message0 = new Message();
            message0.setRole("system");
            message0.setContent(SYSTEM_PROMPT);
            messages[0] = message0;


            Message message1 = new Message();
            message1.setRole("user");
            message1.setContent(message);
            messages[1] = message1;

            req.setMessages(messages);

            // 返回的resp是一个ChatCompletionsResponse的实例，与请求对象对应
            ChatCompletionsResponse resp = deepSeekClientConfig.deepSeekClient().ChatCompletions(req);
            // 输出json格式的字符串回包
//            if (resp.isStream()) { // 流式响应
//                for (SSEResponseModel.SSE e : resp) {
//                    System.out.println(e.Data);
//                }
//            } else { // 非流式响应
            return AbstractModel.toJsonString(resp);
        } catch (TencentCloudSDKException e) {
            e.printStackTrace();
            log.error("AI对话失败", e);
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "AI对话失败");
        }
    }
}