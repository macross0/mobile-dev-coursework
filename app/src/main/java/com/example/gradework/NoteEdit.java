package com.example.gradework;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NoteEdit extends AppCompatActivity {

    Toolbar toolbar;
    Calendar date;
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
        if (filename.isEmpty()) {
            currentNote = new Note(getApplicationContext());
        } else {
            currentNote = new Note(getApplicationContext(), filename);
        }
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
        enableSwitch.setChecked(currentNote.notify);
        toggleTimeSettingsVisibility(currentNote.notify);

        if (currentNote.hasTopic()) {
            topicInput.setText(currentNote.topic);
        } else {
            topicInput.setText("");
        }
        textInput.setText(currentNote.text);
        if (currentNote.notificationTimestamp != 0) {
            Date datetime = new Date(currentNote.notificationTimestamp);
            notificationTime.setText((String) android.text.format.DateFormat.format("dd.MM.yy HH:mm", datetime));
        } else {
            notificationTime.setText("Час не вказано");
        }

        setTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker();
            }
        });
        saveNoteButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.S)
            @Override
            public void onClick(View v) {
                NoteEdit.this.currentNote.topic = String.valueOf(topicInput.getText());
                NoteEdit.this.currentNote.text = String.valueOf(textInput.getText());
                NoteEdit.this.currentNote.write();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    createNotificationChannel();
                    scheduleNotification(NoteEdit.this.currentNote);
                }

                Toast.makeText(getApplicationContext(), "Нотатку збережено", Toast.LENGTH_LONG).show();
            }
        });
        enableSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                NoteEdit.this.currentNote.notify = isChecked;
                toggleTimeSettingsVisibility(isChecked);
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createNotificationChannel() {
        String id = "NoteAlerts";
        String name = "Нагадування";
        String description = "Нагадування про нотатки";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(id, name, importance);
        channel.setDescription(description);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);
    }

    public void scheduleNotification(Note note) {
        Intent intent = new Intent(getApplicationContext(), com.example.gradework.Notification.class);
        intent.putExtra("topic", note.topic);
        intent.putExtra("text", note.text);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//        Log.v("planned", String.format("%d", note.notificationTimestamp));
//        Log.v("now", String.format("%d", Calendar.getInstance().getTimeInMillis()));

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, note.notificationTimestamp, pendingIntent);
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

    private void showDateTimePicker() {
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
                        date.add(Calendar.SECOND, 10);
                        NoteEdit.this.currentNote.notificationTimestamp = date.getTimeInMillis();
                        TextView time = findViewById(R.id.time);
                        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                        time.setText(df.format(date.getTime()));
                    }
                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }
}

