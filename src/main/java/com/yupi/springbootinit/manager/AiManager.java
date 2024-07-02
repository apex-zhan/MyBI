package com.yupi.springbootinit.manager;

import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.exception.BusinessException;
import com.yupi.yucongming.dev.client.YuCongMingClient;
import com.yupi.yucongming.dev.common.BaseResponse;
import com.yupi.yucongming.dev.model.DevChatRequest;
import com.yupi.yucongming.dev.model.DevChatResponse;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 用于对接 AI 平台
 */
@Service
public class AiManager {
    @Resource
    private YuCongMingClient client;

    /**
     * AI 对话
     * @param message
     * @return
     */
    public String doChat(Long moduleId,String message) {
        //构造请求
        DevChatRequest devChatRequest = new DevChatRequest();
        // 模型id，尾后加L，转成long类型
//        devChatRequest.setModelId(1659920671007834113L);
        devChatRequest.setModelId(moduleId);
        devChatRequest.setMessage(message);
        //返回结果
        if (client == null){
            throw  new BusinessException(ErrorCode.SYSTEM_ERROR, "AI 响应错误");
        }
        BaseResponse<DevChatResponse> response = client.doChat(devChatRequest);
        return response.getData().getContent();
    }
}
