package com.daclink.gymlog_v_sp22;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.daclink.gymlog_v_sp22.DB.AppDataBase;

/**
 *This class creates an entity that is a table in the database. This table is called USER_TABLE.
 * @author Joshua Okero
 * Date 12-6-23
 */
@Entity(tableName = AppDataBase.USER_TABLE)
public class User {
    @PrimaryKey(autoGenerate = true)
    private int mUserId;

    private String mUserName;

    private String mUserPassword;

    public User(String userName, String userPassword) {
        mUserName = userName;
        mUserPassword = userPassword;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        mUserId = userId;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public String getUserPassword() {
        return mUserPassword;
    }

    public void setUserPassword(String userPassword) {
        mUserPassword = userPassword;
    }
}
