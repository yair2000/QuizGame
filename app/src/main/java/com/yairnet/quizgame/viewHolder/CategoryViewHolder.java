package com.yairnet.quizgame.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yairnet.quizgame.Interface.ItemClickListener;
import com.yairnet.quizgame.R;

// This class processes each item in the recycle adapter
public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView nameCategory;
    public ImageView imageCategory;
    private ItemClickListener itemClickListener;

    // category_layout.xml file
    public CategoryViewHolder(View itemView){
        super(itemView);
        imageCategory = (ImageView)itemView.findViewById(R.id.imageCategory);
        nameCategory = (TextView)itemView.findViewById(R.id.nameCategory);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v){
        itemClickListener.onClick(v,getAdapterPosition(), false);
    }
}