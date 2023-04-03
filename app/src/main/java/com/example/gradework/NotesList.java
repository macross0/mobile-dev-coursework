package com.example.gradework;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NotesList extends AppCompatActivity {

    public ScrollView notesScrollView;
    public LinearLayout notesLayout;
    List<Note> notes = new ArrayList<Note>();

    LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes_list);
        setTitle(R.string.app_name);

        inflater = (LayoutInflater) getApplicationContext().getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        notesLayout = findViewById(R.id.notesLinearLayout);

//        for (int i = 0; i < 100; i++) {
//            Note note = new Note(getApplicationContext());
//            if (i % 2 == 0) {
//                note.topic = String.format("topic %d", i);
//            }
//            note.text = String.format("%d", i);
//            note.write();
//        }

        String[] fileNames = fileList();
        for (String fileName : fileNames) {
            Note note = new Note(getApplicationContext(), fileName);
            note.read();
//            notes.add(note);

            View card = inflater.inflate(R.layout.note_card,null);
            notesLayout.addView(card);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) card.getLayoutParams();
            params.setMargins(0, 0, 0, this.spToPx(8));
            card.setLayoutParams(params);

            TextView description = card.findViewById(R.id.description);
            description.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), NoteEdit.class);
                    startActivity(intent);
                }
            });
            TextView datetime = card.findViewById(R.id.notificationDatetime);
//            Switch enable_switch = card.findViewById(R.id.enableSwitch);

            description.setText(note.getDescription());
            if (note.hasTopic()) {
                description.setTypeface(null, Typeface.BOLD);
            }
            datetime.setText(note.getModificationDateTime());
        }

//        fileText = findViewById(R.id.read_data);
//        writeButton = findViewById(R.id.write);
//        readButton = findViewById(R.id.read);
//        writeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                writeFile("test.note", "test1234");
//            }
//        });
//
//        readButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                fileText.setText();
//            }
//        });
    }

    private int spToPx(float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getApplicationContext().getResources().getDisplayMetrics());
    }
}