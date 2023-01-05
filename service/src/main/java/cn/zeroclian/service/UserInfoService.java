package cn.zeroclian.service;

import cn.zeroclian.entity.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author ZeroClian
 * @date 2023-01-05 21:49
 */
public interface UserInfoService extends IService<UserInfo> {

    List<UserInfo> findByAge(Integer age);

    List<UserInfo> findByAgeIn(List<Integer> ages);
}
