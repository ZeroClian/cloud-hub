package cn.zeroclian.dao;

import cn.zeroclian.entity.UserInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author ZeroClian
 * @date 2023-01-05 21:48
 */
@Component
public interface UserInfoDao extends BaseMapper<UserInfo> {

    @Select("select * from user_info where age = #{age}")
    List<UserInfo> findByAge(@Param("age") Integer age);

}
