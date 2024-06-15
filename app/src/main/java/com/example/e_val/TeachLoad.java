package com.example.e_val;

public class TeachLoad {
    private int load_ID;
    private int prof_ID;
    private int subject_ID;


    public TeachLoad(int load_ID, int prof_ID, int subject_ID) {
        this.load_ID = load_ID;
        this.prof_ID = prof_ID;
        this.subject_ID = subject_ID;
    }

    public int getLoad_ID() {
        return load_ID;
    }

    public void setLoad_ID(int load_ID) {
        this.load_ID = load_ID;
    }

    public int getProf_ID() {
        return prof_ID;
    }

    public void setProf_ID(int prof_ID) {
        this.prof_ID = prof_ID;
    }

    public int getSubject_ID() {
        return subject_ID;
    }

    public void setSubject_ID(int subject_ID) {
        this.subject_ID = subject_ID;
    }
}
