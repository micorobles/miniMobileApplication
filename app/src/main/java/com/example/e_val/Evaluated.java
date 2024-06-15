package com.example.e_val;

public class Evaluated {

    private int evaluated_id;
    private int stud_id;
    private int teachLoad_id;
    private int prof_id;
    private byte[] profImage;
    private String profName;
    private int subj_id;
    private String subjName;
    private Float aveScore;
    private Float aveEffort;
    private String comment;

    public Evaluated(int evaluated_id, int stud_id, int teachLoad_id, int prof_id, byte[] profImage,
                     String profName, int subj_id, String subjName, Float aveScore, Float aveEffort, String comment) {
        this.evaluated_id = evaluated_id;
        this.stud_id = stud_id;
        this.teachLoad_id = teachLoad_id;
        this.prof_id = prof_id;
        this.profImage = profImage;
        this.profName = profName;
        this.subj_id = subj_id;
        this.subjName = subjName;
        this.aveScore = aveScore;
        this.aveEffort = aveEffort;
        this.comment = comment;
    }

    public int getEvaluated_id() {
        return evaluated_id;
    }

    public void setEvaluated_id(int evaluated_id) {
        this.evaluated_id = evaluated_id;
    }

    public int getStud_id() {
        return stud_id;
    }

    public void setStud_id(int stud_id) {
        this.stud_id = stud_id;
    }

    public int getTeachLoad_id() {
        return teachLoad_id;
    }

    public void setTeachLoad_id(int teachLoad_id) {
        this.teachLoad_id = teachLoad_id;
    }

    public int getProf_id() {
        return prof_id;
    }

    public void setProf_id(int prof_id) {
        this.prof_id = prof_id;
    }

    public byte[] getProfImage() {
        return profImage;
    }

    public void setProfImage(byte[] profImage) {
        this.profImage = profImage;
    }

    public String getProfName() {
        return profName;
    }

    public void setProfName(String profName) {
        this.profName = profName;
    }

    public int getSubj_id() {
        return subj_id;
    }

    public void setSubj_id(int subj_id) {
        this.subj_id = subj_id;
    }

    public String getSubjName() {
        return subjName;
    }

    public void setSubjName(String subjName) {
        this.subjName = subjName;
    }

    public Float getAveScore() {
        return aveScore;
    }

    public void setAveScore(Float aveScore) {
        this.aveScore = aveScore;
    }

    public Float getAveEffort() {
        return aveEffort;
    }

    public void setAveEffort(Float aveEffort) {
        this.aveEffort = aveEffort;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
