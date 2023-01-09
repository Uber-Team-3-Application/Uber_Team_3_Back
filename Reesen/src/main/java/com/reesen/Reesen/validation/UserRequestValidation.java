package com.reesen.Reesen.validation;

import com.reesen.Reesen.security.jwt.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public  String getRoleFromToken(Map<String, String> headers){
        String token = headers.get("x-auth-token");
        String refreshToken = headers.get("refreshtoken");
        List<HashMap<String, String>> role = null;
        if(!isTokenExpired(token)){
            role = jwtTokenUtil.getRole(token);

        }else role = jwtTokenUtil.getRole(refreshToken);

        for (String values: role.get(0).values()){
            return values;
        }
        return "";
    }

    public  boolean areIdsEqual(Map<String, String> headers, Long givenId){
        String token = headers.get("x-auth-token");
        String refreshToken = headers.get("refreshtoken");
        if(!isTokenExpired(token)){
            Integer id = jwtTokenUtil.getId(token);
            if(givenId.intValue() == id) return true;

        }else{
            Integer id = jwtTokenUtil.getId(refreshToken);
            if(givenId.intValue() == id) return true;
        }
        return false;
    }

    private  boolean isTokenExpired(String token){
        return jwtTokenUtil.isExpired(token);
    }
}
