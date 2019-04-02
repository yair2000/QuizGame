package com.yairnet.quizgame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yairnet.quizgame.common.Common;
import com.yairnet.quizgame.model.Question;

import java.util.Collections;

// Starting the game
public class StartActivity extends AppCompatActivity
{
    TextView playBtn;
    FirebaseDatabase database;
    DatabaseReference questions;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        database = FirebaseDatabase.getInstance();
        questions = database.getReference("Questions");

        loadQuestion(Common.categoryId);

        playBtn = (TextView)findViewById(R.id.playBtn);

        playBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Intent intent = new Intent(StartActivity.this, PlayingActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadQuestion(String CategoryId){

        // Clear the list if you have older questions
        if(Common.questionList.size() > 0){
            Common.questionList.clear();
        }

        questions.orderByChild("CategoryId").equalTo(CategoryId)
                .addValueEventListener(new ValueEventListener(){
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot){
                        for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                            Question ques = postSnapshot.getValue(Question.class);
                            Common.questionList.add(ques);
                            Collections.shuffle(Common.questionList); // Shuffling the questions
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError){}
                });
                // Shuffling the questions
                Collections.shuffle(Common.questionList);
    }
}
