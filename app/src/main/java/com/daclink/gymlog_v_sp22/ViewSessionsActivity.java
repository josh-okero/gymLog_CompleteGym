package com.daclink.gymlog_v_sp22;

import android.content.Context;
import android.content.Intent;

public class ViewSessionsActivity {
    //Implementation should be similar to the admin page

    //Maybe there should be a button that allows the user to create a new session

    //Maybe there should be a button that allows the user to add a new exercise

    //If the session is clicked & still current, there should be a button to end the session

    //After an item in sessions page is clicked it should be able to go to another  page where
    //the  user is able to see all content from that session

    public static Intent IntentFactory(Context context){
        Intent intent = new Intent (context, LoginActivity.class);
        return intent;
    }

//    private  GymLog getValuesFromDisplay(){
//        String exercise = "No record found";
//        double weight = 0.0;
//        int reps = 0;
//
//        exercise = mExercise.getText().toString();
//
//        try {
//            weight = Double.parseDouble((mWeight.getText().toString()));
//        }
//        catch (NumberFormatException e){
//            Log.d("GYMLOG", "Couldn't convert weight");
//        }
//        try {
//            reps = Integer.parseInt(mReps.getText().toString());
//        }
//        catch (NumberFormatException e){
//            Log.d("GYMLOG","Couldn't convert reps");
//        }
//        GymLog log = new GymLog(exercise,weight, reps,mUserId);
//
//        return log;
//    }
//    private void submitGymLog(){
//        String exercise = mExercise.getText().toString();
//        double weight = Double.parseDouble(mWeight.getText().toString());
//        int reps = Integer.parseInt(mReps.getText().toString());
//
//        GymLog log = new GymLog(exercise, weight, reps, mUserId);
//
//        mGymLogDAO.insert(log);
//
//    }
}
