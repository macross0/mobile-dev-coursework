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

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

public class Note implements java.io.Serializable {
    private final transient Context context;
    public transient String filename;
    public String topic = "";
    public String text;
    public long modificationTimestamp;
    public long notificationTimestamp;
    public boolean notifyRegularly;

    public Note(Context context) {
        this.context = context;
        this.modificationTimestamp = System.currentTimeMillis();
        this.filename = String.format("%d", this.modificationTimestamp);
    }
    public Note(Context context, String filename) {
        this.context = context;
        this.filename = filename;
        this.read();
    }

    private void updateFromObject(Note object) {
        this.text = object.text;
        this.topic = object.topic;
        this.modificationTimestamp = object.modificationTimestamp;
        this.notificationTimestamp = object.notificationTimestamp;
        this.notifyRegularly = object.notifyRegularly;
    }

    public boolean read() {
        try {
            FileInputStream file = this.context.openFileInput(this.filename);
            ObjectInputStream objectInput = new ObjectInputStream(file);

            Note fileObjectBuffer = (Note) objectInput.readObject();
            updateFromObject(fileObjectBuffer);

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
            FileOutputStream file = this.context.openFileOutput(this.filename, this.context.MODE_PRIVATE);
            ObjectOutputStream objectOutput = new ObjectOutputStream(file);
            objectOutput.writeObject(this);
            file.close();
            objectOutput.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getModificationDateTime() {
        Date datetime = new Date(modificationTimestamp);
        return (String) android.text.format.DateFormat.format("dd.MM.yy\nHH:mm", datetime);
    }

    public String getDescription() {
        if (!this.topic.isEmpty()) return this.topic;
        return this.text;
    }

    public boolean hasTopic() {
        return !this.topic.isEmpty();
    }

    public boolean delete() {
        return this.context.deleteFile(this.filename);
    }
}