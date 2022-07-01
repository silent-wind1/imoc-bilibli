package com.imooc.bilibli.domain;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 用户关注分组表
 */
@Data
public class FollowingGroup {
    private Long id;
    private Long userId;
    private String name;
    private String type;
    private Date creteTime;
    private Date updateTime;
    private List<UserInfo> followingUserInfoList;

}
