package com.example.noteapp;

import static com.example.noteapp.R.*;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.noteapp.Database.RoomDB;
import com.example.noteapp.Models.Notes;
import com.example.noteapp.NotesAdapter.NotesListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    RecyclerView recyclerView;
    NotesListAdapter notesListAdapter;
    List<Notes> notes = new ArrayList<>();
    RoomDB database;
    FloatingActionButton floatingActionButton;

    SearchView searchView_home;

    Notes selectedNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_main);

        recyclerView = findViewById(id.recycle_home);
        floatingActionButton = findViewById(id.floating_actionButton);
        searchView_home = findViewById(id.searchView_home);
        database = RoomDB.getInstance(this);
        notes= database.mainDAO().getALLnotes();
        updateRecycle(notes);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,NotesTakerActivity.class);
                 startActivityForResult(intent,101);

            }
        });

        searchView_home.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                fun_filter(newText);
                return true;
            }
        });


    }

    private void fun_filter(String newText) {
        List<Notes> filteredlist = new ArrayList<>();
        for(Notes singlenotes: notes)
        {
            if(singlenotes.getTitle().toLowerCase().contains(newText.toLowerCase())||singlenotes.getNotes().toLowerCase().contains(newText.toLowerCase()))
            {
                filteredlist.add(singlenotes);
            }
        }
        notesListAdapter.filterList(filteredlist);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==101)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                Notes new_notes = (Notes) data.getSerializableExtra("note");
                database.mainDAO().insert(new_notes);
                notes.clear();
                notes.addAll(database.mainDAO().getALLnotes());
                notesListAdapter.notifyDataSetChanged();
            }
            else if(requestCode ==102)
            {

               if(resultCode==Activity.RESULT_OK)
               {
                   Notes new_notes = (Notes) data.getSerializableExtra("note");
                   database.mainDAO().update(new_notes.getID(),new_notes.getTitle(),new_notes.getNotes());
                   notes.clear();
                   notes.addAll(database.mainDAO().getALLnotes());
                   notesListAdapter.notifyDataSetChanged();

               }
            }
        }
    }

    private void updateRecycle(List<Notes> notes) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        notesListAdapter = new NotesListAdapter(MainActivity.this,notes,notesOnClick1);
        recyclerView.setAdapter(notesListAdapter);
    }
    private final NotesOnClick  notesOnClick1 = new NotesOnClick() {

        @Override
        public void onSetNoteClick(Notes notes) {
            Intent intent = new Intent(MainActivity.this,NotesTakerActivity.class);
            intent.putExtra("note",notes);
            startActivityForResult(intent,102);
        }

        @Override
        public void onLongClickNotes(Notes notes, CardView cardView) {
            selectedNotes  = notes;
             showpopUp(cardView);

        }
    };

    private void showpopUp(CardView cardView) {
        PopupMenu popupMenu = new PopupMenu(this,cardView);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(menu.popupmenu);
        popupMenu.show();

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id =  item.getItemId();
        if(id== R.id.pin)
        {
            if(selectedNotes.isPinned())
            {
                database.mainDAO().pin(selectedNotes.getID(),false);
                Toast.makeText(MainActivity.this, "Unpinned", Toast.LENGTH_SHORT).show();
            }
            else {
                database.mainDAO().pin(selectedNotes.getID(),true);
                Toast.makeText(MainActivity.this, "Pinned", Toast.LENGTH_SHORT).show();
            }
            notes.clear();
            notes.addAll(database.mainDAO().getALLnotes());
            notesListAdapter.notifyDataSetChanged();
            return true;

        } else if (id==R.id.delete) {
            database.mainDAO().delete(selectedNotes);
            notes.remove(selectedNotes);
            notesListAdapter.notifyDataSetChanged();
            Toast.makeText(MainActivity.this,"Notes deleted",Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}