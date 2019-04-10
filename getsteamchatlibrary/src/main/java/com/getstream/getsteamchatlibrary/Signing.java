package com.getstream.getsteamchatlibrary;

import android.util.Base64;
import android.util.Log;

import org.json.JSONObject;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class Signing {

    public static String JWTUserToken(String apiSecret, String userId, String extraData, String jwtOptions) {


        Map map = new HashMap<String,Object>();
        map.put("typ","JWT");

        Date date= new Date();

        long time = date.getTime()/1000;
        String jws = Jwts.builder().claim("user_id",userId).claim("iat",time).signWith(SignatureAlgorithm.HS256,apiSecret.getBytes()).setHeader(map).compact();
        return jws;
    }

    public static String JWTServerToken(String apiSecret, String userId, String extraData, String jwtOptions) {
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

        String jws = Jwts.builder().claim("server",true).signWith(key).compact();
        return jws;
    }


    public static String DevToken(String userID){
        return "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9" + "." + encodeBase64("user_id" + ":" + userID) + "." + "devtoken";
    }

    public static String decodeBase64(String s) {

        byte[] valueDecoded = Base64.decode(s.getBytes(), Base64.DEFAULT);
        return new String(valueDecoded);
    }

    public static String encodeBase64(String s) {

        byte[] valueDecoded = Base64.encode(s.getBytes(), Base64.DEFAULT);
        return new String(valueDecoded);
    }

    public static String UserFromToken(String token) {

        String[] fragments = token.split("\\.");
        if (fragments.length != 3) {
            return "";
        }

        String b64Payload = fragments[1];
        String payload = decodeBase64(b64Payload);

        try {

            JSONObject obj = new JSONObject(payload);

            return obj.getString("user_id");

        } catch (Throwable tx) {
            Log.e("My App", "Could not parse malformed JSON: \"" + payload + "\"");
            return "";
        }


    }

}
