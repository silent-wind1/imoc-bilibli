package com.imooc.bilibli.domain;

import lombok.Data;

import java.util.Date;

/**
 * 用户关注
 */
@Data
public class UserFollowing {
    private Long id;
    private Long userId;
    private Long followingId;
    private Long groupId;
    private Date createTime;
    private UserInfo userInfo;
}
