package com.daclink.gymlog_v_sp22.DB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.daclink.gymlog_v_sp22.GymLog;
import com.daclink.gymlog_v_sp22.User;

import java.util.List;

@Dao
/**
 *This is a DAO interface that gives access to USER_TABLE in the database. It utilizes features of User.java to access the table.
 * @author Joshua Okero
 * Date 12-6-23
 */
public interface GymLogDAO {
    @Insert
    void insert(GymLog...gymLogs);

    @Update
    void update(GymLog...gymLogs);

    @Delete
    void delete(GymLog gymLog);

    @Query("SELECT * FROM " + AppDataBase.GYMLOG_TABLE+ " ORDER BY mDate desc")
    List<GymLog> getAllGymLogs();

    @Query("SELECT * FROM " + AppDataBase.GYMLOG_TABLE + " WHERE mLogId = :logId")
    List<GymLog> getLogById(int logId);

    @Query("SELECT * FROM " + AppDataBase.GYMLOG_TABLE + " WHERE mUserId = :userId ORDER BY mDate desc")
    List<GymLog> getGymLogsByUserId(int userId);

    @Insert
    void insert(User...users);

    @Update
    void update(User...users);

    @Delete
    void delete(User...users);

    @Query("SELECT * FROM " + AppDataBase.USER_TABLE)
    List<User> getAllUsers();


    @Query("SELECT * FROM " + AppDataBase.USER_TABLE+ " WHERE mUserName = :username")
    User getUserByUsername(String username);

    @Query("SELECT * FROM " + AppDataBase.USER_TABLE+ " WHERE mUserId= :userId")
    User getUserByUserId(int userId);
}
