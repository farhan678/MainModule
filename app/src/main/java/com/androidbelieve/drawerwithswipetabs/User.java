package com.androidbelieve.drawerwithswipetabs;

/**
 * Created by Maslin-Android on 5/19/2016.
 */
public class User {


    int id;
    String _name;
    String _email;
    String _contact_number;

    public User() {

    }

    public User(int id, String name, String email, String _contact_number)
    {
        this.id = id;
        this._name = name;
        this._email = email;
        this._contact_number  = _contact_number;
    }

    public User(String name, String email, String _contact_number) {
        this._name = name;
        this._email = email;
        this._contact_number = _contact_number;
    }

    public int getID()
    {
        return this.id;
    }
    public void setID(int id){
        this.id = id;
    }
    public String getName(){
        return this._name;
    }
    public void setName(String name){
        this._name = name;
    }
    public String getEmail(){
        return this._email;
    }
    public void setEmail(String email){
        this._email = email;
    }
    public String getcontactNumber(){
        return this._contact_number;
    }
    public void setPhoneNumber(String contact_number){
        this._contact_number = contact_number;
    }

}



