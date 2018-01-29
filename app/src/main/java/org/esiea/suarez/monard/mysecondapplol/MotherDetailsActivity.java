package org.esiea.suarez.monard.mysecondapplol;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

        Button btn = (Button)findViewById(R.id.open_phone);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+motherPhone.getText()));
                startActivity(intent);
            }
        });
    }
}
