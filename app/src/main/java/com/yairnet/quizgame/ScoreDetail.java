package com.yairnet.quizgame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.yairnet.quizgame.model.QuestionScore;
import com.yairnet.quizgame.viewHolder.ScoreViewHolder;

// Displaying the total score of the user and the score in each category
public class ScoreDetail extends AppCompatActivity
{
    FirebaseDatabase database;
    DatabaseReference questionScore;

    RecyclerView scoreList;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<QuestionScore, ScoreViewHolder> adapter;

    String viewUser = "";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_detail);

        database = FirebaseDatabase.getInstance();
        questionScore = database.getReference("Question Score");

        // User Ranking - Init View
        scoreList = (RecyclerView)findViewById(R.id.scoreList);
        scoreList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        scoreList.setLayoutManager(layoutManager);

        if(getIntent() != null)
            viewUser = getIntent().getStringExtra("viewUser");
        if(!viewUser.isEmpty())
            loadScoreDetail(viewUser);

    }

    private void loadScoreDetail(String viewUser){
        adapter = new FirebaseRecyclerAdapter<QuestionScore, ScoreViewHolder>(
                QuestionScore.class,
                R.layout.score_detail_layout,
                ScoreViewHolder.class,
                questionScore.orderByChild("user").equalTo(viewUser)
        ){

            @Override
            protected void populateViewHolder(ScoreViewHolder viewHolder, QuestionScore model, int i){
                viewHolder.txt_name.setText(model.getCategoryName());
                viewHolder.txt_score.setText(model.getScore());
            }
        };
        adapter.notifyDataSetChanged();
        scoreList.setAdapter(adapter);
    }
}