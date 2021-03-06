package com.yairnet.quizgame;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.yairnet.quizgame.common.Common;
import com.yairnet.quizgame.model.QuestionScore;

public class Done extends AppCompatActivity
{
    TextView totalScoreTxt, totalResultTxt;
    Button tryAgainBtn;

    FirebaseDatabase database;
    DatabaseReference question_score;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done);

        database = FirebaseDatabase.getInstance();
        question_score = database.getReference("Question Score");

        // (R.id.text) - the "text" are ids from the "activity_done.xml)
        totalScoreTxt = (TextView)findViewById(R.id.totalScoreTxt);
        totalResultTxt = (TextView)findViewById(R.id.totalResultTxt);
        tryAgainBtn = (Button)findViewById(R.id.tryAgainBtn);

        tryAgainBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Intent intent = new Intent(Done.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
        // Getting the data from the bundle and view it
        Bundle extra = getIntent().getExtras();

        if(extra != null){
            int score = extra.getInt("Score");
            int totalQuestion = extra.getInt("Passed");
            int correctAnswer = extra.getInt("Correct");

            totalScoreTxt.setText(String.format("Score : %d", score));
            totalResultTxt.setText(String.format("Passed : %d / %d", correctAnswer, totalQuestion));

            // Uploading points to the Database
            question_score.child(String.format("%s_%s", Common.currentUser.getUsername(),
                                                        Common.categoryId))
                    .setValue(new QuestionScore(String.format("%s_%s", Common.currentUser.getUsername(),
                            Common.categoryId),
                            Common.currentUser.getUsername(),
                            String.valueOf(score),
                            Common.categoryId,
                            Common.categoryName));
        }
    }
}