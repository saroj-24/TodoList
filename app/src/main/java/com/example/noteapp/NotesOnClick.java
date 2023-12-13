package com.example.noteapp;

import androidx.cardview.widget.CardView;

import com.example.noteapp.Models.Notes;

public interface NotesOnClick {

    void onSetNoteClick(Notes notes);
    void onLongClickNotes(Notes notes, CardView cardView);

}
