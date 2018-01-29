package org.esiea.suarez.monard.mysecondapplol;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class MotherDetailsActivity extends AppCompatActivity {

    ImageView profilePicture;
    TextView motherName;
    TextView motherAddress;
    TextView motherPhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mother_details);

        this.profilePicture = (ImageView) findViewById(R.id.profile_picture);
        this.motherName = (TextView) findViewById(R.id.name_mother);
        this.motherAddress = (TextView) findViewById(R.id.address_mother);
        this.motherPhone = (TextView) findViewById(R.id.phone_mother);

        this.motherName.setText(getIntent().getStringExtra("MOTHER_NAME"));
        this.motherAddress.setText(getIntent().getStringExtra("MOTHER_ADDRESS"));
        this.motherPhone.setText(getIntent().getStringExtra("PHONE_MOTHER"));
        Glide.with(this).load(getIntent().getStringExtra("IMAGE_URL")).into(this.profilePicture);
    }
}
