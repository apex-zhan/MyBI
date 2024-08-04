package generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxw.springbootinit.model.entity.User;
import generator.service.UserService;
import com.zxw.springbootinit.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author MECHREVO
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2024-06-16 21:47:05
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

}




