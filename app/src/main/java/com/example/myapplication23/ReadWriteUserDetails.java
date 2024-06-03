package com.example.myapplication23;

public class ReadWriteUserDetails {
    public ReadWriteUserDetails(){};
    public String  doB, gender,mobile;
    //Firebase RealtimeDatabase'de gösterilecek olan kullanıcı özellik değişkenleri
    public ReadWriteUserDetails( String textDoB, String textGender, String textMobile){


        this.doB=textDoB;

        this.gender=textGender;

        this.mobile=textMobile;

    }
}
