package com.example.gradework;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

public class Note implements java.io.Serializable {
    public transient Context context;
    public transient String filename;
    private static final long serialVersionUID = 6529685098267757690L;;
    public String topic = "";
    public String text = "";
    public long notificationTimestamp;
    public boolean notify = false;

    public Note(Context context) {
        this.filename = String.format("%d", System.currentTimeMillis()) + ".note";
        this.context = context;
    }
    public Note(Context context, String filename) {
        this.context = context;
        this.filename = filename;
        this.read();
    }

    private void updateFromObject(Context context, Note object) {
        this.text = object.text;
        this.topic = object.topic;
        this.notificationTimestamp = object.notificationTimestamp;
        this.notify = object.notify;
        this.context = context;
    }

    public boolean read() {
        try {
            FileInputStream file = this.context.openFileInput(this.filename);
            ObjectInputStream objectInput = new ObjectInputStream(file);

            Note fileObjectBuffer = (Note) objectInput.readObject();
            updateFromObject(this.context, fileObjectBuffer);

            file.close();
            objectInput.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void write() {
        try {
            FileOutputStream file = this.context.openFileOutput(this.filename, Context.MODE_PRIVATE);
            ObjectOutputStream objectOutput = new ObjectOutputStream(file);
            objectOutput.writeObject(this);
            file.close();
            objectOutput.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean hasTopic() {
        return !this.topic.isEmpty();
    }

    public boolean hasText() {
        return !this.text.isEmpty();
    }
}