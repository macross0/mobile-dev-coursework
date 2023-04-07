package com.example.gradework;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class NoteEdit extends AppCompatActivity {

    Toolbar toolbar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_edit);

        toolbar = findViewById(R.id.noteEditToolbar);
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        toolbar.setTitle("Редагування нотатки");
        setSupportActionBar(toolbar);

        Switch enableSwitch = findViewById(R.id.enableEditSwitch);
        CalendarView calendar = findViewById(R.id.calendar);
        TimePicker timePicker = findViewById(R.id.timePicker);
        calendar.setVisibility(View.GONE);
        timePicker.setVisibility(View.GONE);
        enableSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    calendar.setVisibility(View.VISIBLE);
                    timePicker.setVisibility(View.VISIBLE);
                } else {
                    calendar.setVisibility(View.GONE);
                    timePicker.setVisibility(View.GONE);
                }
            }
        });
    }
}
