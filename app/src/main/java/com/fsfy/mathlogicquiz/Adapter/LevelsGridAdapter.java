package com.fsfy.mathlogicquiz.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fsfy.mathlogicquiz.Database.DBase;
import com.fsfy.mathlogicquiz.Database.DbLevelInfo;
import com.fsfy.mathlogicquiz.Database.DbLevels;
import com.fsfy.mathlogicquiz.R;

import java.util.List;

public class LevelsGridAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context context;
    private List<DbLevels> LevelList;
    private DBase dBase;
    private Activity activity;



    public LevelsGridAdapter(Context context, List<DbLevels> LevelList, DBase dBase, Activity activity) {

        this.context = context;
        this.LevelList = LevelList;
        this.dBase = dBase;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return LevelList.size();
    }

    @Override
    public Object getItem(int position) {
        return LevelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        this.mInflater = ((Activity) context).getLayoutInflater();
        view = mInflater.inflate(R.layout.item_levels, parent, false);

        ImageView star1 = (ImageView) view.findViewById(R.id.itemlevel_star1),
                star2 = (ImageView) view.findViewById(R.id.itemlevel_star2),
                star3 = (ImageView) view.findViewById(R.id.itemlevel_star3),
                imageViewLock = (ImageView) view.findViewById(R.id.itemlevels_imageview_lock);
        TextView textViewName = (TextView) view.findViewById(R.id.itemlevels_textview_name);
        ImageView[] imageViewList = {star1, star2, star3};


        Typeface typeface1 = Typeface.createFromAsset(context.getAssets(), "Righteous-Regular.ttf");
        textViewName.setText(LevelList.get(position).getName());
//        textViewName.setTypeface(typeface1);

        DbLevelInfo dbLevelInfo = dBase.dbDao().getLevelInfo(Integer.valueOf(LevelList.get(position).getName()));
        if (dbLevelInfo != null) {
            if (dbLevelInfo.isDurum()) {
                imageViewLock.setVisibility(View.GONE);
                textViewName.setTextColor(Color.WHITE);

                for (int i = 0; i < dbLevelInfo.getStarCount(); i++) {
                    imageViewList[i].setImageResource(R.drawable.ic_star_on);
                }
            }
        }

        return view;
    }
}
