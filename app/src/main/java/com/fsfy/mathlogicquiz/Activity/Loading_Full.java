package com.fsfy.mathlogicquiz.Activity;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fsfy.mathlogicquiz.Database.DBase;
import com.fsfy.mathlogicquiz.Database.DbLevelInfo;
import com.fsfy.mathlogicquiz.R;

public class Loading_Full extends AppCompatActivity {
    private static DBase dBase;
    private TextView textViewText;

    public static Short isPage = 0;
    // 0 -> Main -> Game
    // 1 -> Main -> Sections
    // 2 -> Section > Game

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_loading);

        getSupportActionBar().hide();
        dBase = Room.databaseBuilder(this, DBase.class, "database").allowMainThreadQueries().build();

        textViewText = (TextView) findViewById(R.id.fullloading_text);
        switch (isPage) {
            case 0:
                textViewText.setText(getString(R.string.loading_level));
                break;
            case 1:
                textViewText.setText(getString(R.string.loading_levels));
                break;
            case 2:
                textViewText.setText(getString(R.string.loading_level));
                break;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        switch (isPage) {
                            case 0:
                                LoadSection();
                                break;
                            case 1:
                                LoadSections();
                                break;
                            case 2:
                                LoadSectionGame();
                                break;
                        }
                    }
                }, 300);
            }
        }).run();
    }

    private void LoadSection() {
        DbLevelInfo dbLevelInfo = dBase.dbDao().getLevelInfoLast();
        if (dbLevelInfo != null) {
            if (!dBase.dbDao().getLevelLast().getName().equals(String.valueOf(dbLevelInfo.getLevelId())) || (dBase.dbDao().getLevelLast().getName().equals(String.valueOf(dbLevelInfo.getLevelId())) && dBase.dbDao().getLevelInfo(Integer.valueOf(dBase.dbDao().getLevelLast().getName())).getFirstFindCount() == 0)) {

                QuizGame.dbLevels = dBase.dbDao().getLevelsByName(String.valueOf(dbLevelInfo.getLevelId()));
                startActivity(new Intent(Loading_Full.this, QuizGame.class));
                overridePendingTransition(R.anim.anim_start_activity, R.anim.anim_stop_activity);
                finish();
            } else {
                Toast.makeText(Loading_Full.this, getString(R.string.all_sections_complete), Toast.LENGTH_LONG).show();

                QuizGame.dbLevels = dBase.dbDao().getLevelsByName("1");
                startActivity(new Intent(Loading_Full.this, QuizGame.class));
                overridePendingTransition(R.anim.anim_start_activity, R.anim.anim_stop_activity);
                finish();
            }
        }
    }

    private void LoadSections() {
            startActivity(new Intent(Loading_Full.this, Levels.class));
            overridePendingTransition(R.anim.anim_start_activity, R.anim.anim_stop_activity);
            finish();
    }

    public static DbLevelInfo dbLevelInfo;

    private void LoadSectionGame() {
        if (dbLevelInfo != null) {
            if (dbLevelInfo.isDurum()) {

                Intent intent = new Intent(Loading_Full.this, QuizGame.class);
                QuizGame.dbLevels = dBase.dbDao().getLevelsByName(String.valueOf(dbLevelInfo.getLevelId()));
                startActivity(intent);
                overridePendingTransition(R.anim.anim_start_activity, R.anim.anim_stop_activity);
                finish();

            } else {
                Toast.makeText(Loading_Full.this, getString(R.string.locked), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(Loading_Full.this, getString(R.string.locked), Toast.LENGTH_SHORT).show();
        }
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
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
}
