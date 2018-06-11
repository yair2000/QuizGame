package com.yairnet.quizgame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import com.yairnet.quizgame.Interface.ItemClickListener;
import com.yairnet.quizgame.common.Common;
import com.yairnet.quizgame.model.Category;
import com.yairnet.quizgame.viewHolder.CategoryViewHolder;

// Home Screen (Showing the categories)
public class CategoryFragment extends Fragment
{
    View myFragment;

    RecyclerView categoryList;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Category, CategoryViewHolder> adapter;

    FirebaseDatabase database;
    DatabaseReference categories;

    public static CategoryFragment newInstance(){
        CategoryFragment categoryFragment = new CategoryFragment();
        return categoryFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
        categories = database.getReference("Category"); // What appears in the firebase Database
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        myFragment = inflater.inflate(R.layout.category_fragment, container, false); // inflater.inflate creates a View from the XML file

        categoryList = (RecyclerView) myFragment.findViewById(R.id.categoryList);
        categoryList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(container.getContext());
        categoryList.setLayoutManager(layoutManager);

        loadCategories(); // Loading the categories after you logged in

        return myFragment;
    }

    // Loading the categories
    private void loadCategories(){
        adapter = new FirebaseRecyclerAdapter<Category, CategoryViewHolder>(
                Category.class, R.layout.category_layout, // The class and XML file that the adapter needs
                CategoryViewHolder.class, categories) // The class and the Reference's name from the "DatabaseReference" that the adapter needs
        {
            @Override
            protected void populateViewHolder(CategoryViewHolder viewHolder, final Category model, int i){
                viewHolder.nameCategory.setText(model.getName());
                Picasso.with(getActivity()) // The Picasso is a type of library that shows images
                        .load(model.getImage())
                        .into(viewHolder.imageCategory); // "imageCategory" - the id from ImageView in the "category_layout.xml" file

                viewHolder.setItemClickListener(new ItemClickListener(){

                    @Override
                    public void onClick(View v, int i, boolean isLongClick){
                        //Toast.makeText(getActivity(), String.format("%s", adapter.getRef(i).getKey(), model.getName()), Toast.LENGTH_SHORT).show();

                        Intent startGame = new Intent(getActivity(), StartActivity.class);
                        Common.categoryId = adapter.getRef(i).getKey();
                        Common.categoryName = model.getName();
                        startActivity(startGame);
                    }
                });
            }
        };
        adapter.notifyDataSetChanged();
        categoryList.setAdapter(adapter);
    }
}