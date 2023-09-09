package com.example.myapplication.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = Todo.class, exportSchema = false, version = 1)
public abstract class TodoDatabase extends RoomDatabase {
    private static TodoDatabase INSTANCE;

    public static synchronized TodoDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), TodoDatabase.class, "TodoDatabase")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return INSTANCE;
    }

    public abstract TodoDao getTodoDao();
}
