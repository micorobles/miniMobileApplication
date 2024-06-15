package com.example.e_val;

public class Evaluation {

    private int eval_id;
    private int stud_id;
    private int load_id;
    private float averageScore;
    private float averageEffort;
    private String comment;

    public Evaluation(int eval_id, int stud_id, int load_id, float averageScore, float averageEffort, String comment) {
        this.eval_id = eval_id;
        this.stud_id = stud_id;
        this.load_id = load_id;
        this.averageScore = averageScore;
        this.averageEffort = averageEffort;
        this.comment = comment;
    }

    public int getEval_id() {
        return eval_id;
    }

    public void setEval_id(int eval_id) {
        this.eval_id = eval_id;
    }

    public int getStud_id() {
        return stud_id;
    }

    public void setStud_id(int stud_id) {
        this.stud_id = stud_id;
    }

    public int getLoad_id() {
        return load_id;
    }

    public void setLoad_id(int load_id) {
        this.load_id = load_id;
    }

    public float getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(float averageScore) {
        this.averageScore = averageScore;
    }

    public float getAverageEffort() {
        return averageEffort;
    }

    public void setAverageEffort(float averageEffort) {
        this.averageEffort = averageEffort;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
