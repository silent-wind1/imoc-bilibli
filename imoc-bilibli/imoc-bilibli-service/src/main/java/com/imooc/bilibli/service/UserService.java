package com.imooc.bilibli.service;

import com.alibaba.fastjson.JSONObject;
import com.imooc.bilibli.dao.UserDao;
import com.imooc.bilibli.domain.PageResult;
import com.imooc.bilibli.domain.User;
import com.imooc.bilibli.domain.UserInfo;
import com.imooc.bilibli.domain.constant.UserConstant;
import com.imooc.bilibli.domain.exception.ConditionException;
import com.imooc.bilibli.service.util.MD5Util;
import com.imooc.bilibli.service.util.RSAUtil;
import com.imooc.bilibli.service.util.TokenUtil;
import com.mysql.cj.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;


    /**
     * 用户注册
     *
     * @param user
     */
    public void addUser(User user) {
        String phone = user.getPhone();
        if (StringUtils.isNullOrEmpty(phone)) {
            throw new ConditionException("手机号不能为空！");
        }
        User dbUser = this.getUserByPhone(phone);
        if (dbUser != null) {
            throw new ConditionException("该手机号已经注册");
        }

        Date now = new Date();

        String salt = String.valueOf(now.getTime());
        String password = user.getPassword();
        String rawPassword;

        try {
            rawPassword = RSAUtil.decrypt(password);
            System.out.println(rawPassword);
        } catch (Exception e) {
            throw new ConditionException("密码解密失败！");
        }

        String md5Password = MD5Util.sign(rawPassword, salt, "UTF-8");
        user.setSalt(salt);
        user.setPassword(md5Password);
        user.setCreateTime(now);
        userDao.addUser(user);

        // 添加用户信息
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(user.getId());
        userInfo.setNick(UserConstant.DEFAULT_NICK);
        userInfo.setBirth(UserConstant.DEFAULT_BIRTH);
        userInfo.setGender(UserConstant.GENDER_FEMALE);
        userInfo.setCreateTime(now);
        userDao.addUserInfo(userInfo);
    }

    /**
     * 获取用户手机号
     *
     * @param phone
     */
    public User getUserByPhone(String phone) {
        return userDao.getUserByPhone(phone);
    }

    /**
     * 用户登录
     *
     * @param user
     * @return
     */
    public String login(User user) throws Exception {
        String phone = user.getPhone() == null ? "" : user.getPhone();
        String email = user.getEmail() == null ? "" : user.getEmail();
        System.out.println("phone = " + phone + "\temail = " + email);
        if (StringUtils.isNullOrEmpty(phone) && StringUtils.isNullOrEmpty(email)) {
            throw new ConditionException("参数异常！");
        }
        User dbUser = null;
        try {
            dbUser = userDao.getUserByPhoneOrEmail(phone, email);
        } catch (Exception e) {
            throw new ConditionException("phone， email错误");
        }

        if (dbUser == null) {
            throw new ConditionException("当前用户不存在");
        }
        String password = user.getPassword();
        String rawPassword;
        try {
            rawPassword = RSAUtil.decrypt(password);
        } catch (Exception e) {
            throw new ConditionException("密码解密失败");
        }
        String salt = dbUser.getSalt();
        String md5Password = MD5Util.sign(rawPassword, salt, "UTF-8");
        if (!md5Password.equals(dbUser.getPassword())) {
            throw new ConditionException("密码错误");
        }
        return TokenUtil.generateToken(dbUser.getId());
    }

    public User getUserInfo(Long userId) {
        User user = userDao.getUserById(userId);
        UserInfo userInfo = userDao.getUserInfoByUserId(userId);
        user.setUserInfo(userInfo);
        return user;
    }

    public void updateUsers(User user) throws Exception {
        Long id = user.getId();
        User dbUser = userDao.getUserById(id);
        if (dbUser == null) {
            throw new ConditionException("用户不存在！");
        }
        if (!StringUtils.isNullOrEmpty(user.getPassword())) {
            String rawPassword = RSAUtil.decrypt(user.getPassword());
            String md5Password = MD5Util.sign(rawPassword, dbUser.getSalt(), "UTF-8");
            user.setPassword(md5Password);
        }
        user.setUpdateTime(new Date());
        userDao.updateUsers(user);
    }

    public void updateUsersInfos(UserInfo userInfo) {
        userInfo.setUpdateTime(new Date());
        userDao.updateUserInfos(userInfo);
    }

    public User getUserById(Long followingID) {
        return userDao.getUserById(followingID);
    }

    public List<UserInfo> getUserInfoByUserIds(Set<Long> userIdList) {
        return userDao.getUserINfoBYUSerIds(userIdList);
    }

    public PageResult<UserInfo> pageListUserInfos(JSONObject params) {
        Integer no = params.getInteger("no");
        Integer size = params.getInteger("size");
        params.put("star", (no - 1) * size);
        params.put("limit", size);
        Integer total = userDao.pageCountUserInfos(params);
        List<UserInfo> list = new ArrayList<>();
        if (total > 0) {
            list = userDao.pageListUserInfos(params);
        }
        return new PageResult<>(total, list);
    }
}
