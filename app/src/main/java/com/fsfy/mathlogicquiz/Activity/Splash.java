package com.fsfy.mathlogicquiz.Activity;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.fsfy.mathlogicquiz.Custom.Properties;
import com.fsfy.mathlogicquiz.Database.DBase;
import com.fsfy.mathlogicquiz.Database.DbLevelInfo;
import com.fsfy.mathlogicquiz.Database.DbLevels;
import com.fsfy.mathlogicquiz.R;

import org.json.JSONArray;
import org.json.JSONException;

public class Splash extends AppCompatActivity {

    private TextView textViewHead1, textViewHead2;
    private ImageView imageView;

    private Typeface myTypeface;

    private static DBase dBase;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        dBase = Room.databaseBuilder(getApplicationContext(), DBase.class, "database").addMigrations(Properties.MIGRATION_1_2).allowMainThreadQueries().build();

        myTypeface = Typeface.createFromAsset(getAssets(), "Comfortaa[wght].ttf");

        textViewHead1 = (TextView) findViewById(R.id.activitysplash_textview_head);
        textViewHead1.setTypeface(myTypeface);

        textViewHead2 = (TextView) findViewById(R.id.activitysplash_textview_head2);
        textViewHead2.setTypeface(myTypeface);

        imageView = (ImageView) findViewById(R.id.activitysplash_imageview_icon);

        imageView.setImageResource(R.drawable.anim_splash);
        ((TransitionDrawable) imageView.getDrawable()).startTransition(2000);


        final AlphaAnimation animation1 = new AlphaAnimation(0.0f, 1.0f);
        animation1.setDuration(1000);
        animation1.setStartOffset(250);
        textViewHead1.startAnimation(animation1);
        textViewHead2.startAnimation(animation1);


        new Thread(new Runnable() {
            @Override
            public void run() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                LoadLevels(Properties.LoadLocalJson(Splash.this));
                            }
                        }).start();

                    }
                }, 1500);
            }
        }).run();
    }

    @Override
    public void onBackPressed() {

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
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    private void LoadLevels(String json) {
        if (json != null) {
            try {
                JSONArray obj = new JSONArray(json);
                Integer sz = dBase.dbDao().getLevels().size();

                if (obj.length() != sz) {
                    dBase.dbDao().deleteLevels();

                    for (int i = 0; i < obj.length(); i++) {
                        DbLevels dbLevels = new DbLevels();

                        if (obj.getJSONObject(i).get("name") != null) {
                            dbLevels.setName(obj.getJSONObject(i).get("name").toString());
                        }

                        if (obj.getJSONObject(i).get("image") != null) {
                            dbLevels.setImage(obj.getJSONObject(i).get("image").toString());
                        }

                        if (obj.getJSONObject(i).get("solution") != null) {
                            dbLevels.setSolution(obj.getJSONObject(i).get("solution").toString());
                        }

                        dBase.dbDao().addLevels(dbLevels);
                    }


                    if (dBase.dbDao().getLevelInfoLast() != null) {
                        if (sz == dBase.dbDao().getLevelInfoLast().getLevelId()) {
                            DbLevelInfo dbLevelInfo = new DbLevelInfo();
                            dbLevelInfo.setDurum(true);
                            dbLevelInfo.setLevelId(sz + 1);

                            if (dBase.dbDao().getLevelInfo(dbLevelInfo.getLevelId()) == null) {
                                dbLevelInfo.setFirstFindCount(0);
                                //                                dbLevelInfo.setTotalTrialCount(0);
                                dbLevelInfo.setStarCount(0);
                                dbLevelInfo.setHeart(sz + 1 % 10 == 0 ? QuizGame._minHeart : QuizGame._maxHeart);

                                dBase.dbDao().addLevelInfo(dbLevelInfo);
                            }
                        }
                    } else {
                        DbLevelInfo dbLevelInfo = new DbLevelInfo();
                        dbLevelInfo.setDurum(true);
                        dbLevelInfo.setLevelId(sz + 1);

                        if (dBase.dbDao().getLevelInfo(dbLevelInfo.getLevelId()) == null) {
                            dbLevelInfo.setFirstFindCount(0);
                            //                            dbLevelInfo.setTotalTrialCount(0);
                            dbLevelInfo.setStarCount(0);
                            dbLevelInfo.setHeart(sz + 1 % 10 == 0 ? QuizGame._minHeart : QuizGame._maxHeart);

                            dBase.dbDao().addLevelInfo(dbLevelInfo);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Intent mainIntent = new Intent(Splash.this, Main.class);
        Splash.this.startActivity(mainIntent);
        overridePendingTransition(R.anim.anim_start_activity, R.anim.anim_stop_activity);
        Splash.this.finish();
    }
}
