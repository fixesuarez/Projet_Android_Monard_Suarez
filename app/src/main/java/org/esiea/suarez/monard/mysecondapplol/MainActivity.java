package org.esiea.suarez.monard.mysecondapplol;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MotherService.startActionGetAllMothers(this);
        IntentFilter intentFilter = new IntentFilter(MOTHERS_UPDATE);
        LocalBroadcastManager.getInstance(this).registerReceiver(new MothersUpdate(), intentFilter);

        this.recyclerView = (RecyclerView) findViewById(R.id.rv_mothers);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        this.recyclerView.setAdapter(new MothersAdapter(getMothersFromFile()));
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
                String motherAddress = mothers.getJSONObject(position).getJSONObject("location").getString("city");
                holder.tv_name.setText(motherName);
                holder.tv_adress.setText(motherAddress);

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
            TextView tv_adress;

            public MothersHolder(View itemView) {
                super(itemView);
                this.iv_profilePicture = itemView.findViewById(R.id.profile_picture);
                this.tv_name = itemView.findViewById(R.id.name);
                this.tv_adress = itemView.findViewById(R.id.address);
            }
        }
    }
}
