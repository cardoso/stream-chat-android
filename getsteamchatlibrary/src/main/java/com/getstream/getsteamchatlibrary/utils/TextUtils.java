package com.getstream.getsteamchatlibrary.utils;

import com.getstream.getsteamchatlibrary.Channel;
import com.getstream.getsteamchatlibrary.Member;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class TextUtils {
    public static String getGroupChannelTitle(Channel channel) {
        List<Member> members = channel.getMembers();

        if (members.size() < 2) {
            return "No Members";
        } else if (members.size() == 2) {
            StringBuffer names = new StringBuffer();
            for (Member member : members) {


                names.append(", " + member.getNickname());
            }
            return names.delete(0, 2).toString();
        } else {
            int count = 0;
            StringBuffer names = new StringBuffer();
            for (Member member : members) {


                count++;
                names.append(", " + member.getNickname());

                if(count >= 10) {
                    break;
                }
            }
            return names.delete(0, 2).toString();
        }
    }

    /**
     * Calculate MD5
     * @param data
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String generateMD5(String data) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("MD5");
        digest.update(data.getBytes());
        byte messageDigest[] = digest.digest();

        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < messageDigest.length; i++)
            hexString.append(Integer.toHexString(0xFF & messageDigest[i]));

        return hexString.toString();
    }
}
