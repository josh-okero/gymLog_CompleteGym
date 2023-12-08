package com.daclink.gymlog_v_sp22;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.daclink.gymlog_v_sp22.databinding.ActivityAdminBinding;

import java.util.List;


public class AdminActivity extends AppCompatActivity {
    //ActivityMainBinding binding;
    private static final String USER_ID_KEY = "com.daclink.gymlog_v_sp22.userIdKey";
    private static final String PREFEENCES_KEY ="com.daclink.gymlog_v_sp22.PREFENCES_KEY" ;

    private List<User> mUserList;
    private GymLogDAO mGymLogDAO;

    ActivityAdminBinding binding;

    private TextView mMainDisplay;

    int mUserId;

    User mUser;

    private  SharedPreferences mPreferences = null;

    private Menu menu;



    //private List<GymLog> mGymLogList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);



        getDatabase();
        checkForUser();
        addUserToPreference(mUserId);
        loginUser(mUserId);
        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //mMainDisplay = binding.AdminActivity;
        mMainDisplay = binding.AdminActivity;

        displayUsers();


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
    public static Intent IntentFactory(Context context, int userId){
        Intent intent = new Intent (context, AdminActivity.class);
        intent.putExtra(USER_ID_KEY, userId);
        return intent;
    }


//    private void displayUsers() {
//        mUserList = mGymLogDAO.getAllUsers();
//        //Continue: I'm able to attain all the values from the array but I don't know
//        //via the code below, the next step is to figure out how to put them
//        //in a button
//        if (!mUserList.isEmpty()) {
//            StringBuilder sb = new StringBuilder();
//            for (User user : mUserList) {
//                sb.append(user.getUserName());
//            }
//            mMainDisplay.setText(sb.toString());
//        } else {
//            mMainDisplay.setText("No users found");
//        }
//    }


    private void displayUsers() {
        //Continue: Implement log out button in xml
        //
        //
        LinearLayout buttonContainer = findViewById(R.id.buttonContainer);

        mUserList = mGymLogDAO.getAllUsers();
        // Number of buttons you want to create
        int numberOfButtons = mUserList.size();

        // Create buttons in a loop
        for (int i = 0; i < numberOfButtons; i++) {
            // Create a new Button
            Button button = new Button(this);
            button.setBackgroundColor(3);
            User currentUser = mUserList.get(i);
            // Set button text (you can customize this)
            button.setText(currentUser.getUserName());
            // Set an OnClickListener for the button (customize as needed)

                button.setOnClickListener(view -> {
                    if(!currentUser.getUserName().equals("admin2")) {
                        mUserList.remove(currentUser);
                        mGymLogDAO.delete(currentUser);

                        buttonContainer.removeView(button);
                        buttonContainer.invalidate();
                        // Handle button click event
                        // Example: Toast.makeText(YourActivity.this, "Button " + (i + 1) + " clicked", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(this, "Cannot delete an admin user.", Toast.LENGTH_SHORT).show();
                    }
                    });
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                    );
            button.setLayoutParams(layoutParams);
            buttonContainer.addView(button);
            }
            // Add the button to the LinearLayout

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
                Intent intent = LoginActivity.IntentFactory(AdminActivity.this);
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
//        Menu menu = findViewById(R.menu.user_menu).getMen;
        MenuItem cheerMessageItem = menu.findItem(R.id.CheerMessage);

        if (cheerMessageItem != null) {
            cheerMessageItem.setVisible(true);
        }
        return true;
    }
    public  boolean onOptionsItemSelected(@NonNull MenuItem item){
        switch ((item.getItemId())){
            case R.id.CheerMessage:
                Toast.makeText(this, "Keep up the good work admin!.",Toast.LENGTH_SHORT).show();
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




