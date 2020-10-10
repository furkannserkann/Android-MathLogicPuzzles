package com.fsfy.mathlogicquiz.Custom;

import android.app.Activity;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

import com.fsfy.mathlogicquiz.Database.DbLevels;
import com.fsfy.mathlogicquiz.Items.ItemQuestion;
import com.fsfy.mathlogicquiz.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Properties {

    public static ArrayList<DbLevels> LevelList;

    public static MediaPlayer mediaPlayerClick;
    public static MediaPlayer mediaPlayerLevelUp;
    public static MediaPlayer mediaPlayerAdMenu;
    public static MediaPlayer mediaPlayerWrongAnswer;

    public static boolean SoundCheck = true;
    public static boolean VibrationCheck = true;

    public static boolean FullAdShowCheck = true;

    public static List<ItemQuestion> itemQuestionList;


    public static String LoadLocalJson(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("ornek.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        return json;
    }

    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("UPDATE levels set image='quiz0068' where image='quiz00068'");
        }
    };

    public static boolean isOnline(Activity activity) {
        return (new InternetControl(activity)).isOnline();
    }

    public static void loadClickSound(final Context context) {
        try {
            if (mediaPlayerClick == null) {
                mediaPlayerClick = MediaPlayer.create(context, R.raw.click2);
            }

            if (Properties.SoundCheck) {
                mediaPlayerClick.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadLevelUpSound(final Context context) {
        try {
            if (mediaPlayerLevelUp == null) {
                mediaPlayerLevelUp = MediaPlayer.create(context, R.raw.levelup);
            }

            if (Properties.SoundCheck) {
                mediaPlayerLevelUp.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadAdMenuSound(final Context context) {
        try {
            if (mediaPlayerAdMenu == null) {
                mediaPlayerAdMenu = MediaPlayer.create(context, R.raw.watch_ads_menu);
            }

            if (Properties.SoundCheck) {
                mediaPlayerAdMenu.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadWrongAnswerSound(final Context context) {
        try {
            if (mediaPlayerWrongAnswer == null) {
                mediaPlayerWrongAnswer = MediaPlayer.create(context, R.raw.wrong_answer);
            }

            if (Properties.SoundCheck) {
                mediaPlayerWrongAnswer.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Vibrator vibrator;

    public static void Vibration(final Context context, final Integer time) {
        if (Properties.VibrationCheck) {
            if (vibrator == null) {
                vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(time, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                vibrator.vibrate(time);
            }
        }
    }
}
