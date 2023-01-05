package cn.zeroclian.controller;

import cn.zeroclian.entity.UserInfo;
import cn.zeroclian.query.PageQuery;
import cn.zeroclian.service.UserInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

/**
 * Mybatis-Plus 使用案例
 *
 * @author ZeroClian
 * @date 2023-01-05 21:52
 */
@RestController
@RequestMapping("/user_info")
public class UserInfoController {
    @Autowired
    private UserInfoService userInfoService;

    @GetMapping("/{userId}")
    public UserInfo getInfo(@PathVariable String userId) {
        return userInfoService.getById(userId);
    }

    @GetMapping("/list")
    public List<UserInfo> getInfoList() {
        return userInfoService.list();
    }


    @PostMapping("/list")
    public Collection<UserInfo> getInfoListByQuery(@RequestBody PageQuery query) {
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<UserInfo>().like("name", query.getName());
        return userInfoService.list(queryWrapper);
    }

    @GetMapping("/page")
    public IPage<UserInfo> getUserInfoPage(@RequestBody PageQuery query) {
        IPage<UserInfo> page = new Page<>();
        page.setCurrent(query.page);
        page.setSize(query.size);
        return userInfoService.page(page);
    }

    @PostMapping("/page")
    public IPage<UserInfo> getUserInfoPageByQuery(@RequestBody PageQuery query) {
        IPage<UserInfo> page = new Page<>();
        page.setCurrent(query.page);
        page.setSize(query.size);
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().like(UserInfo::getName, query.getName());
        return userInfoService.page(page, queryWrapper);
    }

    @GetMapping("/age")
    public List<UserInfo> getByAge(@RequestParam("age") Integer age) {
        return userInfoService.findByAge(age);
    }

    @GetMapping("/ages")
    public List<UserInfo> getByAge(@RequestParam("ages") List<Integer> ages) {
        return userInfoService.findByAgeIn(ages);
    }
}
