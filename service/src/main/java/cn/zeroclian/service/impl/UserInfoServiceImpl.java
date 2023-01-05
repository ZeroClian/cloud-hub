package cn.zeroclian.service.impl;

import cn.zeroclian.dao.UserInfoDao;
import cn.zeroclian.entity.UserInfo;
import cn.zeroclian.service.UserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @author ZeroClian
 * @date 2023-01-05 21:50
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoDao, UserInfo> implements UserInfoService {

    @Autowired
    private UserInfoDao userInfoDao;

    @Override
    public List<UserInfo> findByAge(Integer age) {
        return userInfoDao.findByAge(age);
    }

    @Override
    public List<UserInfo> findByAgeIn(List<Integer> ages) {
        return Collections.emptyList();
    }
}
