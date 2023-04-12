package com.example.gradework;

import static android.content.ContentValues.TAG;

import static java.util.Calendar.SHORT_FORMAT;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.opengl.Visibility;
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
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NoteEdit extends AppCompatActivity {

    Toolbar toolbar;
    Calendar date;
    Note note;
    TextInputEditText topicInput;
    TextInputEditText textInput;
    Switch enableSwitch;
    TextView notificationTime;
    Button setTimeButton;
    ImageButton saveNoteButton;
    Note currentNote;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String filename = (String) intent.getSerializableExtra("filename");
        currentNote = new Note(getApplicationContext(), filename);
        setContentView(R.layout.note_edit);

        topicInput = findViewById(R.id.noteTopicInput);
        textInput = findViewById(R.id.noteTextInput);
        enableSwitch = findViewById(R.id.noteEnableSwitch);
        notificationTime = findViewById(R.id.time);
        setTimeButton = findViewById(R.id.setTimeButton);
        saveNoteButton = findViewById(R.id.saveNote);

        toolbar = findViewById(R.id.noteEditToolbar);
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        toolbar.setTitle("Редагування нотатки");
        setSupportActionBar(toolbar);
        toggleTimeSettingsVisibility(false);

        if (currentNote.topic.equals("")) {
            topicInput.setText("");
        } else {
            topicInput.setText(currentNote.topic);
        }
        textInput.setText(currentNote.text);
        if (currentNote.notificationTimestamp != 0) {
            Date datetime = new Date(currentNote.notificationTimestamp);
            notificationTime.setText((String) android.text.format.DateFormat.format("dd.MM.yy HH:mm", datetime));
        } else {
            notificationTime.setText("Час не вказано");
        }

        setTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                showTimePicker();
            }
        });
        saveNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                NoteEdit.this.currentNote.write();
                Intent intent = new Intent(getApplicationContext(), Notification.class);
                intent.putExtra("titleExtra", "Dynamic Title");
                intent.putExtra("textExtra", "Dynamic Text Body");
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, currentNote.notificationTimestamp, pendingIntent);
                Toast.makeText(getApplicationContext(),"Нотатку збережено.", Toast.LENGTH_LONG).show();
            }
        });
        enableSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                toggleTimeSettingsVisibility(isChecked);
            }
        });
    }

    private void toggleTimeSettingsVisibility(boolean enable) {
        if (enable) {
            this.notificationTime.setVisibility(View.VISIBLE);
            this.setTimeButton.setVisibility(View.VISIBLE);
        } else {
            this.notificationTime.setVisibility(View.GONE);
            this.setTimeButton.setVisibility(View.GONE);
        }
    }

    private void showTimePicker() {
        final Calendar currentDate = Calendar.getInstance();
        date = Calendar.getInstance();
        new TimePickerDialog(NoteEdit.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                date.add(Calendar.DATE, 1);
                date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                date.set(Calendar.MINUTE, minute);
                NoteEdit.this.currentNote.notificationTimestamp = date.getTimeInMillis();
                TextView time = findViewById(R.id.time);
                SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                time.setText(df.format(date.getTime()));
            }
        }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), true).show();
    }
}
