package com.yairnet.quizgame;

import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.yairnet.quizgame.common.Common;

public class PlayingActivity extends AppCompatActivity implements View.OnClickListener
{
    final static long INTERVAL = 1000; // 1 second
    final static long TIMEOUT = 7000; // 7 seconds
    int progressValue = 0;

    CountDownTimer countDown;
    int index=0, score=0, thisQuestion=0, totalQuestion, correctAnswer;

    ProgressBar progress;
    ImageView question_image;
    TextView btnA, btnB, btnC, btnD;
    TextView scoreTxt, questionNumTxt, question_text;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing);

        // Views
        scoreTxt = (TextView)findViewById(R.id.scoreTxt);
        questionNumTxt = (TextView)findViewById(R.id.totalQuestionTxt);
        question_text = (TextView)findViewById(R.id.question_text);
        question_image = (ImageView)findViewById(R.id.question_image);
        progress = (ProgressBar)findViewById(R.id.progress);

        // Buttons
        btnA = (TextView)findViewById(R.id.answerAbtn);
        btnB = (TextView)findViewById(R.id.answerBbtn);
        btnC = (TextView)findViewById(R.id.answerCbtn);
        btnD = (TextView)findViewById(R.id.answerDbtn);

        btnA.setOnClickListener(this);
        btnB.setOnClickListener(this);
        btnC.setOnClickListener(this);
        btnD.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        countDown.cancel();

        if(index < totalQuestion){// If there are still questions in the list
            TextView click = (TextView)v;
            if(click.getText().equals(Common.questionList.get(index).getCorrectAnswer())){ // If you clicked on the correct answer
                score+=10; // Add 10 points
                correctAnswer++;
                showQuestion(++index); // Next question
            }
            else{ // If you clicked on the wrong answer
                Intent intent = new Intent(this,Done.class);
                Bundle dataSend = new Bundle();
                dataSend.putInt("Score",score);
                dataSend.putInt("Passed",totalQuestion);
                dataSend.putInt("Correct",correctAnswer);
                intent.putExtras(dataSend);
                startActivity(intent);
                question_text.setTextColor(Color.parseColor("#DF0F0F"));
                question_image.setBackgroundColor(Color.parseColor("#DF0F0F"));
                finish();
            }
            scoreTxt.setText(String.format("%d",score));
        }
    }

    private void showQuestion(int index){
        if(index < totalQuestion){
            thisQuestion++;
            questionNumTxt.setText(String.format("%d / %d",thisQuestion,totalQuestion)); // %d - Decimal Integer
            progress.setProgress(0);
            progressValue = 0;

            if(Common.questionList.get(index).getIsImageQuestion().equals("true")){ // If the question is an Image Question
                Picasso.with(getBaseContext())
                        .load(Common.questionList.get(index).getQuestion())
                        .into(question_image);
                question_image.setVisibility(View.VISIBLE);
                question_text.setVisibility(View.INVISIBLE);
            }
            else{ // If the question is a Text Question
                question_text.setText(Common.questionList.get(index).getQuestion());
                question_image.setVisibility(View.INVISIBLE);
                question_text.setVisibility(View.VISIBLE);
            }
            btnA.setText(Common.questionList.get(index).getAnswerA());
            btnB.setText(Common.questionList.get(index).getAnswerB());
            btnC.setText(Common.questionList.get(index).getAnswerC());
            btnD.setText(Common.questionList.get(index).getAnswerD());
            countDown.start(); // Start the timer
        }
        else{ // If it's the final question
            Intent intent = new Intent(this,Done.class);
            Bundle dataSend = new Bundle();
            dataSend.putInt("Score",score);
            dataSend.putInt("Passed",totalQuestion);
            dataSend.putInt("Correct",correctAnswer);
            intent.putExtras(dataSend);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onResume(){
        super.onResume();

        totalQuestion = Common.questionList.size();
        countDown = new CountDownTimer(TIMEOUT,INTERVAL){

            @Override
            public void onTick(long miniseconds){
                progress.setProgress(progressValue);
                progressValue++;
            }

            @Override
            public void onFinish(){
                countDown.cancel();
                finish();
            }
        };
        showQuestion(index);
    }
}
