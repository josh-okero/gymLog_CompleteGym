package com.daclink.gymlog_v_sp22;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.daclink.gymlog_v_sp22.DB.AppDataBase;
import com.daclink.gymlog_v_sp22.DB.GymLogDAO;
import com.daclink.gymlog_v_sp22.databinding.ActivitySessionlogBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SessionLog extends AppCompatActivity {
    //ActivityMainBinding binding;
    private static final String USER_ID_KEY = "com.daclink.gymlog_v_sp22.userIdKey";
    private static final String PREFEENCES_KEY ="com.daclink.gymlog_v_sp22.PREFENCES_KEY" ;

    private List<GymLog> mGymLogs;
    private GymLogDAO mGymLogDAO;

    ActivitySessionlogBinding binding;

    private TextView mViewSessionsDisplay;

    int mUserId;

    User mUser;

    private SharedPreferences mPreferences = null;

    private Menu menu;

    private static int sessionID;


    //private List<GymLog> mGymLogList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sessionlog);



        getDatabase();
        checkForUser();
        addUserToPreference(mUserId);
        loginUser(mUserId);
        binding = ActivitySessionlogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mViewSessionsDisplay = binding.sessionLogActivity;

        displayLogs();


        //addUserToPreference(mUserId);
        //loginUser(mUserId);


        //What i want to do is take populate the XML
//        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        mMainDisplay = binding.mainGymLogDisplay;

    }

    private void getDatabase() {
        mGymLogDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .GymLogDAO();
    }
    private void checkForUser() {
        mUserId= getIntent().getIntExtra(USER_ID_KEY,-1);
        if(mUserId != -1){
            return;
        }

        if(mPreferences == null){
            getPrefs();
        }
        mUserId = mPreferences.getInt(USER_ID_KEY,-1);

        if(mUserId != -1){
            return;
        }

        List<User> users = mGymLogDAO.getAllUsers();

        if(users.size()<= 0){
//            User defaultUser = new User("daclink","dac123");
//            User altUser = new User("drew", "dac123");
            User testUser = new User("testuser1","testuser1");
            User adminUser = new User("admin2", "admin2");
            mGymLogDAO.insert(testUser,adminUser);
        }
    }

    //Implement code get all the users and display them in a button



    public static Intent IntentFactory(Context context, int userId, int sessionId){
        Intent intent = new Intent (context, SessionLog.class);
        intent.putExtra(USER_ID_KEY, userId);
        sessionID = sessionId;
        return intent;
    }



    private void displayLogs() {

        LinearLayout buttonContainer = findViewById(R.id.sessionLogContainer);

        mGymLogs= mGymLogDAO.getGymLogsBySessionId(sessionID);

        //Get unique session Id's
        StringBuilder sb = new StringBuilder();
        for (GymLog log : mGymLogs) {
            sb.append(log.toString());
            mViewSessionsDisplay.setText(sb.toString());
        }

        }

    private void logoutUser(){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setMessage(R.string.logout);
        alertBuilder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                clearUserFromIntent();
                clearUserFromPref();
                mUserId = -1;
                Intent intent = LoginActivity.IntentFactory(SessionLog.this);
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
    private void clearUserFromIntent(){
        getIntent().putExtra(USER_ID_KEY,-1);
    }

    private void clearUserFromPref(){
        addUserToPreference(-1);
    }


    private void addUserToPreference(int userId){
        if(mPreferences == null){
            getPrefs();
        }
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(USER_ID_KEY,userId);
        editor.apply();
    }
    private void getPrefs(){
        mPreferences = this.getSharedPreferences(PREFEENCES_KEY,Context.MODE_PRIVATE);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user_menu,menu);
        MenuItem backButton = menu.findItem(R.id.backButton);
        if (backButton != null) {
            backButton.setVisible(true);
        }
//        Menu menu = findViewById(R.menu.user_menu).getMen;
        return true;
    }
    public  boolean onOptionsItemSelected(@NonNull MenuItem item){
        switch ((item.getItemId())){
            case R.id.backButton:
                Intent intent = ViewSessionsActivity.IntentFactory(getApplicationContext(),mUserId);
                startActivity(intent);
                return true;
            case R.id.userMenuLogout:
                logoutUser();
                return true;
            default:
                return  super.onOptionsItemSelected(item);
        }
    }
    private void loginUser(int userId) {
        mUser = mGymLogDAO.getUserByUserId(userId);
        addUserToPreference(userId);
        invalidateOptionsMenu();
    }
    public  boolean onPrepareOptionsMenu(Menu menu){
        if(mUser!=null){
            MenuItem item = menu.findItem(R.id.userMenuLogout);
            item.setTitle(mUser.getUserName());
        }
        return  super.onPrepareOptionsMenu(menu);
    }
}

