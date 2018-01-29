package org.esiea.suarez.monard.mysecondapplol;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import static android.content.ContentValues.TAG;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class MotherService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_GET_MOTHERS = "org.esiea.suarez.monard.meetamother.action.GET_ALL_MOTHERS";

    public MotherService() {
        super("MotherService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionGetAllMothers(Context context) {
        Intent intent = new Intent(context, MotherService.class);
        intent.setAction(ACTION_GET_MOTHERS);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_GET_MOTHERS.equals(action)) {
                handleActionGetAllMothers();
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionGetAllMothers() {
        // TODO: Handle action Foo
        Log.i("tag", "Mothers received");
        URL mothersUrl = null;
        try {
            mothersUrl = new URL("https://randomuser.me/api/?results=500&gender=female&inc=name,location,email,phone,picture&nat=fr&format=json");
            HttpsURLConnection connection = (HttpsURLConnection) mothersUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            if(HttpsURLConnection.HTTP_OK == connection.getResponseCode()){
                copyInputStreamToFile(connection.getInputStream(), new File(getCacheDir(), "mothers.json"));
                Log.d(TAG, "The JSON of mothers's download has been successful");
            }
        } catch(MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(MainActivity.MOTHERS_UPDATE));
    }

    private void copyInputStreamToFile(InputStream in, File file){
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while((len=in.read(buf))>0){
                out.write(buf,0,len);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
