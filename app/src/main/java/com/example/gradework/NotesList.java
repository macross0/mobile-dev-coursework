package com.example.gradework;

import static android.view.View.TEXT_ALIGNMENT_CENTER;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.material.divider.MaterialDivider;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class NotesList extends AppCompatActivity {

    public LinearLayout notesLayout;
    ImageButton newNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes_list);
        setTitle(R.string.app_name);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            this.requestAlarmPermission();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            this.requestPostNotificaionPermission();
        }

        notesLayout = findViewById(R.id.notesLinearLayout);
        newNote = findViewById(R.id.newNote);
        newNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NoteEdit.class);
                intent.putExtra("filename", "");
                startActivity(intent);
            }
        });
        updateNoteList();
    }

    public void updateNoteList() {
        notesLayout.removeAllViews();
        String[] fileNames = fileList();
        for (String fileName : fileNames) {
            Note note = new Note(getApplicationContext(), fileName);
            CardView card = generateNoteView(NotesList.this, note);
            notesLayout.addView(card);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    private void requestAlarmPermission() {
        if (!(ContextCompat.checkSelfPermission(this, android.Manifest.permission.SCHEDULE_EXACT_ALARM) == PackageManager.PERMISSION_GRANTED)) {
            int requestCode = UUID.randomUUID().hashCode();
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.SCHEDULE_EXACT_ALARM}, requestCode);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private void requestPostNotificaionPermission() {
        if (!(ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED)) {
            int requestCode = UUID.randomUUID().hashCode();
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, requestCode);
        }
    }

    private CardView generateNoteView(Context context, Note note) {
        TextView description = new TextView(context);
        if (note.hasTopic()) {
            description.setText(note.topic);
            description.setTypeface(null, Typeface.BOLD);
        } else if (note.hasText()) {
            description.setText(note.text);
        } else {
            description.setText("<Текст відсутній>");
        }
        LinearLayout.LayoutParams descriptionParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
                9.0f
        );
        descriptionParams.setMargins(0, 0, this.spToPx(10), 0);
        description.setLayoutParams(descriptionParams);
        description.setClickable(true);
        description.setEllipsize(TextUtils.TruncateAt.END);
        description.setGravity(Gravity.CENTER_VERTICAL);
        description.setSingleLine(false);
        description.setTextColor(getColor(R.color.white));
        description.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NoteEdit.class);
                intent.putExtra("filename", note.filename);
                startActivity(intent);
            }
        });

        View divider = new View(context);
        TextView notificationDatetime = new TextView(context);
        if (note.notify) {
            LinearLayout.LayoutParams dividerParams = new LinearLayout.LayoutParams(
                    1,
                    LinearLayout.LayoutParams.MATCH_PARENT
            );
            dividerParams.setMargins(0, 0, this.spToPx(10), 0);
            divider.setLayoutParams(dividerParams);
            divider.setBackgroundColor(getColor(R.color.white));

            LinearLayout.LayoutParams datetimeParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
            );
            datetimeParams.setMargins(0, 0, this.spToPx(10), 0);
            notificationDatetime.setLayoutParams(datetimeParams);
            Date datetime = new Date(note.notificationTimestamp);
            notificationDatetime.setText((String) android.text.format.DateFormat.format("dd.MM.yy\nHH:mm", datetime));
            notificationDatetime.setTextColor(getColor(R.color.white));
            notificationDatetime.setGravity(Gravity.CENTER_VERTICAL);
            notificationDatetime.setPadding(0, 0, 0, 0);
            notificationDatetime.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        }

        LinearLayout views = new LinearLayout(context);
        LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        rowParams.setMargins(
                this.spToPx(16),
                this.spToPx(12),
                this.spToPx(12),
                this.spToPx(12)
        );
        views.setLayoutParams(rowParams);
        views.setOrientation(LinearLayout.HORIZONTAL);
        views.setMinimumHeight(60);
        views.addView(description);
        if (note.notify) {
            views.addView(divider);
            views.addView(notificationDatetime);
        }

        CardView card = new CardView(context);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(
                this.spToPx(8),
                this.spToPx(4),
                this.spToPx(8),
                this.spToPx(4)
        );
        card.setLayoutParams(cardParams);
        card.setCardBackgroundColor(getColor(androidx.cardview.R.color.cardview_dark_background));
        card.setClickable(true);
        card.setRadius(this.spToPx(8));
        card.addView(views);

        return card;
    }

    private int spToPx(float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getApplicationContext().getResources().getDisplayMetrics());
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateNoteList();
    }
}