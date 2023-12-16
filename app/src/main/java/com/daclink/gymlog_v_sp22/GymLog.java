package com.daclink.gymlog_v_sp22;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.daclink.gymlog_v_sp22.DB.AppDataBase;

import java.util.Date;

@Entity(tableName = AppDataBase.GYMLOG_TABLE)
public class GymLog {

    @PrimaryKey(autoGenerate = true)
    private int mLogId;

    private String mExercise;
    private double mWeight;
    private int mReps;

    private Date mDate;

    private int mUserId;

    private int mSessionId;

    private String mSessionName;

    public GymLog(String exercise, double weight, int reps, int userId,int sessionId, String sessionName) {
        mExercise = exercise;
        mWeight = weight;
        mReps = reps;

        mDate = new Date();

        mUserId = userId;

        this.mSessionId = sessionId;

        this.mSessionName = sessionName;
    }

    public int getUserId() {
        return mUserId;
    }

    public int getSessionId() {
        return mSessionId;
    }

    public void setSessionId(int sessionId) {
        mSessionId = sessionId;
    }

    public String getSessionName() {
        return mSessionName;
    }

    public void setSessionName(String sessionName) {
        mSessionName = sessionName;
    }

    public void setUserId(int userId) {
        mUserId = userId;
    }

    public String getExercise() {
        return mExercise;
    }

    public void setExercise(String exercise) {
        mExercise = exercise;
    }

    public double getWeight() {
        return mWeight;
    }

    public void setWeight(double weight) {
        mWeight = weight;
    }

    public int getReps() {
        return mReps;
    }

    public void setReps(int reps) {
        mReps = reps;
    }



    public int getLogId() {
        return mLogId;
    }

    public void setLogId(int logId) {
        mLogId = logId;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }
    @Override
    public String toString() {
//        return "Log ID#"+mLogId+ "\n" +
//                "Exercise: "+mExercise+ "\n" +
//                "Weight: "+mWeight+ "\n" +
//                "Reps: "+mReps+ "\n" +
//                "Date: "+mDate+ "\n" +
//                "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-\n";
        return //"Log ID#"+mLogId+ "\n" +
                "Date: "+mDate+ "\n" +
                         "\n" +
                "Exercise: "+mExercise+ " | " + "Weight: "+mWeight+ " | " + "Reps: "+mReps +"\n" +
                        "\n" +
                "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-\n";

    }
}
