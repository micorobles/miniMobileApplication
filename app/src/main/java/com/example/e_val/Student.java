package com.example.e_val;

public class Student {
    private int id;
    private byte[] image;
    private String num;
    private String email;
    private String lname;
    private String fname;
    private String mname;
    private String password;
    private int isCleared;

//    public Student(byte[] image, String num, String lname) {
//        this.image = image;
//        this.num = num;
//        this.lname = lname;
//    }

    public Student( int id, byte[] image, String num, String email, String lname, String fname, String mname, String password, int isCleared) {
        this.id = id;
        this.image = image;
        this.num = num;
        this.email = email;
        this.lname = lname;
        this.fname = fname;
        this.mname = mname;
        this.password = password;
        this.isCleared = isCleared;
    }

//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
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

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public int getIsCleared() {
        return isCleared;
    }

    public void setIsCleared(int isCleared) {
        this.isCleared = isCleared;
    }
}
