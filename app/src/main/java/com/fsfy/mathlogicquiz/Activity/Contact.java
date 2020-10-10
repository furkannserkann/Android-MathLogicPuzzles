package com.fsfy.mathlogicquiz.Activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fsfy.mathlogicquiz.Custom.Properties;
import com.fsfy.mathlogicquiz.R;

public class Contact extends AppCompatActivity {
    Typeface myTypeface, myTypeface2, myTypeface3;

    private LinearLayout linearLayoutExit, linearLayoutSend;
    private EditText editTextName, editTextMessage;
    private TextView textViewName, textViewMessage, textViewSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        getSupportActionBar().hide();

        ToolLoading();
    }

    private void ToolLoading() {

        myTypeface2 = Typeface.createFromAsset(getAssets(), "Comfortaa[wght].ttf");
        myTypeface3 = Typeface.createFromAsset(getAssets(), "PatrickHand-Regular.ttf");
        myTypeface = Typeface.createFromAsset(getAssets(), "Righteous-Regular.ttf");


        linearLayoutExit = (LinearLayout) findViewById(R.id.contact_linear_backbutton);
        linearLayoutSend = (LinearLayout) findViewById(R.id.contact_linear_send);

        editTextName = (EditText) findViewById(R.id.contact_plaintext_name);
        editTextMessage = (EditText) findViewById(R.id.contact_plaintext_message);
//        editTextName.setTypeface(myTypeface2);
//        editTextMessage.setTypeface(myTypeface2);



        textViewName = (TextView) findViewById(R.id.contact_textview_name);
        textViewMessage = (TextView) findViewById(R.id.contact_textview_message);
        textViewSend = (TextView) findViewById(R.id.contact_textview_send);
//        textViewName.setTypeface(myTypeface);
//        textViewMessage.setTypeface(myTypeface);
//        textViewSend.setTypeface(myTypeface);

        linearLayoutExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Properties.Vibration(Contact.this, 60);

                startActivity(new Intent(Contact.this, Main.class));
                overridePendingTransition(R.anim.anim_start_activity, R.anim.anim_stop_activity);
                finish();
            }
        });

        linearLayoutSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Properties.Vibration(Contact.this, 60);

                Intent emailIntent = new Intent(android.content.Intent.ACTION_SENDTO);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));//Email konusu
                emailIntent.putExtra(Intent.EXTRA_TEXT, editTextName.getText() + ";\n\n" + editTextMessage.getText());//Email içeriği
                emailIntent.setData(Uri.parse("mailto:fsfy.software@yandex.com"));
                emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                try {
                    startActivity(Intent.createChooser(emailIntent, getString(R.string.email_select)));
                    Log.i("Finished sending...", "");
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(Contact.this, getString(R.string.your_email_has_been_not_sent), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        BottombarHide();
    }

    @Override
    protected void onResume() {
        super.onResume();
        BottombarHide();
    }

    private void BottombarHide() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();

        startActivity(new Intent(Contact.this, Main.class));
        overridePendingTransition(R.anim.anim_start_activity, R.anim.anim_stop_activity);
        finish();
    }


}
