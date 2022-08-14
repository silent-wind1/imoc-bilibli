package com.imooc.bilibli.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
