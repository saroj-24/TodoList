package com.example.noteapp.NotesAdapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noteapp.Models.Notes;
import com.example.noteapp.NotesOnClick;
import com.example.noteapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NotesListAdapter  extends RecyclerView.Adapter<NotesViewHolder>{
    Context context;
    List<Notes> list;
    NotesOnClick notesOnClick;

    public NotesListAdapter(Context context, List<Notes> list, NotesOnClick notesOnClick) {
        this.context = context;
        this.list = list;
        this.notesOnClick = notesOnClick;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotesViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_item,parent,false));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
          holder.textView_title.setText(list.get(position).getTitle());
          holder.textView_title.setSelected(true);

          holder.textView_notes.setText(list.get(position).getNotes());

          holder.textView_dates.setText(list.get(position).getDates());
          holder.textView_dates.setSelected(true);

          if(list.get(position).isPinned())
          {
              holder.pin_id.setImageResource(R.drawable.pin);
          }
          else {
              holder.pin_id.setImageResource(0);
          }
          int color_code  = getRandomColor();
          holder.notes_containers.setCardBackgroundColor(holder.itemView.getResources().getColor(color_code,null));



          holder.notes_containers.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {

              }
          });

          holder.notes_containers.setOnLongClickListener(new View.OnLongClickListener() {
              @Override
              public boolean onLongClick(View v) {
                  notesOnClick.onLongClickNotes(list.get(holder.getAdapterPosition()),holder.notes_containers);
                  return true;
              }
          });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void filterList(List<Notes> filteredList)
    {
        list = filteredList;
        notifyDataSetChanged();
    }

    private  int getRandomColor()
    {
        List<Integer> color_code = new ArrayList<>();
        color_code.add(R.color.color1);
        color_code.add(R.color.color2);
        color_code.add(R.color.color3);
        color_code.add(R.color.color4);
        color_code.add(R.color.color5);
        color_code.add(R.color.color6);
        color_code.add(R.color.color7);
        color_code.add(R.color.color8);
        color_code.add(R.color.color10);
        color_code.add(R.color.color11);
        color_code.add(R.color.color12);
        color_code.add(R.color.color13);
        color_code.add(R.color.white);

        Random random = new Random();
        int random_color= random.nextInt(color_code.size());
        return color_code.get(random_color);
    }
}
class NotesViewHolder extends RecyclerView.ViewHolder{

    CardView notes_containers;
    TextView textView_title,textView_notes,textView_dates;
    ImageView pin_id;
    public NotesViewHolder(@NonNull View itemView) {
        super(itemView);

        notes_containers=itemView.findViewById(R.id.notes_containers);
        textView_title=itemView.findViewById(R.id.textView_title);
        textView_notes=itemView.findViewById(R.id.textView_notes);
        textView_dates=itemView.findViewById(R.id.textView_dates);
        pin_id=itemView.findViewById(R.id.pin_id);

    }
}
