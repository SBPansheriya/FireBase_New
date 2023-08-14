package com.kmsoft.firebase_new;

import android.text.TextUtils;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class Users {

    String uid;
    String name;
    String number;
    String img;

    public Users(String uid, String name, String number, String img) {
        this.uid = uid;
        this.name = name;
        this.number = number;
        this.img = img;
    }

    public Users() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Users getClassFromDocumentSnapshot(DocumentSnapshot documentSnapshot) {

        Users users = new Users();

        String name = "";
        name = documentSnapshot.getString("name");

        if (!TextUtils.isEmpty(name)) {
            users.setName(name);
        }

        String number = "";
        number = documentSnapshot.getString("number");

        if (!TextUtils.isEmpty(number)) {
            users.setNumber(number);
        }

        String id = "";
        id = documentSnapshot.getString("uid");

        if (!TextUtils.isEmpty(id)) {
            users.setUid(id);
        }

        String image = "";
        image = documentSnapshot.getString("img");

        if (!TextUtils.isEmpty(image)) {
            users.setImg(image);
        }
        return users;
    }

    public Map<String, Object> getHashMap(Users users) {

        Map<String, Object> map = new HashMap<>();

        map.put("name", users.getName());
        map.put("number", users.getNumber());
        map.put("uid", users.getUid());
        map.put("img",users.getImg());

        return map;
    }
}
