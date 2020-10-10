package com.fsfy.mathlogicquiz.Activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fsfy.mathlogicquiz.Custom.AnalyticsApplication;
import com.fsfy.mathlogicquiz.Custom.Properties;
import com.fsfy.mathlogicquiz.Database.DBase;
import com.fsfy.mathlogicquiz.Database.DbLevelInfo;
import com.fsfy.mathlogicquiz.Database.DbLevels;
import com.fsfy.mathlogicquiz.R;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.json.JSONArray;
import org.json.JSONException;

public class Main extends AppCompatActivity {

    Typeface myTypeface, myTypeface2, myTypeface3;

    private TextView textviewActionHead;
    private TextView textViewStartGame, textViewLevels, textViewRestart, textViewShare, textViewVote, textViewExit, textViewContact;
    private LinearLayout linearLayoutStartGame, linearLayoutLevels, linearLayoutRestart, linearLayoutShare, linearLayoutVote, linearLayoutExit, linearLayoutContact;
    private ImageView imageViewSoundCheck, imageViewVibrationCheck;
    private SharedPreferences sharedPreferences;

    private static DBase dBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
        dBase = Room.databaseBuilder(this, DBase.class, "database").allowMainThreadQueries().build();

        sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);

        myTypeface2 = Typeface.createFromAsset(getAssets(), "Comfortaa[wght].ttf");
        myTypeface3 = Typeface.createFromAsset(getAssets(), "PatrickHand-Regular.ttf");
        myTypeface = Typeface.createFromAsset(getAssets(), "Righteous-Regular.ttf");

        if (Properties.isOnline(Main.this)) {
            Tracker t = ((AnalyticsApplication) getApplication()).getTracker(AnalyticsApplication.TrackerName.APP_TRACKER);
            t.setScreenName("Main Activity"); //Hangi activityde olduğunu bildiriyor
            t.send(new HitBuilders.ScreenViewBuilder().build());
        }

        ToolPresent();
        SoundAndVibrationCheck();
    }

    private void ToolPresent() {
        textviewActionHead = (TextView) findViewById(R.id.main_textview_actionhead);
//        textviewActionHead.setTypeface(myTypeface);

        imageViewSoundCheck = (ImageView) findViewById(R.id.main_imageviewsound);
        imageViewVibrationCheck = (ImageView) findViewById(R.id.main_imageview_vibration);

        linearLayoutStartGame = (LinearLayout) findViewById(R.id.main_linear_startgame);
        linearLayoutLevels = (LinearLayout) findViewById(R.id.main_linear_levels);
        linearLayoutRestart = (LinearLayout) findViewById(R.id.main_linear_restart);
        linearLayoutShare = (LinearLayout) findViewById(R.id.main_linear_share);
        linearLayoutVote = (LinearLayout) findViewById(R.id.main_linear_vote);
        linearLayoutExit = (LinearLayout) findViewById(R.id.main_linear_exit);
        linearLayoutContact = (LinearLayout) findViewById(R.id.main_linear_contact);
//        linearLayoutSoundCheck = (LinearLayout) findViewById(R.id.main_linear_soundscheck);


        textViewStartGame = (TextView) findViewById(R.id.main_textview_start);
        textViewLevels = (TextView) findViewById(R.id.main_textview_levels);
        textViewRestart = (TextView) findViewById(R.id.main_textview_restart);
        textViewShare = (TextView) findViewById(R.id.main_textview_share);
        textViewVote = (TextView) findViewById(R.id.main_textview_vote);
        textViewExit = (TextView) findViewById(R.id.main_textview_exit);
        textViewContact = (TextView) findViewById(R.id.main_textview_contact);

        linearLayoutStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Properties.Vibration(Main.this, 60);

                Loading_Full.isPage = 0;
                startActivity(new Intent(Main.this, Loading_Full.class));
                overridePendingTransition(R.anim.anim_start_activity, R.anim.anim_stop_activity);
                finish();
            }
        });

        linearLayoutLevels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Properties.Vibration(Main.this, 60);

                Loading_Full.isPage = 1;
                startActivity(new Intent(Main.this, Loading_Full.class));
                overridePendingTransition(R.anim.anim_start_activity, R.anim.anim_stop_activity);
                finish();
            }
        });

        linearLayoutExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Properties.Vibration(Main.this, 60);
                showMyCustomAlertDialog(getString(R.string.exit), getString(R.string.cancel), getString(R.string.are_you_sure_you_want_to_exit), (short) 2, R.drawable.exit);
            }
        });

        linearLayoutRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Properties.Vibration(Main.this, 60);
                showMyCustomAlertDialog(getString(R.string.restart), getString(R.string.cancel), getString(R.string.are_you_sure_you_want_to_restart), (short) 1, R.drawable.ic_restart);
            }
        });

        imageViewSoundCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Properties.SoundCheck) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isSound", false);
                    editor.apply();

                    Properties.SoundCheck = false;
                    imageViewSoundCheck.setImageResource(R.drawable.ic_sound_off);
                } else {
                    Properties.SoundCheck = true;
                    Properties.loadClickSound(Main.this);

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isSound", true);
                    editor.apply();

                    imageViewSoundCheck.setImageResource(R.drawable.ic_sound_on);
                }
            }
        });

        if (Properties.VibrationCheck) {
            imageViewVibrationCheck.setImageResource(R.drawable.ic_vibration_on);
        } else {
            imageViewVibrationCheck.setImageResource(R.drawable.ic_vibration_off);
        }

        imageViewVibrationCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Properties.VibrationCheck) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isVibration", false);
                    editor.apply();

                    Properties.VibrationCheck = false;
                    imageViewVibrationCheck.setImageResource(R.drawable.ic_vibration_off);
                } else {
                    Properties.VibrationCheck = true;
                    Properties.Vibration(Main.this, 250);

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isVibration", true);
                    editor.apply();

                    imageViewVibrationCheck.setImageResource(R.drawable.ic_vibration_on);
                }
            }
        });

        linearLayoutShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Properties.Vibration(Main.this, 60);

                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.fsfy.mathlogicquiz");
                startActivity(Intent.createChooser(sharingIntent, getString(R.string.share)));
            }
        });

        linearLayoutVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Properties.Vibration(Main.this, 60);

                Uri webpage = Uri.parse("https://play.google.com/store/apps/details?id=com.fsfy.mathlogicquiz");
                Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
                startActivity(webIntent);
            }
        });

        linearLayoutContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Properties.Vibration(Main.this, 60);

                startActivity(new Intent(Main.this, Contact.class));
                overridePendingTransition(R.anim.anim_start_activity, R.anim.anim_stop_activity);
                finish();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    public void showMyCustomAlertDialog(String posMessage, String NegMessage, String baslik, final short btnKaydetOperation, int image) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog);

        Button btnKaydet = (Button) dialog.findViewById(R.id.button_kaydet);
        final Button btnIptal = (Button) dialog.findViewById(R.id.button_iptal);
        TextView tvBaslik = (TextView) dialog.findViewById(R.id.textview_baslik);
        ImageView ivResim = (ImageView) dialog.findViewById(R.id.imageview_resim);

        btnKaydet.setText(posMessage);
        btnIptal.setText(NegMessage);

        tvBaslik.setText(baslik);
        ivResim.setImageResource(image);

        btnKaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Properties.Vibration(Main.this, 60);

                switch (btnKaydetOperation) {
                    case 1:
                        LoadLevels(Properties.LoadLocalJson(Main.this));

                        DbLevelInfo dbLevelInfo = new DbLevelInfo();
                        dbLevelInfo.setDurum(true);
                        dbLevelInfo.setLevelId(1);
                        dbLevelInfo.setFirstFindCount(0);
//                        dbLevelInfo.setTotalTrialCount(0);
                        dbLevelInfo.setStarCount(0);
                        dbLevelInfo.setHeart(QuizGame._maxHeart);
                        dBase.dbDao().deleteLevelInfo();

                        if (dBase.dbDao().getLevelInfo(dbLevelInfo.getLevelId()) == null) {
                            dBase.dbDao().addLevelInfo(dbLevelInfo);
                        }

                        dialog.dismiss();
                        break;
                    case 2:
                        System.exit(1);
                        break;
                }
            }
        });

        btnIptal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Properties.Vibration(Main.this, 60);

                dialog.dismiss();
            }
        });


        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        dialog.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
    }

    private void LoadLevels(String json) {
        if (json != null) {
            try {
                JSONArray obj = new JSONArray(json);
                Integer sz = dBase.dbDao().getLevels().size();

                if (obj.length() != sz) {
//                    dBase.dbDao().deleteLevels();

                    for (int i = sz; i < obj.length(); i++) {
                        DbLevels dbLevels = new DbLevels();

                        if (obj.getJSONObject(i).get("name") != null)
                            dbLevels.setName(obj.getJSONObject(i).get("name").toString());

                        if (obj.getJSONObject(i).get("image") != null) {
                            dbLevels.setImage(obj.getJSONObject(i).get("image").toString());
                        }

                        if (obj.getJSONObject(i).get("solution") != null)
                            dbLevels.setSolution(obj.getJSONObject(i).get("solution").toString());

                        dBase.dbDao().addLevels(dbLevels);
                    }


                    DbLevelInfo dbLevelInfo = new DbLevelInfo();
                    dbLevelInfo.setDurum(true);
                    dbLevelInfo.setLevelId(sz + 1);

                    if (dBase.dbDao().getLevelInfo(dbLevelInfo.getLevelId()) == null) {
                        dbLevelInfo.setFirstFindCount(0);
//                        dbLevelInfo.setTotalTrialCount(0);
                        dbLevelInfo.setStarCount(0);
                        dbLevelInfo.setHeart(QuizGame._maxHeart);

                        dBase.dbDao().addLevelInfo(dbLevelInfo);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void SoundAndVibrationCheck() {

        if (sharedPreferences.getBoolean("isSound", true)) {
            Properties.SoundCheck = true;
            imageViewSoundCheck.setImageResource(R.drawable.ic_sound_on);
        } else {
            Properties.SoundCheck = false;
            imageViewSoundCheck.setImageResource(R.drawable.ic_sound_off);
        }

        if (sharedPreferences.getBoolean("isVibration", true)) {
            Properties.VibrationCheck = true;
            imageViewVibrationCheck.setImageResource(R.drawable.ic_vibration_on);
        } else {
            Properties.VibrationCheck = false;
            imageViewVibrationCheck.setImageResource(R.drawable.ic_vibration_off);
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isSound", Properties.SoundCheck);
        editor.putBoolean("isVibration", Properties.VibrationCheck);
        editor.apply();

    }

    @Override
    public void onBackPressed() {
        showMyCustomAlertDialog(getString(R.string.exit), getString(R.string.cancel), getString(R.string.are_you_sure_you_want_to_exit), (short) 2, R.drawable.exit);
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
}