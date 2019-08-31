package com.tom.musicraft.Room;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.tom.musicraft.Models.Comment;
import com.tom.musicraft.Models.Post;
import com.tom.musicraft.Models.UserAccountSettings;

@Database(entities = {Post.class/*, Comment.class, UserAccountSettings.class*/}, version = 5)
public abstract class PostRoomDatabase extends RoomDatabase {
    public abstract PostDao postDao();
    public abstract CommentDao commentsDao();     /// TODO
//    public abstract UserDao userDao();

    private static volatile PostRoomDatabase INSTANCE;

    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback() {

                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    super.onOpen(db);
                }
            };

    static PostRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (PostRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            PostRoomDatabase.class, "musiccraft-679ee")     //https://musiccraft-679ee.firebaseio.com/
                            .addCallback(sRoomDatabaseCallback)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
