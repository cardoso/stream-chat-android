package com.getstream.getsteamchatlibrary;

import android.util.Log;
import android.widget.Toast;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Signing {

    public static String JWTUserToken(String apiSecret, String userId, String extraData, String jwtOptions) {


        try {
            Algorithm algorithm = Algorithm.HMAC256("secret");
            String token = JWT.create()
                    .withIssuer("auth0")
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException exception){
            //Invalid Signing configuration / Couldn't convert Claims.
            return "";
        }

    }

    public static String JWTServerToken(String apiSecret, String userId, String extraData, String jwtOptions) {


        try {
            Algorithm algorithm = Algorithm.HMAC256("secret");
            String token = JWT.create()
                    .withIssuer("auth0")
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException exception){
            //Invalid Signing configuration / Couldn't convert Claims.
            return "";
        }

    }


    public static String DevToken(String userID){
        return "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9" + "." + encodeBase64("user_id" + ":" + userID) + "." + "devtoken";
    }

    public static String decodeBase64(String s) {

        byte[] valueDecoded = Base64.decodeBase64(s.getBytes());
        return new String(valueDecoded);
    }

    public static String encodeBase64(String s) {

        byte[] valueDecoded = Base64.encodeBase64(s.getBytes());
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
