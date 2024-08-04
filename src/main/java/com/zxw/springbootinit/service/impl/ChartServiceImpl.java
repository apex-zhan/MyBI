package com.zxw.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxw.springbootinit.manager.AiManager;
import com.zxw.springbootinit.model.entity.Chart;
import com.zxw.springbootinit.model.enums.QueueStatusEnum;
import com.zxw.springbootinit.service.ChartService;
import com.zxw.springbootinit.mapper.ChartMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author MECHREVO
* @description 针对表【chart(图标信息表)】的数据库操作Service实现
* @createDate 2024-06-16 21:47:05
*/
@Service
public class ChartServiceImpl extends ServiceImpl<ChartMapper, Chart>
    implements ChartService{

}




