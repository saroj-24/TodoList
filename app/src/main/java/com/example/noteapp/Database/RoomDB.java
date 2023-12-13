package com.example.noteapp.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.noteapp.Models.Notes;

@Database(entities = Notes.class, version = 1,exportSchema = false)
public abstract class RoomDB extends RoomDatabase {

    public  static RoomDB database;
    public static  String  DATABASE_NAME="NotesApp";
    public synchronized  static  RoomDB getInstance(Context context)
    {
        if(database==null)
        {
            database= Room.databaseBuilder(context.getApplicationContext(),
                    RoomDB.class, DATABASE_NAME).allowMainThreadQueries().fallbackToDestructiveMigration().
                    build();
        }
        return  database;
    }

  public  abstract MainDAO mainDAO();




}
