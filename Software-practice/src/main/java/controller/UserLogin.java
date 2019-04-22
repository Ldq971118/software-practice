package controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mysql.cj.Session;
import com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithm;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.jws.Oneway;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/api/web/user")
public class UserLogin {

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    private static final String TOKEN_SECRET = "echo";
    private static final long EXPIRE_TIME = 1000 * 60 * 60 * 24 * 3;

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public void Login(HttpServletRequest request,HttpServletResponse response){
        String username=request.getParameter("username");
        String passwd=request.getParameter("password");
        SqlSession session=sqlSessionFactory.openSession();

    }

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
