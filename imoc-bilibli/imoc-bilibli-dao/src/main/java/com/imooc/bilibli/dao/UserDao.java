package com.imooc.bilibli.dao;

import com.alibaba.fastjson.JSONObject;
import com.imooc.bilibli.domain.User;
import com.imooc.bilibli.domain.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Mapper
public interface UserDao {
    User getUserByPhone(String phone);

    Integer addUser(User user);

    Integer addUserInfo(UserInfo userInfo);

    User getUserById(Long id);

    UserInfo getUserInfoByUserId(Long userId);

    Integer updateUsers(User user);

    User getUserByPhoneOrEmail(@Param("phone") String phone, @Param("email") String email);

    Integer updateUserInfos(UserInfo userInfo);

    List<UserInfo> getUserINfoBYUSerIds(Set<Long> userIdList);

    Integer pageCountUserInfos(Map<String, Object> params);

    List<UserInfo> pageListUserInfos(JSONObject params);
}
