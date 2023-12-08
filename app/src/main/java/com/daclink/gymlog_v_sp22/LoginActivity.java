package com.daclink.gymlog_v_sp22;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.daclink.gymlog_v_sp22.DB.AppDataBase;
import com.daclink.gymlog_v_sp22.DB.GymLogDAO;

public class LoginActivity extends AppCompatActivity {

    private EditText mUsernameField;
    private EditText mPasswordField;

    private Button mButton;

    private GymLogDAO mGymLogDAO;

    private  String mUserName;
    private String mPassword;

    private User mUser;

    private Button mSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        wireupDisplay();

        getDatabase();


    }
    private void wireupDisplay(){
        mUsernameField = findViewById(R.id.editTextLoginUserName);
        mPasswordField =  findViewById(R.id.editTextTextLoginPassword);
        mButton = findViewById(R.id.buttonLogin);
        mButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getValuesFromDisplay();
                if (checkForUserInDatabase()) {
                    if (!validatePassword()) {
                        Toast.makeText(LoginActivity.this, "Invalid password", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent;
                        if (mUser.getUserName().equals("admin2")) {
                            intent = AdminActivity.IntentFactory(getApplicationContext(), mUser.getUserId());
                            startActivity(intent);
                        } else {
                            intent = SessionActivity.IntentFactory(getApplicationContext(),mUser.getUserId());
                            startActivity(intent);
                        }
                    }

                }
            }
        });
        mSignUp = findViewById(R.id.buttonSignup);
        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = SignupActivity.IntentFactory(getApplicationContext());
                startActivity(intent);
            }
        });
    }
    private boolean validatePassword(){
        return mUser.getUserPassword().equals(mPassword);
    }

    private void getValuesFromDisplay(){
        mUserName = mUsernameField.getText().toString();
        mPassword = mPasswordField.getText().toString();

    }

    private boolean checkForUserInDatabase(){
        mUser = mGymLogDAO.getUserByUsername(mUserName);
        if(mUser == null){
            Toast.makeText(this, "no user"+ mUserName + " found", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void getDatabase(){
        mGymLogDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .GymLogDAO();
    }
    public static Intent IntentFactory(Context context){
     Intent intent = new Intent (context, LoginActivity.class);
     return intent;
    }
}