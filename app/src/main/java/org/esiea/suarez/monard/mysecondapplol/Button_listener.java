package org.esiea.suarez.monard.mysecondapplol;

/**
 * Created by Bobby on 29/01/2018.
 */

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class Button_listener extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(intent.getExtras().getInt("id"));
        Toast.makeText(context, "Button notification called", Toast.LENGTH_SHORT).show();
    }
}
