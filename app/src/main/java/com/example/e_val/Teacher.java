package com.example.e_val;

public class Teacher {

    private int id;
    private byte[] image;
    private String email;
    private String position;
    private String lname;
    private String fname;
    private String mname;
    private String password;

    public Teacher(int id, byte[] image, String email, String position, String lname, String fname, String mname, String password) {
        this.id = id;
        this.image = image;
        this.email = email;
        this.position = position;
        this.lname = lname;
        this.fname = fname;
        this.mname = mname;
        this.password = password;
    }
//    public Teacher(byte[] image, String email, String lname) {
//        this.image = image;
//        this.email = email;
//        this.lname = lname;
//    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
