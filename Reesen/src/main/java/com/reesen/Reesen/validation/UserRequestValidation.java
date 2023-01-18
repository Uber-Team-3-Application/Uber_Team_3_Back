package com.reesen.Reesen.validation;

import com.reesen.Reesen.security.jwt.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserRequestValidation {


    private final JwtTokenUtil jwtTokenUtil;


    @Autowired
    public UserRequestValidation(JwtTokenUtil jwtTokenUtil){
        this.jwtTokenUtil = jwtTokenUtil;
    }

    public Long getIdFromToken(Map<String, String> headers){
        String token = headers.get("x-auth-token");
        String refreshToken = headers.get("refreshtoken");
        Long id;
        if(isTokenExpired(token)) id = jwtTokenUtil.getId(refreshToken).longValue();

        else id = jwtTokenUtil.getId(token).longValue();

        return id;
    }
    public  String getRoleFromToken(Map<String, String> headers){
        String token = headers.get("x-auth-token");
        String refreshToken = headers.get("refreshtoken");
        List<HashMap<String, String>> role;
        if(isTokenExpired(token)){
            role = jwtTokenUtil.getRole(refreshToken);


        }else  role = jwtTokenUtil.getRole(token);

        for (String values: role.get(0).values()){
            return values;
        }
        return "";
    }

    public  boolean areIdsEqual(Map<String, String> headers, Long givenId){
        String token = headers.get("x-auth-token");
        String refreshToken = headers.get("refreshtoken");
        if(isTokenExpired(token)){
            Integer id = jwtTokenUtil.getId(refreshToken);
            return givenId.intValue() == id;

        }
        Integer id = jwtTokenUtil.getId(token);
        return givenId.intValue() == id;
    }

    public Integer getUserId(Map<String, String> headers) {
        String token = headers.get("x-auth-token");
        String refreshToken = headers.get("refreshtoken");
        if (isTokenExpired(token))
            return jwtTokenUtil.getId(refreshToken);
        else
            return jwtTokenUtil.getId(token);
    }

    private  boolean isTokenExpired(String token){
        return jwtTokenUtil.isExpired(token);
    }
}
