package com.fsfy.mathlogicquiz.Activity;

import android.content.Intent;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;

import com.fsfy.mathlogicquiz.Custom.Properties;
import com.fsfy.mathlogicquiz.Database.DbLevelInfo;
import com.fsfy.mathlogicquiz.R;
import com.plattysoft.leonids.ParticleSystem;

public class Tebrikler extends AppCompatActivity {

    private Button buttonAnasayfa, buttonBolumler;
    private ImageView imageviewStar1, imageviewStar2, imageviewStar3;

    public static DbLevelInfo dbLevelInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tebrikler);

        getSupportActionBar().hide();

        Properties.loadLevelUpSound(Tebrikler.this);

        ToolLoading();
    }

    private void ToolLoading() {
        buttonAnasayfa = (Button) findViewById(R.id.activitytebrikler_button_anasayfa);
        buttonBolumler = (Button) findViewById(R.id.activitytebrikler_button_bolumler);

        imageviewStar1 = (ImageView) findViewById(R.id.activitytebrikler_nextquest_star1);
        imageviewStar2 = (ImageView) findViewById(R.id.activitytebrikler_nextquest_star2);
        imageviewStar3 = (ImageView) findViewById(R.id.activitytebrikler_nextquest_star3);

        final ImageView[] imageviewList = {imageviewStar1, imageviewStar2, imageviewStar3};
        if (dbLevelInfo != null) {
            for (int i = 0; i < dbLevelInfo.getStarCount(); i++) {
                imageviewList[i].setImageResource(R.drawable.anim_star_on);
                ((TransitionDrawable) imageviewList[i].getDrawable()).startTransition(400);
            }
        }

        buttonAnasayfa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Properties.Vibration(Tebrikler.this, 60);

                startActivity(new Intent(Tebrikler.this, Main.class));
                overridePendingTransition(R.anim.anim_start_activity, R.anim.anim_stop_activity);
                finish();
            }
        });

        buttonBolumler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Properties.Vibration(Tebrikler.this, 60);

                startActivity(new Intent(Tebrikler.this, Levels.class));
                overridePendingTransition(R.anim.anim_start_activity, R.anim.anim_stop_activity);
                finish();
            }
        });

        handler.postDelayed(runnable, 100);
    }

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            LoadPartical();
        }
    };

    private void LoadPartical() {

        int time = 3500, partical = 250;

        ParticleSystem ps = new ParticleSystem(this, partical, R.drawable.star_pink, time);
        ps.setScaleRange(0.7f, 1.3f);
        ps.setSpeedRange(0.1f, 0.25f);
        ps.setRotationSpeedRange(90, 180);
        ps.setFadeOut(200, new AccelerateInterpolator());
        ps.oneShot(imageviewStar1, 70);

        ParticleSystem ps2 = new ParticleSystem(this, partical, R.drawable.star_white, time);
        ps2.setScaleRange(0.7f, 1.3f);
        ps2.setSpeedRange(0.1f, 0.25f);
        ps.setRotationSpeedRange(90, 180);
        ps2.setFadeOut(200, new AccelerateInterpolator());
        ps2.oneShot(imageviewStar1, 70);

        ps = new ParticleSystem(this, partical, R.drawable.star_pink, time);
        ps.setScaleRange(0.7f, 1.3f);
        ps.setSpeedRange(0.1f, 0.25f);
        ps.setRotationSpeedRange(90, 180);
        ps.setFadeOut(200, new AccelerateInterpolator());
        ps.oneShot(imageviewStar2, 70);

        ps2 = new ParticleSystem(this, partical, R.drawable.star_white, time);
        ps2.setScaleRange(0.7f, 1.3f);
        ps2.setSpeedRange(0.1f, 0.25f);
        ps.setRotationSpeedRange(90, 180);
        ps2.setFadeOut(200, new AccelerateInterpolator());
        ps2.oneShot(imageviewStar2, 70);

        ps = new ParticleSystem(this, partical, R.drawable.star_pink, time);
        ps.setScaleRange(0.7f, 1.3f);
        ps.setSpeedRange(0.1f, 0.25f);
        ps.setRotationSpeedRange(90, 180);
        ps.setFadeOut(200, new AccelerateInterpolator());
        ps.oneShot(imageviewStar3, 70);

        ps2 = new ParticleSystem(this, partical, R.drawable.star_white, time);
        ps2.setScaleRange(0.7f, 1.3f);
        ps2.setSpeedRange(0.1f, 0.25f);
        ps.setRotationSpeedRange(90, 180);
        ps2.setFadeOut(200, new AccelerateInterpolator());
        ps2.oneShot(imageviewStar3, 70);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();

        startActivity(new Intent(Tebrikler.this, Main.class));
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