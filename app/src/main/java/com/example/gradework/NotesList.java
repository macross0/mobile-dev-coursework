package com.example.gradework;

import static android.view.View.TEXT_ALIGNMENT_CENTER;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcel;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
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

public class NotesList extends AppCompatActivity {

    public ScrollView notesScrollView;
    public LinearLayout notesLayout;
    Map<Integer, Note> notes = new ArrayMap<Integer, Note>();

    LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes_list);
        setTitle(R.string.app_name);

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
        for (int i = 0; i < fileNames.length; i++) {
            Note note = new Note(getApplicationContext(), fileNames[i]);

            CardView card = generateNoteView(NotesList.this, note);
            notesLayout.addView(card);
        }
    }
//            description.setOnClickListener(new View.OnClickListener() {
//                @Override public void onClick(View v) {
//                    Bundle include = new Bundle();
//                    Intent intent = new Intent(getApplicationContext(), NoteEdit.class);
//                    intent.putExtra("note", finalNote);
//                    startActivity(intent);
//                }
//            });
//            TextView datetime = card.findViewById(R.id.notificationDatetime);
////            Switch enable_switch = card.findViewById(R.id.enableSwitch);
//
//            description.setText(note.getDescription());
//            if (note.hasTopic()) {
//                description.setTypeface(null, Typeface.BOLD);
//            }
//            datetime.setText(note.getModificationDateTime());
//        }

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
//    }

    private CardView generateNoteView(Context context, Note note) {
        TextView description = new TextView(context);
        if (note.hasTopic()) {
            description.setText(note.topic);
            description.setTypeface(null, Typeface.BOLD);
        } else {
            description.setText(note.text);
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
                Bundle include = new Bundle();
                Intent intent = new Intent(getApplicationContext(), NoteEdit.class);
                intent.putExtra("filename", note.filename);
                startActivity(intent);
            }
        });

        View divider = new View(context);
        LinearLayout.LayoutParams dividerParams = new LinearLayout.LayoutParams(
                1,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        dividerParams.setMargins(0, 0, this.spToPx(10), 0);
        divider.setLayoutParams(dividerParams);
        divider.setBackgroundColor(getColor(R.color.white));

        TextView notificationDatetime = new TextView(context);
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

        Switch enableSwitch = new Switch(context);
        LinearLayout.LayoutParams switchParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        switchParams.gravity = Gravity.CENTER;
        enableSwitch.setLayoutParams(switchParams);
        enableSwitch.setPadding(0, this.spToPx(8), 0, this.spToPx(8));

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
        views.addView(description);
        views.addView(divider);
        views.addView(notificationDatetime);
        views.addView(enableSwitch);

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
}