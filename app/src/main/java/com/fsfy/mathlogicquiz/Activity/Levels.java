package com.fsfy.mathlogicquiz.Activity;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.fsfy.mathlogicquiz.Adapter.LevelsGridAdapter;
import com.fsfy.mathlogicquiz.Custom.AnalyticsApplication;
import com.fsfy.mathlogicquiz.Custom.Properties;
import com.fsfy.mathlogicquiz.Database.DBase;
import com.fsfy.mathlogicquiz.Database.DbLevelInfo;
import com.fsfy.mathlogicquiz.Database.DbLevels;
import com.fsfy.mathlogicquiz.R;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.List;

public class Levels extends AppCompatActivity {

    private GridView gridView;
//    private TextView textViewActionHead;
    private LinearLayout linearLayoutBackButton;

//    private Typeface typeface1;

    private DBase dBase;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_gridview);

        getSupportActionBar().hide();
        dBase = Room.databaseBuilder(this, DBase.class, "database").allowMainThreadQueries().build();


        gridView = (GridView) findViewById(R.id.item_gridview_gridview);
        linearLayoutBackButton = (LinearLayout) findViewById(R.id.itemgridview_linear_backbutton);


        if (Properties.isOnline(Levels.this)) {
            Tracker t = ((AnalyticsApplication) getApplication()).getTracker(AnalyticsApplication.TrackerName.APP_TRACKER);
            t.setScreenName("Levels"); //Hangi activityde olduğunu bildiriyor
            t.send(new HitBuilders.ScreenViewBuilder().build());
        }


        final List<DbLevels> dbLevelsList = dBase.dbDao().getLevels();
        if (dbLevelsList != null) {
            LevelsGridAdapter levelsGridAdapter = new LevelsGridAdapter(this, dbLevelsList, dBase, this);
            gridView.setAdapter(levelsGridAdapter);

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Properties.Vibration(Levels.this, 60);

                    DbLevelInfo dbLevelInfo = dBase.dbDao().getLevelInfo(Integer.valueOf(dbLevelsList.get(position).getName()));
                    if (dbLevelInfo != null) {
                        if (dbLevelInfo.isDurum()) {
                            Loading_Full.isPage = 2;
                            Loading_Full.dbLevelInfo = dbLevelInfo;
//                            Loading_Full.SectionGamedbLevelsList = dbLevelsList;
//                            Loading_Full.SectionGameposition = position;

                            startActivity(new Intent(Levels.this, Loading_Full.class));
                            overridePendingTransition(R.anim.anim_start_activity, R.anim.anim_stop_activity);
                            finish();
                        } else {
                            Toast.makeText(Levels.this, getString(R.string.locked), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Levels.this, getString(R.string.locked), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        linearLayoutBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Properties.Vibration(Levels.this, 60);

                startActivity(new Intent(Levels.this, Main.class));
                overridePendingTransition(R.anim.anim_start_activity, R.anim.anim_stop_activity);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();

        startActivity(new Intent(Levels.this, Main.class));
        overridePendingTransition(R.anim.anim_start_activity, R.anim.anim_stop_activity);
        finish();
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
