package com.imooc.bilibli.service.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.imooc.bilibli.domain.exception.ConditionException;

import java.util.Calendar;
import java.util.Date;

public class TokenUtil {
    private static final String ISSUER = "签发者";

    /**
     * 生成token
     * @param userId
     * @return
     * @throws Exception
     */
    public static String generateToken(Long userId) throws Exception {
        // 设置一种算法,进行加密
        Algorithm algorithm = Algorithm.RSA256(RSAUtil.getPublicKey(), RSAUtil.getPrivateKey());
        Calendar calendar = Calendar.getInstance();
        // 获取当前时间并注入
        calendar.setTime(new Date());
        // 设置token30秒后过期
        calendar.add(Calendar.SECOND, 30000000);
        // 创建一个JWT并且返回token
        return JWT.create().withKeyId(String.valueOf(userId))
                .withIssuer(ISSUER)
                .withExpiresAt(calendar.getTime())
                .sign(algorithm);
    }

    /**
     * 验证用户token
     * @param token
     * @return
     */
    public static Long verifyToken(String token)  {
        try {
            Algorithm algorithm = Algorithm.RSA256(RSAUtil.getPublicKey(), RSAUtil.getPrivateKey());
            // 创建验证对象
            JWTVerifier verifier = JWT.require(algorithm).build();
            // 验证token
            DecodedJWT jwt = verifier.verify(token);
            String userId = jwt.getKeyId();
            jwt.getClaim("name").asString();
            return Long.valueOf(userId);
        } catch (TokenExpiredException e) {
            throw new ConditionException("555", "token过期！");
        }catch (Exception e){
            throw new ConditionException("非法用户token！");
        }
    }
}
