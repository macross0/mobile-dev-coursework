package com.example.gradework;

import static android.content.ContentValues.TAG;

import static java.util.Calendar.SHORT_FORMAT;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NoteEdit extends AppCompatActivity {

    Toolbar toolbar;
    Calendar date;
    Note note;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        note = (Note) intent.getSerializableExtra("note");
        setContentView(R.layout.note_edit);

        toolbar = findViewById(R.id.noteEditToolbar);
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        toolbar.setTitle("Редагування нотатки");
        setSupportActionBar(toolbar);

        TextInputEditText topicInput = findViewById(R.id.noteTopicInput);
        TextInputEditText textInput = findViewById(R.id.noteTextInput);
        TextView notificationTime = findViewById(R.id.time);
        Switch enableSwitch = findViewById(R.id.enableEditSwitch);
        Button setTimeButton = findViewById(R.id.setTimeButton);

        if (note.topic.equals("")) {
            topicInput.setText("");
        } else {
            topicInput.setText(note.topic);
        }
        textInput.setText(note.text);
        if (note.notificationTimestamp != 0) {
            Date datetime = new Date(note.notificationTimestamp);
            notificationTime.setText((String) android.text.format.DateFormat.format("dd.MM.yy HH:mm", datetime));
        } else {
            notificationTime.setText("Час не вказано");
        }

        setTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                showDateTimePicker();
            }
        });
        enableSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                } else {

                }
            }
        });
    }

    public void showDateTimePicker() {
        final Calendar currentDate = Calendar.getInstance();
        date = Calendar.getInstance();
        new DatePickerDialog(NoteEdit.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);
                new TimePickerDialog(NoteEdit.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        date.set(Calendar.MINUTE, minute);
                        note.notificationTimestamp = date.getTimeInMillis();
                        TextView time = findViewById(R.id.time);
                        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                        time.setText(df.format(date.getTime()));
                    }
                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), true).show();
            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }
}
