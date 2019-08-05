package com.getstream.sdk.chat.rest;

import com.getstream.sdk.chat.model.Device;
import com.getstream.sdk.chat.model.Mute;
import com.getstream.sdk.chat.utils.Global;
import com.getstream.sdk.chat.utils.StringUtility;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;

/**
 * A user
 */

public class User {
    @SerializedName("id")
    String id;

    @SerializedName("name")
    String name;

    @SerializedName("image")
    String image;

    @SerializedName("role")
    String role;

    @SerializedName("created_at")
    String created_at;

    @SerializedName("updated_at")
    String updated_at;

    @SerializedName("last_active")
    String last_active;

    @SerializedName("online")
    Boolean online;

    @SerializedName("invisible")
    Boolean invisible;

    @SerializedName("devices")
    List<Device> devices;

    @SerializedName("mutes")
    List<Mute> mutes;

    @SerializedName("unread_count")
    int unread_count;

    @SerializedName("total_unread_count")
    int total_unread_count;

    @SerializedName("unread_channels")
    int unread_channels;

    HashMap<String, Object> extraData;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getLast_active() {
        return last_active;
    }

    public void setLast_active(String last_active) {
        this.last_active = last_active;
    }

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public Boolean getInvisible() {
        return invisible;
    }

    public void setInvisible(Boolean invisible) {
        this.invisible = invisible;
    }

    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }

    public List<Mute> getMutes() {
        return mutes;
    }

    public void setMutes(List<Mute> mutes) {
        this.mutes = mutes;
    }

    public int getUnread_count() {
        return unread_count;
    }

    public void setUnread_count(int unread_count) {
        this.unread_count = unread_count;
    }

    public int getTotal_unread_count() {
        return total_unread_count;
    }

    public void setTotal_unread_count(int total_unread_count) {
        this.total_unread_count = total_unread_count;
    }

    public int getUnread_channels() {
        return unread_channels;
    }

    public void setUnread_channels(int unread_channels) {
        this.unread_channels = unread_channels;
    }

    /**
     * Constructor
     * @param id User id
     * */
    public User(String id) {
        this.id = id;
        this.online = false;
        this.extraData = new HashMap<>();
    }

    /**
    * Constructor
    * @param id User id
    * @param extraData Custom user fields (ie: name, image, anything that json can serialize is ok)
    * */
    public User(String id, HashMap<String,Object> extraData) {
        this.id = id;
        this.online = false;

        if (extraData == null) {
            this.extraData = new HashMap<>();
        } else {
            this.extraData = new HashMap<>(extraData);
        }

        // since name and image are very common fields, we are going to promote them as
        Object image = this.extraData.remove("image");
        if (image != null) {
            this.image = image.toString();
        }

        Object name = this.extraData.remove("name");
        if (name != null) {
            this.name = name.toString();
        }

        this.extraData.remove("id");
    }

    public HashMap<String, Object> getExtraData() {
        return extraData;
    }

    public boolean isMe(){
        return id.equals(Global.client.user.getId());
    }

    public String getUserInitials() {
        if (this.name == null) {
            this.name = "";
        }
        String name = this.name;
        String[] names = name.split(" ");
        String firstName = names[0];
        String lastName = null;
        try {
            lastName = names[1];
        } catch (Exception e) {
        }

        if (!StringUtility.isNullOrEmpty(firstName) && StringUtility.isNullOrEmpty(lastName))
            return firstName.substring(0, 1).toUpperCase();
        if (StringUtility.isNullOrEmpty(firstName) && !StringUtility.isNullOrEmpty(lastName))
            return lastName.substring(0, 1).toUpperCase();

        if (!StringUtility.isNullOrEmpty(firstName) && !StringUtility.isNullOrEmpty(lastName))
            return firstName.substring(0, 1).toUpperCase() + lastName.substring(0, 1).toUpperCase();
        return null;
    }
}
