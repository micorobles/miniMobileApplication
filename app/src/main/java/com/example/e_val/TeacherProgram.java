package com.example.e_val;

public class TeacherProgram {
    private int ProgramID;
    private String ProfName;
    private String SubjName;

    public TeacherProgram(int programID, String profName, String subjName) {
        ProgramID = programID;
        ProfName = profName;
        SubjName = subjName;
    }

    public int getProgramID() {
        return ProgramID;
    }

    public void setProgramID(int programID) {
        this.ProgramID = programID;
    }

    public String getProfName() {
        return ProfName;
    }

    public void setProfName(String profName) {
        ProfName = profName;
    }

    public String getSubjName() {
        return SubjName;
    }

    public void setSubjName(String subjName) {
        SubjName = subjName;
    }
}
