package com.daclink.gymlog_v_sp22;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.text.InputType;

import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.daclink.gymlog_v_sp22.DB.AppDataBase;
import com.daclink.gymlog_v_sp22.DB.GymLogDAO;
import com.daclink.gymlog_v_sp22.databinding.ActivityMainBinding;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    //Continue:
    //Make the text displayed prettier("look at his previous videos)



    private static final String USER_ID_KEY = "com.daclink.gymlog_v_sp22.userIdKey";
    private static final String PREFEENCES_KEY = "com.daclink.gymlog_v_sp22.PREFENCES_KEY";


    ActivityMainBinding binding;
    private TextView mMainDisplay;


    private TextView mAdmin;

    private EditText mExercise;

    private EditText mWeight;

    private EditText mReps;

    private Button mSubmit;


    private Button mEndSession;


    private GymLogDAO mGymLogDAO;

    private List<GymLog> mGymLogList;

    private int mUserId = -1;


    private SharedPreferences mPreferences = null;

    private User mUser;

    Random random  = new Random();

    int testId = random.nextInt();;

    List<Integer> mSessionIdList = new ArrayList<>();

    private int mSessionId;

    private String mSessionName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (mSessionIdList.size() == 0) {
            mSessionIdList.add(testId);
            mSessionId = testId;
        }
        //Make sure id# hasn't been used
        else {
            if (mSessionIdList.contains(testId)) {
                boolean notContain = true;
                while (notContain) {
                    testId = random.nextInt();
                    if (!mSessionIdList.contains(testId)) {
                        notContain = false;
                        mSessionIdList.add(testId);
                        mSessionId = testId;
                    }
                }
            } else {
                mSessionIdList.add(testId);
                mSessionId = testId;
            }
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        if (intent != null) mSessionName = intent.getStringExtra("SESSION_NAME");


        getDatabase();

        checkForUser();

        addUserToPreference(mUserId);
        loginUser(mUserId);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mMainDisplay = binding.mainGymLogDisplay;
        mExercise = binding.mainExerciseEditText;
        mWeight = binding.mainWeightEditText;
        mReps = binding.mainRepsEditText;
        mSubmit = binding.mainSubmitButton;

        mAdmin = binding.textViewAdmin;// previous assignment findViewById(R.id.textView_admin);
        mEndSession = binding.endSessionButton;


        mMainDisplay.setMovementMethod(new ScrollingMovementMethod());

//        mMainDisplay2 = findViewById(R.id.mainGymLogDisplay);
//        mMainDisplay2.setMovementMethod(new ScrollingMovementMethod());
////
////        mExercise = findViewById(R.id.mainExerciseEditText);
////
////        mWeight = findViewById(R.id.mainWeightEditText);
////
////        mReps = findViewById(R.id.mainRepsEditText);


        mGymLogDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .GymLogDAO();
        refreshDisplay();
        //Location data for xml package
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GymLog log = getValuesFromDisplay();
                log.setUserId(mUser.getUserId());
                mGymLogDAO.insert(log);
                refreshDisplay();
            }
        });

        mEndSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //callAlertBuilder();
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MainActivity.this);
                alertBuilder.setMessage("End  session and go back to previous page?");
                alertBuilder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        Intent intent = SessionActivity.IntentFactory(getApplicationContext(),mUserId);
                        startActivity(intent);

                    }
                });
                alertBuilder.setNegativeButton(getString(R.string.no),
                        new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which){
                                //We don't really need to do anything here
                            }
                        });
                alertBuilder.create().show();
            }
        });
    }

//            if(mUser!=null && mUser.getUserName().equals("admin2")){
//                mAdmin.setVisibility(View.VISIBLE);
//            }
//            else{
//                mAdmin.setVisibility(View.GONE);
//            }



        private void loginUser(int userId) {
            mUser = mGymLogDAO.getUserByUserId(userId);
            addUserToPreference(userId);
            invalidateOptionsMenu();
        }

        public boolean onPrepareOptionsMenu(Menu menu) {
            if (mUser != null) {
                MenuItem item = menu.findItem(R.id.userMenuLogout);
                item.setTitle(mUser.getUserName());
            }
            return super.onPrepareOptionsMenu(menu);
        }

        private void addUserToPreference(int userId) {
            if (mPreferences == null) {
                getPrefs();
            }
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putInt(USER_ID_KEY, userId);
            editor.apply();
        }

        private void getDatabase() {
            mGymLogDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build()
                    .GymLogDAO();
        }

        private void checkForUser() {
            mUserId = getIntent().getIntExtra(USER_ID_KEY, -1);
            if (mUserId != -1) {
                return;
            }

            if (mPreferences == null) {
                getPrefs();
            }
            mUserId = mPreferences.getInt(USER_ID_KEY, -1);

            if (mUserId != -1) {
                return;
            }

            List<User> users = mGymLogDAO.getAllUsers();

            if (users.size() <= 0) {
//            User defaultUser = new User("daclink","dac123");
//            User altUser = new User("drew", "dac123");
                User testUser = new User("testuser1", "testuser1");
                User adminUser = new User("admin2", "admin2");
                mGymLogDAO.insert(testUser, adminUser);
            }

            Intent intent = LoginActivity.IntentFactory(this);
            startActivity(intent);
        }

        private void getPrefs() {
            mPreferences = this.getSharedPreferences(PREFEENCES_KEY, Context.MODE_PRIVATE);
        }

        private void logoutUser() {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
            alertBuilder.setMessage(R.string.logout);
            alertBuilder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    clearUserFromIntent();
                    clearUserFromPref();
                    mUserId = -1;
                    checkForUser();

                }
            });
            alertBuilder.setNegativeButton(getString(R.string.no),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //We don't really need to do anything here
                        }
                    });
            alertBuilder.create().show();
        }

        private void clearUserFromIntent() {
            getIntent().putExtra(USER_ID_KEY, -1);
        }

        private void clearUserFromPref() {
            addUserToPreference(-1);
        }

        private GymLog getValuesFromDisplay() {
            String exercise = "No record found";
            double weight = 0.0;
            int reps = 0;

            exercise = mExercise.getText().toString();

            try {
                weight = Double.parseDouble((mWeight.getText().toString()));
            } catch (NumberFormatException e) {
                Log.d("GYMLOG", "Couldn't convert weight");
            }
            try {
                reps = Integer.parseInt(mReps.getText().toString());
            } catch (NumberFormatException e) {
                Log.d("GYMLOG", "Couldn't convert reps");
            }
            GymLog log = new GymLog(exercise, weight, reps, mUserId, mSessionId, mSessionName);

            return log;
        }

        private void submitGymLog() {
            String exercise = mExercise.getText().toString();
            double weight = Double.parseDouble(mWeight.getText().toString());
            int reps = Integer.parseInt(mReps.getText().toString());

            GymLog log = new GymLog(exercise, weight, reps, mUserId, mSessionId, mSessionName);

            mGymLogDAO.insert(log);

        }

        private void refreshDisplay() {
            mGymLogList = mGymLogDAO.getGymLogsBySessionId(mSessionId);

            if (!mGymLogList.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (GymLog log : mGymLogList) {
                        sb.append(log.toString());
                        mMainDisplay.setText(sb.toString());
                }
            } else {
                mMainDisplay.setText(R.string.no_logs_message);
            }

        }

//private void refreshDisplay() {
//    LinearLayout buttonContainer = findViewById(R.id.sessionLogContainer);
//    mGymLogList = mGymLogDAO.getGymLogsBySessionId(mSessionId);
//
//    // Iterate through the HashMap and display the session name of each gym log
//    if (!mGymLogList.isEmpty()) {
//        for (GymLog log : mGymLogList) {
//            // Create a new Button
//            Button button = new Button(this);
//            button.setBackgroundResource(R.drawable.button_border); // Set the background to the border drawable
//
//            // Set button text (you can customize this)
//            button.setText(log.toString());
//            // Set an OnClickListener for the button (customize as needed)
//
//            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
//                    ViewGroup.LayoutParams.MATCH_PARENT,
//                    ViewGroup.LayoutParams.WRAP_CONTENT
//            );
//            int marginInPixels = getResources().getDimensionPixelSize(R.dimen.button_margin); // Adjust the margin as needed
//            layoutParams.setMargins(marginInPixels, marginInPixels, marginInPixels, marginInPixels);
//
//            button.setLayoutParams(layoutParams);
//            buttonContainer.addView(button);
//        }
//    }
//    else {
//               mMainDisplay.setText(R.string.no_logs_message);
//            }
//}



        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.user_menu, menu);
            return true;
        }

        public boolean onOptionsItemSelected(@NonNull MenuItem item) {
            switch ((item.getItemId())) {
                case R.id.userMenuLogout:
                    logoutUser();
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }

        public static Intent IntentFactory(Context context, int userId, String sessionName) {
        Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra(USER_ID_KEY, userId);
            intent.putExtra("SESSION_NAME", sessionName);
            return intent;
        }
    }
