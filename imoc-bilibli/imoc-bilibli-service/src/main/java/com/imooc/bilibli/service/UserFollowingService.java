package com.imooc.bilibli.service;

import com.imooc.bilibli.dao.UserFollowingDao;
import com.imooc.bilibli.domain.FollowingGroup;
import com.imooc.bilibli.domain.User;
import com.imooc.bilibli.domain.UserFollowing;
import com.imooc.bilibli.domain.UserInfo;
import com.imooc.bilibli.domain.constant.UserConstant;
import com.imooc.bilibli.domain.exception.ConditionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserFollowingService {
    @Autowired
    private UserFollowingDao userFollowingDao;
    @Autowired
    private FollowingGroupService followingGroupService;
    @Autowired
    private UserService userService;

    /**
     * 添加用户关注分组
     *
     * @param userFollowing
     */
    @Transactional
    public void addUserFollowings(UserFollowing userFollowing) {
        Long groupId = userFollowing.getGroupId();
        // 如果分组id不存在，添加到默认分组中
        if (groupId == null) {
            FollowingGroup followingGroup = followingGroupService.getByType(UserConstant.USER_FOLLOWING_GROUP_TYPE_DEFAULT);
            userFollowing.setGroupId(followingGroup.getId());
        } else {
            FollowingGroup followingGroup = followingGroupService.getById(groupId);
            if (followingGroup == null) {
                throw new ConditionException("关注分组不存在！");
            }
        }
        // 获取到关注人的用户id
        Long followingID = userFollowing.getFollowingId();
        User user = userService.getUserById(followingID);
        if (user == null) {
            throw new ConditionException("关注的用户不存在！");
        }
        userFollowingDao.deleteUserFollowing(userFollowing.getUserId(), followingID);
        userFollowing.setCreateTime(new Date());
        userFollowingDao.addUserFollowing(userFollowing);
    }

    // 第一步：获取关注的用户列表
    // 第二步：根据关注用户的id查询关注用户的基本信息
    // 第三步： 将关注用户按关注分组进行分类
    public List<FollowingGroup> getUserFollowings(Long userId) {
        List<UserFollowing> list = userFollowingDao.getUserFollowings(userId);
        Set<Long> followingIdSet = list.stream().map(UserFollowing::getFollowingId).collect(Collectors.toSet());
        List<UserInfo> userInfoList = new ArrayList<>();
        if (followingIdSet.size() > 0) {
            userInfoList = userService.getUserInfoByUserIds(followingIdSet);
        }
        for (UserFollowing userFollowing : list) {
            for (UserInfo userInfo : userInfoList) {
                if (userFollowing.getFollowingId().equals(userInfo.getUserId())) {
                    userFollowing.setUserInfo(userInfo);
                }
            }
        }
        List<FollowingGroup> groups = followingGroupService.getByUserId(userId);
        FollowingGroup allGroup = new FollowingGroup();
        allGroup.setName(UserConstant.USER_FOLLOWING_GROUP_ALL_NAME);
        allGroup.setFollowingUserInfoList(userInfoList);
        List<FollowingGroup> result = new ArrayList<>();
        result.add(allGroup);
        for (FollowingGroup followingGroup : groups) {
            List<UserInfo> infoList = new ArrayList<>();
            for (UserFollowing userFollowing : list) {
                if (followingGroup.getId().equals(userFollowing.getGroupId())) {
                    infoList.add(userFollowing.getUserInfo());
                }
            }
            followingGroup.setFollowingUserInfoList(infoList);
            result.add(followingGroup);
        }
        return result;
    }

    // 第一步：获取当前用户的粉丝列表
    // 第二步：根据粉丝的用户id查询基本信息
    // 第三步：查询当前用户是否已经关注该粉丝
    public List<UserFollowing> getUserFans(Long userId) {
        List<UserFollowing> userList = userFollowingDao.getUserFans(userId);
        Set<Long> fanIdSet = userList.stream().map(UserFollowing::getUserId).collect(Collectors.toSet());
        List<UserInfo> userInfoList = new ArrayList<>();
        if (fanIdSet.size() > 0) {
            userInfoList = userService.getUserInfoByUserIds(fanIdSet);
        }
        List<UserFollowing> followingList = userFollowingDao.getUserFollowings(userId);
        for (UserFollowing fan : followingList) {
            for (UserInfo userInfo : userInfoList) {
                if(fan.getUserId().equals(userInfo.getUserId())) {
                    userInfo.setFollowed(false);
                    fan.setUserInfo(userInfo);
                }
            }
            for (UserFollowing following : followingList) {
                if(following.getFollowingId().equals(fan.getUserId())) {
                    fan.getUserInfo().setFollowed(true);
                }
            }
        }
        return followingList;
    }

    public Long addUserFollowingGroups(FollowingGroup followingGroup) {
        followingGroup.setCreateTime(new Date());
        followingGroup.setType(UserConstant.USER_FOLLOWING_GROUP_TYPE_USER);
        System.out.println(followingGroup);
        followingGroupService.addFollowingGroup(followingGroup);
        return followingGroup.getId();
    }

    public List<FollowingGroup> getUserFollowingGroups(Long userId) {
        return followingGroupService.getUserFollowingGroups(userId);
    }

    public List<UserInfo> checkFollowingStatus(List<UserInfo> list, Long userId) {
        List<UserFollowing> userFollowings = userFollowingDao.getUserFollowings(userId);
        for (UserInfo userInfo : list) {
            userInfo.setFollowed(false);
            for (UserFollowing userFollowing : userFollowings) {
                if(userFollowing.getFollowingId().equals(userInfo.getUserId())) {
                    userInfo.setFollowed(true);
                }
            }
        }
        return list;
    }
}