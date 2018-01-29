package org.esiea.suarez.monard.mysecondapplol;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    public static final String MOTHERS_UPDATE = "org.esiea.suarez.monard.meetamother";
    public JSONArray mothers;
    private RecyclerView recyclerView;
    private NotificationCompat.Builder builder;
    private NotificationManager notificationManager;
    private int notification_id;
    private RemoteViews remoteViews;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Intent intent = new Intent(this, MotherDetailsActivity.class);

        MotherService.startActionGetAllMothers(this);
        IntentFilter intentFilter = new IntentFilter(MOTHERS_UPDATE);
        LocalBroadcastManager.getInstance(this).registerReceiver(new MothersUpdate(), intentFilter);

        this.recyclerView = (RecyclerView) findViewById(R.id.rv_mothers);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        this.recyclerView.setAdapter(new MothersAdapter(getMothersFromFile()));
        this.recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, this.recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                try {
                    String profileUrl = mothers.getJSONObject(position).getJSONObject("picture").getString("large");
                    String motherName = mothers.getJSONObject(position).getJSONObject("name").getString("first");
                    motherName = motherName.substring(0,1).toUpperCase() + motherName.substring(1).toLowerCase();
                    String motherCity = mothers.getJSONObject(position).getJSONObject("location").getString("city");
                    motherCity = motherCity.toUpperCase();
                    String phoneMother = mothers.getJSONObject(position).getString("phone");
                    intent.putExtra("IMAGE_URL", profileUrl);
                    intent.putExtra("MOTHER_NAME", motherName);
                    intent.putExtra("MOTHER_ADDRESS", motherCity);
                    intent.putExtra("PHONE_MOTHER", phoneMother);

                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        context = this;
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        remoteViews = new RemoteViews(getPackageName(), R.layout.notification_layout);

        notification_id = (int) System.currentTimeMillis();
        Intent button_intent = new Intent("button_clicked");
        button_intent.putExtra("id", notification_id);

        PendingIntent p_button_intent = PendingIntent.getBroadcast(context, 123, button_intent, 0);
        remoteViews.setOnClickPendingIntent(R.id.notif_button, p_button_intent);

        findViewById(R.id.notif_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent notification_intent = new Intent(context, MainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notification_intent, 0);

                builder = new NotificationCompat.Builder(context);
                builder.setSmallIcon(R.drawable.ic_launcher_background).setAutoCancel(true).setCustomContentView(remoteViews).setContentIntent(pendingIntent);

            @Override
            public void onItemLongClick(View view, int position) {
            }
        }));
                notificationManager.notify(notification_id, builder.build());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.menu, menu);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_bar_button:
                // User chose the "Settings" item, show the app settings UI...
                startActivity(new Intent(this, Informations.class));
                Toast.makeText(getApplicationContext(),getString(R.string.toast), Toast.LENGTH_LONG).show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class MothersUpdate extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("About to be updated", getIntent().getAction());
            mothers = getMothersFromFile();
        }
    }

    public JSONArray getMothersFromFile() {
        try {
            InputStream inputStream = new FileInputStream(getCacheDir() + "/" + "mothers.json");
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();
            JSONObject result = new JSONObject(new String(buffer, "UTF-8"));
            return result.getJSONArray("results");
        } catch(IOException e) {
            e.printStackTrace();
            return new JSONArray();
        } catch(JSONException e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }

    private class MothersAdapter extends RecyclerView.Adapter<MothersAdapter.MothersHolder> {
        JSONArray mothers;

        public MothersAdapter(JSONArray data) {
            this.mothers = data;
        }

        @Override
        public MothersHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View v = (View) layoutInflater.inflate(R.layout.rv_mothers_element, null);
            MothersHolder motherHolder = new MothersHolder(v);
            return motherHolder;
        }
        @Override
        public void onBindViewHolder(MothersHolder holder, int position) {
            try {
                String profileUrl = mothers.getJSONObject(position).getJSONObject("picture").getString("large");
                String motherName = mothers.getJSONObject(position).getJSONObject("name").getString("first");
                motherName = motherName.substring(0,1).toUpperCase() + motherName.substring(1).toLowerCase();
                String motherCity = mothers.getJSONObject(position).getJSONObject("location").getString("city");
                motherCity = motherCity.toUpperCase();
                String phoneMother = mothers.getJSONObject(position).getString("phone");
                holder.tv_name.setText(motherName);
                holder.tv_address.setText(motherCity);
                holder.tv_phone.setText(phoneMother);

                Glide.with(holder.itemView.getContext())
                        .load(profileUrl)
                        .into(holder.iv_profilePicture);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        @Override
        public int getItemCount() {
            return mothers.length();
        }

        public void setNewMother(JSONArray newMothers) {
            mothers = newMothers;
            notifyDataSetChanged();
        }

        public class MothersHolder extends RecyclerView.ViewHolder {

            ImageView iv_profilePicture;
            TextView tv_name;
            TextView tv_address;
            TextView tv_phone;

            public MothersHolder(View itemView) {
                super(itemView);
                this.iv_profilePicture = itemView.findViewById(R.id.profile_picture);
                this.tv_name = itemView.findViewById(R.id.name);
                this.tv_address = itemView.findViewById(R.id.address);
                this.tv_phone = itemView.findViewById(R.id.phone);
            }
        }
    }
}
