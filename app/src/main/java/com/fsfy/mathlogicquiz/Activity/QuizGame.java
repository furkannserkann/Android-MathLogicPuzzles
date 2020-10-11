package com.fsfy.mathlogicquiz.Activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fsfy.mathlogicquiz.Custom.AnalyticsApplication;
import com.fsfy.mathlogicquiz.Custom.Properties;
import com.fsfy.mathlogicquiz.Database.DBase;
import com.fsfy.mathlogicquiz.Database.DbLevelInfo;
import com.fsfy.mathlogicquiz.Database.DbLevels;
import com.fsfy.mathlogicquiz.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.io.File;

//import com.squareup.picasso.Picasso;

public class QuizGame extends AppCompatActivity {
    public static final int _maxHeart = 3, _minHeart = 1;

    public static DbLevels dbLevels;

    private TextView textviewActionHead, textviewSolution, textviewWarning;
    private LinearLayout linearLayoutBackButton, linearLayoutWarning;
    private ImageView imageviewQuestion, buttonEnter, imageviewDelete;
    public static ProgressBar progressBarAds;

    private ImageView imageviewHeart1, imageviewHeart2, imageviewHeart3, imageviewHeart4,imageviewHeart5, imageviewSolution;
    private ImageView[] imagelistHeart = new ImageView[_maxHeart];

    private Button buttonNum1, buttonNum2, buttonNum3, buttonNum4, buttonNum5, buttonNum6, buttonNum7, buttonNum8, buttonNum9,
            buttonNum0;

    private DBase dBase;
    private Integer _TrialCount = 0;
    private Integer _StarCount = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizgame);

        Log.e("--------------------", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));

        getSupportActionBar().hide();
        dBase = Room.databaseBuilder(this, DBase.class, "database").allowMainThreadQueries().build();

        ToolPresent();
        if (Properties.isOnline(QuizGame.this)) {
            Tracker t = ((AnalyticsApplication) getApplication()).getTracker(AnalyticsApplication.TrackerName.APP_TRACKER);
            t.setScreenName("Question"); //Hangi activityde olduğunu bildiriyor
            t.send(new HitBuilders.ScreenViewBuilder().build());

//            FullscreenAd();
        }

        loadQuestions();
    }

    private void ClickNumber(String Number) {
        if (textviewSolution.getText() != null) {
            String text = String.valueOf(textviewSolution.getText());

            if (!text.equals("")) {
                if (text.length() < 6) {
                    textviewSolution.setText(text + Number);
                }
            } else {
                textviewSolution.setText(Number);
            }
        } else {
            textviewSolution.setText(Number);
        }
    }

    private void ToolPresent() {
        textviewActionHead = (TextView) findViewById(R.id.quizgame_textview_actionhead);
        textviewSolution = (TextView) findViewById(R.id.quizgame_edittext_solution);
        textviewWarning = (TextView) findViewById(R.id.activityquizgame_textview_warning);

        linearLayoutBackButton = (LinearLayout) findViewById(R.id.quizgame_linear_backbutton);
        linearLayoutWarning = (LinearLayout) findViewById(R.id.activityquizgame_linearlayout_warning);
        linearLayoutWarning.setVisibility(View.GONE);

        imageviewQuestion = (ImageView) findViewById(R.id.quizgame_imageview_question);
        imageviewHeart1 = (ImageView) findViewById(R.id.activityquizgame_imageview_heart1);
        imageviewHeart2 = (ImageView) findViewById(R.id.activityquizgame_imageview_heart2);
        imageviewHeart3 = (ImageView) findViewById(R.id.activityquizgame_imageview_heart3);
        imageviewSolution = (ImageView) findViewById(R.id.quizgame_imageview_solution);

        progressBarAds = (ProgressBar) findViewById(R.id.activityquizghame_progress_ads);
        progressBarAds.setVisibility(View.GONE);

        imagelistHeart[0] = imageviewHeart1;
        imagelistHeart[1] = imageviewHeart2;
        imagelistHeart[2] = imageviewHeart3;
//        imagelistHeart[3] = imageviewHeart4;
//        imagelistHeart[4] = imageviewHeart5;

        buttonNum1 = (Button) findViewById(R.id.button1);
        buttonNum2 = (Button) findViewById(R.id.button2);
        buttonNum3 = (Button) findViewById(R.id.button3);
        buttonNum4 = (Button) findViewById(R.id.button4);
        buttonNum5 = (Button) findViewById(R.id.button5);
        buttonNum6 = (Button) findViewById(R.id.button6);
        buttonNum7 = (Button) findViewById(R.id.button7);
        buttonNum8 = (Button) findViewById(R.id.button8);
        buttonNum9 = (Button) findViewById(R.id.button9);
        buttonNum0 = (Button) findViewById(R.id.button0);
        buttonEnter = (ImageView) findViewById(R.id.quizgame_button_enter);
        imageviewDelete = (ImageView) findViewById(R.id.activityquizgame_imageview_delete);

        linearLayoutBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Properties.Vibration(QuizGame.this, 60);

                Loading_Full.isPage = 1;
                startActivity(new Intent(QuizGame.this, Loading_Full.class));
                overridePendingTransition(R.anim.anim_start_activity, R.anim.anim_stop_activity);
                finish();
            }
        });

        imageviewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Properties.Vibration(QuizGame.this, 50);
                textviewSolution.setText("");
            }
        });

        buttonEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dBase.dbDao().getLevelInfo(Integer.valueOf(dbLevels.getName())).getHeart() <= 0) {
                    showMyHeartDialog();
                } else {
                    _TrialCount++;

                    DbLevelInfo dbLevelInfoTrial = dBase.dbDao().getLevelInfo(Integer.valueOf(dbLevels.getName()));

                    dbLevelInfoTrial.setDurum(true);
//                    dbLevelInfoTrial.setTotalTrialCount(_TrialCount);
                    dBase.dbDao().updateLevelInfo(dbLevelInfoTrial);

                    if (String.valueOf(textviewSolution.getText()).equals(dbLevels.getSolution())) {
                        Properties.Vibration(QuizGame.this, 60);

                        Boolean SonLevelIlkBitis = false;
                        if (dbLevelInfoTrial.getFirstFindCount() == 0) {
                            dbLevelInfoTrial.setFirstFindCount(_TrialCount);
                            SonLevelIlkBitis = true;

                            if (_TrialCount <= 3) {
                                _StarCount = 3;
                            } else if (_TrialCount <= 6) {
                                _StarCount = 2;
                            } else {
                                _StarCount = 1;
                            }

                            dbLevelInfoTrial.setStarCount(_StarCount);
                            dBase.dbDao().updateLevelInfo(dbLevelInfoTrial);
                        }


                        if (Integer.valueOf(dbLevels.getName()) != dBase.dbDao().getLevels().size()) {
                            DbLevelInfo dbLevelInfo = new DbLevelInfo();
                            dbLevelInfo.setDurum(true);
                            dbLevelInfo.setLevelId(Integer.valueOf(dbLevels.getName()) + 1);

                            if (dBase.dbDao().getLevelInfo(Integer.valueOf(dbLevels.getName()) + 1) != null) {
                                dBase.dbDao().updateLevelInfo(dbLevelInfo);
                            } else {
                                dbLevelInfo.setFirstFindCount(0);
//                                dbLevelInfo.setTotalTrialCount(0);
                                dbLevelInfo.setStarCount(0);
                                dbLevelInfo.setHeart((Integer.valueOf(dbLevels.getName()) + 1) % 10 == 0 ? QuizGame._minHeart : QuizGame._maxHeart);
                                dBase.dbDao().addLevelInfo(dbLevelInfo);
                            }

                            _StarCount = dBase.dbDao().getLevelInfo(Integer.valueOf(dbLevels.getName())).getStarCount();
                            dbLevels = dBase.dbDao().getLevelsByName(String.valueOf(Integer.valueOf(dbLevels.getName()) + 1));

                            Properties.loadLevelUpSound(QuizGame.this);
                            showMyNextQuestions(getString(R.string.levels), getString(R.string.next), getString(R.string.congratulations), false);
                        } else if (SonLevelIlkBitis) {
                            Tebrikler.dbLevelInfo = dBase.dbDao().getLevelInfo(Integer.valueOf(dbLevels.getName()));
                            startActivity(new Intent(QuizGame.this, Tebrikler.class));
                            overridePendingTransition(R.anim.anim_start_activity, R.anim.anim_stop_activity);
                            finish();
                        } else if (!SonLevelIlkBitis) {
                            _StarCount = dBase.dbDao().getLevelInfo(Integer.valueOf(dbLevels.getName())).getStarCount();
                            showMyNextQuestions(getString(R.string.home_page), getString(R.string.levels), getString(R.string.all_sections_complete), true);
                        }

                        _TrialCount = 0;
                    } else {
                        DbLevelInfo dbLevelInfo = dBase.dbDao().getLevelInfo(Integer.valueOf(dbLevels.getName()));
                        if (dbLevelInfo != null) {
                            int _heart = dbLevelInfo.getHeart() - 1;

                            if (_heart >= 0) {
                                Properties.loadWrongAnswerSound(QuizGame.this);

                                d1++;

                                linearLayoutWarning.setVisibility(View.VISIBLE);
                                Animation shake = AnimationUtils.loadAnimation(QuizGame.this, R.anim.vibration_text);
                                textviewWarning.startAnimation(shake);

                                handler.postDelayed(runnable, 1800);
                                Properties.Vibration(QuizGame.this, 250);

                                dbLevelInfo.setHeart(_heart);
                                imagelistHeart[_heart].setImageResource(R.drawable.anim_heart_off);
                                ((TransitionDrawable) imagelistHeart[_heart].getDrawable()).startTransition(750);
                                dBase.dbDao().updateLevelInfo(dbLevelInfo);

                                textviewSolution.setText("");
                            } else {
                                // Reklam izleme sayfası açılsın!
                                if (_heart < 0) {
                                    imagelistHeart[0].setImageResource(R.drawable.anim_heart_off);
                                    ((TransitionDrawable) imagelistHeart[0].getDrawable()).startTransition(750);
                                }

                                dbLevelInfo.setHeart(0);
                                dBase.dbDao().updateLevelInfo(dbLevelInfo);

                                showMyHeartDialog();
                            }
                        }
                    }
                }
            }
        });

        MobileAds.initialize(this, getString(R.string.odullu_ad_id));
        mRewardedVideoAdHakKazan = MobileAds.getRewardedVideoAdInstance(getApplicationContext());
        mRewardedVideoAdHakKazan.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdLoaded() {
                isLoadAd = true;

                progressBarAds.setVisibility(View.GONE);
                try {
                    //linearLoadAd.setVisibility(View.GONE);
                    linearLoadAd.animate().translationY(-linearLoadAd.getHeight()).alpha(0.0f).setDuration(300).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            linearLoadAd.setVisibility(View.GONE);
                        }
                    });
                    hintdialogButtonHint.animate().translationY(0).alpha(1.0f).setDuration(300).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                        }
                    });
                    hintdialogButtonAddHeart.animate().translationY(0).alpha(1.0f).setDuration(300).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                        }
                    });
                    hintdialogButtonClose.animate().translationY(0).alpha(1.0f).setDuration(300).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                        }
                    });

                    hintdialogButtonHint.setBackgroundResource(R.drawable.dialog_button);
                    hintdialogButtonAddHeart.setBackgroundResource(R.drawable.dialog_button);

                    hintdialogButtonHint.setTextColor(getResources().getColor(android.R.color.black));
                    hintdialogButtonAddHeart.setTextColor(getResources().getColor(android.R.color.black));
                } catch (Exception e) {
                }

                try {
                    linear_heartdialog_loadad.setVisibility(View.INVISIBLE);
                    button_heartdialog_reklamizle.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                }

                AdOdul = false;
            }

            @Override
            public void onRewardedVideoAdOpened() {

            }

            @Override
            public void onRewardedVideoStarted() {

            }

            @Override
            public void onRewardedVideoAdClosed() {
                // Reklamı kapatınca!
                loadRewardedVideoAdHakKazan();
                BottombarHide();

                if (AdOdul && clickAdSelect == 1) {
                    showAnswerDialog();
                }

                AdOdul = false;
            }

            @Override
            public void onRewarded(RewardItem rewardItem) {
                // Ödül Kazanınca
                AdOdul = true;

                if (clickAdSelect == 2) {
                    TumHaklariDoldur();
                }
            }

            @Override
            public void onRewardedVideoAdLeftApplication() {

            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int i) {
                Toast.makeText(QuizGame.this, getString(R.string.ad_could_not_load), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoCompleted() {

            }
        });
        loadRewardedVideoAdHakKazan();

        imageviewSolution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Properties.Vibration(QuizGame.this, 60);

//                if (mRewardedVideoAdHakKazan.isLoaded()) {
//                    mRewardedVideoAdHakKazan.show();
//                }

                showInfoAdDialog();
            }
        });

        Button[] butdizi = {buttonNum0, buttonNum1, buttonNum2, buttonNum3, buttonNum4, buttonNum5, buttonNum6, buttonNum7, buttonNum8, buttonNum9};
        for (int i = 0; i < butdizi.length; i++) {
            final int i2 = i;
            butdizi[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Properties.loadMediaPlayer(QuizGame.this, R.raw.click2);
                    Properties.loadClickSound(QuizGame.this);
                    Properties.Vibration(QuizGame.this, 50);
                    ClickNumber(String.valueOf(i2));
                }
            });
        }
    }

    private void loadRewardedVideoAdHakKazan() {
        mRewardedVideoAdHakKazan.loadAd(getString(R.string.odullu_ad_id),
                new AdRequest.Builder().addTestDevice(getString(R.string.test_device_id)).build());
        isLoadAd = false;

        progressBarAds.setVisibility(View.VISIBLE);
        try {
            linearLoadAd.setVisibility(View.VISIBLE);
            hintdialogButtonHint.setBackgroundResource(R.drawable.dialog_button_disable);
            hintdialogButtonAddHeart.setBackgroundResource(R.drawable.dialog_button_disable);

            hintdialogButtonHint.setTextColor(getResources().getColor(android.R.color.white));
            hintdialogButtonAddHeart.setTextColor(getResources().getColor(android.R.color.white));
        } catch (Exception e) {}
    }

    private RewardedVideoAd mRewardedVideoAdHakKazan;
    Integer d1 = 0, d2 = 1;

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (d1 == d2) {
                linearLayoutWarning.setVisibility(View.GONE);
            }
            d2 ++;
        }
    };

    private void loadQuestions() {
        if (dbLevels != null) {
            // burada çökme var.
            CacheClear(QuizGame.this);
            imageviewQuestion.setImageResource(getResources().getIdentifier(dbLevels.getImage(), "drawable", getPackageName()));
            textviewSolution.setText("");
            textviewActionHead.setText(getString(R.string.level) + ": " + dbLevels.getName());

            for (int i = 0; i < dBase.dbDao().getLevelInfo(Integer.valueOf(dbLevels.getName())).getHeart(); i++) {
                imagelistHeart[i].setImageResource(R.drawable.heart);
            }
            for (int i = dBase.dbDao().getLevelInfo(Integer.valueOf(dbLevels.getName())).getHeart(); i < imagelistHeart.length; i++) {
                imagelistHeart[i].setImageResource(R.drawable.notheart);
            }
        } else {
            Toast.makeText(this, getString(R.string.please_select_level), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @SuppressLint("SetTextI18n")
    public void showMyNextQuestions(String leftMessage, String rightMessage, String baslik, final Boolean sonBolum) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_next_question);

        Button btnRight= (Button) dialog.findViewById(R.id.nextquestions_next);
        final Button btnLeft = (Button) dialog.findViewById(R.id.nextquestions_exit);
        TextView tvBaslik = (TextView) dialog.findViewById(R.id.textview_baslik);

        ImageView imageviewStar1 = (ImageView) dialog.findViewById(R.id.nextquest_star1);
        ImageView imageviewStar2 = (ImageView) dialog.findViewById(R.id.nextquest_star2);
        ImageView imageviewStar3 = (ImageView) dialog.findViewById(R.id.nextquest_star3);

        ImageView[] imageviewList = {imageviewStar1, imageviewStar2, imageviewStar3};
        for (int i=0; i < _StarCount; i++) {
            imageviewList[i].setImageResource(R.drawable.anim_star_on);
            ((TransitionDrawable) imageviewList[i].getDrawable()).startTransition(400);
        }

        btnLeft.setText(leftMessage);
        btnRight.setText(rightMessage);

        tvBaslik.setText(baslik);

        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Properties.Vibration(QuizGame.this, 60);

                if (!sonBolum) {
                    loadQuestions();
                    dialog.dismiss();

                    if (Properties.FullAdShowCheck) {
//                        FullscreenAd();
                        if (isLoadShowAd) {
                            if (Properties.FullAdShowCheck) {
                                mInterstitialAd.show();
                                Properties.FullAdShowCheck = false;
                            }
                        } else {
                            isShowAd = true;
                        }
                    }
                } else {
                    Loading_Full.isPage = 1;
                    startActivity(new Intent(QuizGame.this, Loading_Full.class));
                    overridePendingTransition(R.anim.anim_start_activity, R.anim.anim_stop_activity);
                    finish();
//
//                    startActivity(new Intent(QuizGame.this, Levels.class));
//                    overridePendingTransition(R.anim.anim_start_activity, R.anim.anim_stop_activity);
//                    finish();
                }

            }
        });

        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Properties.Vibration(QuizGame.this, 60);

                Loading_Full.isPage = 1;
                startActivity(new Intent(QuizGame.this, Loading_Full.class));
                overridePendingTransition(R.anim.anim_start_activity, R.anim.anim_stop_activity);
                finish();

//                dialog.dismiss();
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

    @Override
    public void onBackPressed() {
//        super.onBackPressed();

        Loading_Full.isPage = 1;
        startActivity(new Intent(QuizGame.this, Loading_Full.class));
        overridePendingTransition(R.anim.anim_start_activity, R.anim.anim_stop_activity);
        finish();
    }

    private LinearLayout linear_heartdialog_loadad;
    private Button button_heartdialog_reklamizle;
    @SuppressLint("SetTextI18n")
    public void showMyHeartDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_heart_dialog);

        Button buttonAnasayfa = (Button) dialog.findViewById(R.id.heartdialog_button_anasayfa);
        button_heartdialog_reklamizle = (Button) dialog.findViewById(R.id.heartdialog_button_reklamizle);
        TextView tvBaslik = (TextView) dialog.findViewById(R.id.textview_baslik);

        buttonAnasayfa.setText(getString(R.string.cancel));

        linear_heartdialog_loadad = (LinearLayout) dialog.findViewById(R.id.heartdialog_linear_load_ad);
        if (isLoadAd) {
            linear_heartdialog_loadad.setVisibility(View.INVISIBLE);
            button_heartdialog_reklamizle.setVisibility(View.VISIBLE);
        } else {
            linear_heartdialog_loadad.setVisibility(View.VISIBLE);
            button_heartdialog_reklamizle.setVisibility(View.INVISIBLE);
        }

        buttonAnasayfa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Properties.Vibration(QuizGame.this, 60);

//                startActivity(new Intent(QuizGame.this, Main.class));
//                overridePendingTransition(R.anim.anim_start_activity, R.anim.anim_stop_activity);
//                finish();
                dialog.dismiss();
            }
        });

        button_heartdialog_reklamizle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Properties.Vibration(QuizGame.this, 60);
                clickAdSelect = 2;

                if (mRewardedVideoAdHakKazan.isLoaded()) {
                    mRewardedVideoAdHakKazan.show();
                }
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
        Properties.loadAdMenuSound(QuizGame.this);
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

    private void TumHaklariDoldur() {
        DbLevelInfo dbLevelInfo = dBase.dbDao().getLevelInfo(Integer.valueOf(dbLevels.getName()));
        if (Integer.valueOf(dbLevels.getName()) % 10 == 0) {
            dbLevelInfo.setHeart(QuizGame._minHeart);
        } else {
            dbLevelInfo.setHeart(QuizGame._maxHeart);
        }
        dBase.dbDao().updateLevelInfo(dbLevelInfo);

        for (int i=0; i < dBase.dbDao().getLevelInfo(Integer.valueOf(dbLevels.getName())).getHeart(); i++) {
            imagelistHeart[i].setImageResource(R.drawable.heart);
        }
        for (int i=dBase.dbDao().getLevelInfo(Integer.valueOf(dbLevels.getName())).getHeart(); i < imagelistHeart.length; i++) {
            imagelistHeart[i].setImageResource(R.drawable.notheart);
        }
    }

    private Integer clickAdSelect = 0;
    private Boolean isLoadAd = false;
    private LinearLayout linearLoadAd;
    private Button hintdialogButtonHint, hintdialogButtonAddHeart, hintdialogButtonClose;
    private Boolean AdOdul = false;

    public void showInfoAdDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_heart_hint_dialog);

        hintdialogButtonHint = (Button) dialog.findViewById(R.id.hint_button_hint);
        hintdialogButtonAddHeart = (Button) dialog.findViewById(R.id.hint_button_addheart);
        hintdialogButtonClose = (Button) dialog.findViewById(R.id.hint_button_close);

        TextView textViewHead = (TextView) dialog.findViewById(R.id.hint_textview_baslik);
        TextView textViewLittleHead = (TextView) dialog.findViewById(R.id.hint_textview_little_baslik);

        linearLoadAd = (LinearLayout) dialog.findViewById(R.id.hint_linear_load_ad);
        if (isLoadAd) {
            linearLoadAd.setVisibility(View.GONE);
            hintdialogButtonHint.setBackgroundResource(R.drawable.dialog_button);
            hintdialogButtonAddHeart.setBackgroundResource(R.drawable.dialog_button);

            hintdialogButtonHint.setTextColor(getResources().getColor(android.R.color.black));
            hintdialogButtonAddHeart.setTextColor(getResources().getColor(android.R.color.black));
        } else {
            linearLoadAd.setVisibility(View.VISIBLE);
            hintdialogButtonHint.setBackgroundResource(R.drawable.dialog_button_disable);
            hintdialogButtonAddHeart.setBackgroundResource(R.drawable.dialog_button_disable);

            hintdialogButtonHint.setTextColor(getResources().getColor(android.R.color.white));
            hintdialogButtonAddHeart.setTextColor(getResources().getColor(android.R.color.white));
        }

        clickAdSelect = 0;

        hintdialogButtonHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLoadAd) {
                    Properties.Vibration(QuizGame.this, 60);
                    clickAdSelect = 1;

                    if (mRewardedVideoAdHakKazan.isLoaded()) {
                        mRewardedVideoAdHakKazan.show();
                    }
                    dialog.dismiss();
                }
            }
        });

        hintdialogButtonAddHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLoadAd) {
                    Properties.Vibration(QuizGame.this, 60);
                    clickAdSelect = 2;

                    if (mRewardedVideoAdHakKazan.isLoaded()) {
                        mRewardedVideoAdHakKazan.show();
                    }
                    dialog.dismiss();
                }
            }
        });

        hintdialogButtonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Properties.Vibration(QuizGame.this, 60);
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
//        Properties.loadAdMenuSound(QuizGame.this);
    }

    @SuppressLint("SetTextI18n")
    public void showAnswerDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog);

        Button btnExit = (Button) dialog.findViewById(R.id.button_kaydet);
        Button btnIptal = (Button) dialog.findViewById(R.id.button_iptal);
        TextView tvBaslik = (TextView) dialog.findViewById(R.id.textview_baslik);
        ImageView ivResim = (ImageView) dialog.findViewById(R.id.imageview_resim);

        btnExit.setText(getString(R.string.exit));
        btnIptal.setVisibility(View.GONE);
        ivResim.setVisibility(View.GONE);

        tvBaslik.setText(getString(R.string.answer) + " : " + dbLevels.getSolution());
        tvBaslik.setTextSize(20);

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Properties.Vibration(QuizGame.this, 60);
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

    private Boolean isShowAd = false, isLoadShowAd = false;
    InterstitialAd mInterstitialAd;
    private void FullscreenAd() {
        mInterstitialAd = new InterstitialAd(this);

        mInterstitialAd.setAdUnitId(getString(R.string.fullscreen_ad_id));
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                isLoadShowAd = true;

                if (isShowAd) {
                    if (Properties.FullAdShowCheck) {
                        mInterstitialAd.show();
                        Properties.FullAdShowCheck = false;
                    }
                }
            }
        });

        requestNewInterstitial();
    }

    private void requestNewInterstitial() { //Test cihazı ekliyoruz Admob dan ban yememek için
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("F5DBF9369AA09DEC004804A7E6AAC356")
                .build();

        mInterstitialAd.loadAd(adRequest);
    }

    public static void CacheClear(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }
}