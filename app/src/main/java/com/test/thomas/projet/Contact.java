package com.test.thomas.projet;


import android.net.Uri;


public class Contact {

    private String _name, _phone, _email, _market;
    private int _id;
    private Uri _imageURI;

    public Contact( int id,String name, String phone, String email, String market, Uri imageUri ){
        _id = id;
        _name = name;
        _phone = phone;
        _email = email;
        _market = market;
        _imageURI = imageUri;
    }

    public int get_id() {return _id; }
    public String getName(){
        return _name;
    }
    public String getPhone(){
        return _phone;
    }
    public String getEmail(){
        return _email;
    }
    public String getMarket(){
        return _market;
    }
    public Uri getImageURI(){return _imageURI;}
}
