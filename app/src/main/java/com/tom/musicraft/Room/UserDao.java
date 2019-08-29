package com.tom.musicraft.Room;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.tom.musicraft.Models.UserAccountSettings;

import java.util.List;

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(UserAccountSettings userAccountSettings);

    @Query("DELETE FROM Users")
    void deleteAll();

    @Query("SELECT * from Users")
    LiveData<List<UserAccountSettings>> getAllUsers();

    @Query("SELECT * FROM Users WHERE user_id= :userId")
    LiveData<UserAccountSettings> getUserById(String userId);
}
