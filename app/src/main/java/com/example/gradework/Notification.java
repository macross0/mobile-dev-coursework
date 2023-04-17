package com.example.gradework;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.UUID;

public class Notification extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
                String text = intent.getStringExtra("text");
                String topic = intent.getStringExtra("topic");
            android.app.Notification notification = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                notification = new android.app.Notification.Builder(context, "NoteAlerts").setSmallIcon(R.drawable.baseline_event_note_24_black)
                        .setContentText(text).setContentTitle(topic).build();
            }
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            int notificationId = UUID.randomUUID().hashCode();
            manager.notify(notificationId, notification);
    }
}
