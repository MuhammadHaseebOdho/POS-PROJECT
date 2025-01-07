package com.user.configuration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private final String SECRET_KEY="3e163adb0264a5765118178771efc6d64d438e2133342845d18ad6604cfa73666c77c555e3e9ea1f224bbcf1324b33eb6995fd69a920091d7172389b03621b7ed2c60b23ee42ed2752b603b492a90c61c80db0bc1c2e9b446e922de39301f59ee1e188794c7a11284f6001a3da8edc3588fb13409b188fcb3e3089b46ac0899c5c595a83105cf9d813e46a98f415d6076276a75b5d98fd5ead37db3979d67fc97ce876b906795bb68bcc891da31880f6ff092d2712a361cb344e878a2488cca7be84c199f2ace0689e791fcfbf4a9c3372a6453017b2e49f1b0265a8fad9185281c734cb0d2647adaaaa75b48459553e3b84a1ad84d6c3d7d57443a08ab3afd2a87f4a2a3f854d793d38e69711b492ce95da607939f9c353addeebf6535fe1b19e19711dad9dbdfd290f8c29e9b6fad8e8e83831c2f175dddd10c42ae1571ed0";

    private final int tokenTime=1000*60*60;

    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims,T> claimsTFunction) {
        final Claims claims=extractAllClaims(token);
        return claimsTFunction.apply(claims);

    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token,Claims::getExpiration);
    }

    public String generateToken(UserDetails userDetails){
        Map<String,Object> claims=new HashMap<>();
        return createToken(claims,userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String username) {
        return Jwts.builder().setClaims(claims).setSubject(username).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+tokenTime))
                .signWith(SignatureAlgorithm.HS256,SECRET_KEY).compact();
    }

    public Boolean validateToken(String token,UserDetails userDetails){
        final String username=extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

}
