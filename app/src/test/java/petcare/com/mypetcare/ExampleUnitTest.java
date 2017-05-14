package petcare.com.mypetcare;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;

import java.security.Key;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import petcare.com.mypetcare.Util.HttpConn;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void HttpConnectionTest() throws Exception {
        HttpConn test = new HttpConn();
        test.execute();
    }

    @Test
    public void addition_isCorrect() throws Exception {
        String url = "http://220.73.175.100:8080/MPMS/mob/auth.service";
        ObjectMapper obj = new ObjectMapper();
        Key key = MacProvider.generateKey();
        String payload = "{\"test\":\"testval\"}";
        String compactJws = Jwts.builder()
                .setSubject("test")
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
        System.out.println(compactJws);
        System.out.println(compactJws);
    }
//    private String createJWT(String id, String issuer, String audience, long ttlMillis) {
//
//        //The JWT signature algorithm we will be using to sign the token
//        long nowMillis = System.currentTimeMillis();
//        Date now = new Date(nowMillis);
//
//        //We will sign our JWT with our ApiKey secret
//        //Let's set the JWT Claims
//        JwtBuilder builder = Jwts.builder().setId(id)
//                .setIssuedAt(now)
//                .setIssuer(issuer)
//                .setAudience(audience)
//                .signWith(SignatureAlgorithm.HS256, apiKeySecretBytes);
//
//        //if it has been specified, let's add the expiration
//        if (ttlMillis >= 0) {
//            long expMillis = nowMillis + ttlMillis;
//            Date exp = new Date(expMillis);
//            builder.setExpiration(exp);
//        }
//
//        //Builds the JWT and serializes it to a compact, URL-safe string
//        return builder.compact();
//    }
}