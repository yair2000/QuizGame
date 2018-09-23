package com.yairnet.quizgame.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.yairnet.quizgame.R;

public class ScoreViewHolder extends RecyclerView.ViewHolder
{
    public TextView txt_name, txt_score;

    public ScoreViewHolder(View itemView){
        super(itemView);

        txt_name = (TextView)itemView.findViewById(R.id.txt_name);
        txt_score = (TextView)itemView.findViewById(R.id.txt_score);
    }
}