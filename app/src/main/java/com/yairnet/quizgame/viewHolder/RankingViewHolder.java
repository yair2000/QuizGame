package com.yairnet.quizgame.viewHolder;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.yairnet.quizgame.Interface.ItemClickListener;
import com.yairnet.quizgame.R;

// User Ranking
public class RankingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txt_name, txt_score;
    private ItemClickListener itemClickListener;

    public RankingViewHolder(View itemView){
        super(itemView);
        txt_name = (TextView)itemView.findViewById(R.id.txt_name);
        txt_score = (TextView)itemView.findViewById(R.id.txt_score);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v){
        itemClickListener.onClick(v,getAdapterPosition(),false);
    }
}