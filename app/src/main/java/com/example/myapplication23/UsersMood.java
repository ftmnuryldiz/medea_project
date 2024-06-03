package com.example.myapplication23;


public class UsersMood {

    public String mod;
   public String date;

   public UsersMood (){

   }

    public UsersMood(String mod, String date) {
        this.mod = mod;
        this.date = date;
    }

    public String getMod() {
        return mod;
    }

    public void setMod(String mod) {
        this.mod = mod;



    }

    public String getDate() {
      return date;
    }

    public void setDate( String date) {
        this.date = date;

    }
}
