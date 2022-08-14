package com.imooc.com.imooc.bilibli.api;

import com.imooc.bilibli.domain.FollowingGroup;
import com.imooc.bilibli.domain.JsonResponse;
import com.imooc.bilibli.domain.UserFollowing;
import com.imooc.bilibli.service.UserFollowingService;
import com.imooc.com.imooc.bilibli.api.support.UserSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户分组
 */
@RestController
public class UserFollowingApi {
    @Autowired
    private UserFollowingService userFollowingService;

    @Autowired
    private UserSupport userSupport;

    /**
     * 添加用户关注
     * @param userFollowing
     * @return
     */
    @PostMapping("/user-followings")
    public JsonResponse<String> addUserFollowings(@RequestBody UserFollowing userFollowing) {
        Long userId = userSupport.getCurrentUserId();
        userFollowing.setUserId(userId);
        userFollowingService.addUserFollowings(userFollowing);
        return JsonResponse.success();
    }

    /**
     * 获取用户分组信息
     * @return
     */
    @GetMapping("/user-folowings")
    public JsonResponse<List<FollowingGroup>> getUserFollowings() {
        Long userID = userSupport.getCurrentUserId();
        List<FollowingGroup> userFollowings = userFollowingService.getUserFollowings(userID);
        return new JsonResponse<>(userFollowings);
    }

    /**
     * 获取用户粉丝
     * @return
     */
    @GetMapping("/user-fans")
    public JsonResponse<List<UserFollowing>> getUserFans() {
        Long userID = userSupport.getCurrentUserId();
        List<UserFollowing> userFollowings = userFollowingService.getUserFans(userID);
        return new JsonResponse<>(userFollowings);
    }

    /**
     * 添加用户关注分组
     * @param followingGroup
     * @return
     */
    @PostMapping("/user-following-groups")
    public JsonResponse<Long> addUserFollowingGroups(@RequestBody FollowingGroup followingGroup) {
        Long userId = userSupport.getCurrentUserId();
        followingGroup.setUserId(userId);
        Long groupId = userFollowingService.addUserFollowingGroups(followingGroup);
        return new JsonResponse<>(groupId);
    }

    /**
     * 获取到用户关注分组
     * @return
     */

    @GetMapping("/user-following-groups")
    public JsonResponse<List<FollowingGroup>> getUserFollowingGroups() {
        Long userId = userSupport.getCurrentUserId();
        List<FollowingGroup> list = userFollowingService.getUserFollowingGroups(userId);
        return new JsonResponse<>(list);
    }
}
