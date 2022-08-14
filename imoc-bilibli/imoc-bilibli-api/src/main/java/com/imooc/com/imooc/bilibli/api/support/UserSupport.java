package com.imooc.com.imooc.bilibli.api.support;

import com.imooc.bilibli.domain.exception.ConditionException;
import com.imooc.bilibli.service.util.TokenUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 获取用户id以及请求头的token
 */
@Component
public class UserSupport {
    public Long getCurrentUserId() {
        // 获取请求头
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        // 获取请求头中的token字段
        String token = requestAttributes.getRequest().getHeader("token");
        Long userID = TokenUtil.verifyToken(token);
        if (userID < 0) {
            throw new ConditionException("非法用户！");
        }
        return userID;
    }
}
