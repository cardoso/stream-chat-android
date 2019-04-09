package com.getstream.getsteamchatlibrary;

import android.util.Base64;
import android.util.Log;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class Signing {

    public static String JWTUserToken(String apiSecret, String userId, String extraData, String jwtOptions) {

        byte[] apiKeySecretBytes = Base64.decode("MTA1NDQ2NDYyMjkxODQ3NjI0NjM4NjUxNTYxZGZnMTU2MTQ4ZGY5NDE4MTk0OTg=",Base64.DEFAULT);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());

//        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

//        Key JWT = Keys.secretKeyFor(SignatureAlgorithm.JWT);

        Map map = new HashMap<String,Object>();
        map.put("typ","JWT");

        Date date= new Date();

        long time = date.getTime()/1000;
        String jws = Jwts.builder().claim("user_id",userId).claim("iat",time).signWith(signingKey).setHeader(map).compact();
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

//        String json = "";
//
////        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
//
//        try {
//            json = ow.writeValueAsString(payload);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }

        try {

            JSONObject obj = new JSONObject(payload);

            return obj.getString("user_id");

        } catch (Throwable tx) {
            Log.e("My App", "Could not parse malformed JSON: \"" + payload + "\"");
            return "";
        }


    }

}
