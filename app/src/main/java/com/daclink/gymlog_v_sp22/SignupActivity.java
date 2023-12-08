package com.daclink.gymlog_v_sp22;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.daclink.gymlog_v_sp22.DB.AppDataBase;
import com.daclink.gymlog_v_sp22.DB.GymLogDAO;
//To do: After you add the user to the db, go ahead and
//input the username and password into the db
public class SignupActivity extends AppCompatActivity {
    private EditText mUsernameField;
    private EditText mPasswordField;

    private Button mButton;

    private GymLogDAO mGymLogDAO;

    private  String mUserName;
    private String mPassword;

    private User mUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        wireupDisplay();

        getDatabase();

    }
    private void wireupDisplay(){
        mUsernameField = findViewById(R.id.editTextLoginUserName);
        mPasswordField =  findViewById(R.id.editTextTextLoginPassword);
        mButton = findViewById(R.id.buttonSignup);
        mButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getValuesFromDisplay();
                if(!checkForUserInDatabase()){
                    //If the user is not in the database, add them to the db
                    User user = new User(mUsernameField.getText().toString(), mPasswordField.getText().toString());
                    mGymLogDAO.insert(user);
                    Intent intent = LoginActivity.IntentFactory(SignupActivity.this);
                    startActivity(intent);
                    //To do: Move to log in page, so call the intentFactory method
                }
                else {
                    //Clear the text fields
                    Toast.makeText(SignupActivity.this, "Username:"+ mUserName + " already exists.", Toast.LENGTH_SHORT).show();

                };

            }
        });
    }
    private void getValuesFromDisplay(){
        mUserName = mUsernameField.getText().toString();
        mPassword = mPasswordField.getText().toString();

    }
    private boolean checkForUserInDatabase(){
        mUser = mGymLogDAO.getUserByUsername(mUserName);
        if(mUser == null){
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
        Intent intent = new Intent (context, SignupActivity.class);
        return intent;
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user_menu,menu);

        MenuItem cheerMessageItem = menu.findItem(R.id.CheerMessage);
        MenuItem userMenuLogout = menu.findItem(R.id.userMenuLogout);
        MenuItem loginPage = menu.findItem(R.id.LoginPage);

        cheerMessageItem.setVisible(false);
        userMenuLogout.setVisible(false);
        loginPage.setVisible(true);

        return true;
    }
    public  boolean onOptionsItemSelected(@NonNull MenuItem item){
        switch ((item.getItemId())){
            case R.id.LoginPage:
                returnToLoginPage();
                return true;
            default:
                return  super.onOptionsItemSelected(item);
        }

}

    private void returnToLoginPage() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setMessage("Back to log in page?");
        alertBuilder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                Intent login = LoginActivity.IntentFactory(SignupActivity.this);
                startActivity(login);
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
    }

