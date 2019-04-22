package com.example.softwarepractice.function;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Token {
    private static final String TOKEN_SECRET = "echo";
    private static final long EXPIRE_TIME = 1000 * 60 * 60 * 24 * 3;

    public static String createToken(String username,String password) {
        //过期时间
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        //秘钥级加密算法
        Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
        //设置头部信息
        Map<String, Object> header = new HashMap<String, Object>();
        header.put("alg", "HS256");
        header.put("type", "JWT");

        Map<String,String> claim=new HashMap<String, String>();
        claim.put("username",username);
        claim.put("password",password);

        //生成签名
        return JWT.create()
                .withHeader(header)
                .withClaim("username",username)
                .withClaim("password",password)
                .withExpiresAt(date)
                .sign(algorithm);
    }

    public static boolean varify(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
