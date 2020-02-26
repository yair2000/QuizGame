package com.yairnet.quizgame;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yairnet.quizgame.Interface.ItemClickListener;
import com.yairnet.quizgame.Interface.RankingCallBack;
import com.yairnet.quizgame.common.Common;
import com.yairnet.quizgame.model.QuestionScore;
import com.yairnet.quizgame.model.Ranking;
import com.yairnet.quizgame.viewHolder.RankingViewHolder;

// Home Screen (Showing the ranking)
public class RankingFragment extends Fragment
{
    View myFragment;
    RecyclerView rankList;
    LinearLayoutManager layoutManager;
    FirebaseRecyclerAdapter<Ranking,RankingViewHolder> adapter;

    FirebaseDatabase database;
    DatabaseReference questionScore, rankingTable;
    int sum = 0;

    public static RankingFragment newInstance(){
        RankingFragment rankingFragment = new RankingFragment();
        return rankingFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        // The Firebase Database
        database = FirebaseDatabase.getInstance();
        questionScore = database.getReference("Question Score"); // What appears in the firebase Database
        rankingTable = database.getReference("Ranking");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        myFragment = inflater.inflate(R.layout.ranking_fragment, container, false);

        // User Ranking - Init View
        rankList = (RecyclerView)myFragment.findViewById(R.id.rankList);
        layoutManager = new LinearLayoutManager(getActivity());
        rankList.setHasFixedSize(true);

        // Reverse the Recycle Data - Firebase organizes thing in an ascending way
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        rankList.setLayoutManager(layoutManager);

        // User Ranking
        updateScore(Common.currentUser.getUsername(), new RankingCallBack<Ranking>(){

            @Override
            public void callBack(Ranking ranking){
                rankingTable.child(ranking.getUserName()).setValue(ranking); // Get the ranking of the username
            }
        });

        //Setting the Adapter
        adapter = new FirebaseRecyclerAdapter<Ranking, RankingViewHolder>(
                Ranking.class,
                R.layout.ranking_layout,
                RankingViewHolder.class,
                rankingTable.orderByChild("score")
        ){

            @Override
            protected void populateViewHolder(RankingViewHolder viewHolder, final Ranking model, int i){
                viewHolder.txt_name.setText(model.getUserName());
                viewHolder.txt_score.setText(String.valueOf(model.getScore()));

                viewHolder.setItemClickListener(new ItemClickListener(){

                    @Override
                    public void onClick(View v, int i, boolean isLongClick){
                        Intent scoreDetail = new Intent(getActivity(),ScoreDetail.class);
                        scoreDetail.putExtra("viewUser", model.getUserName());
                        startActivity(scoreDetail);
                    }
                });
            }
        };
        adapter.notifyDataSetChanged();
        rankList.setAdapter(adapter);

        return myFragment;
    }

    private void updateScore(final String userName, final RankingCallBack<Ranking> callBack){
        questionScore.orderByChild("user").equalTo(userName).addValueEventListener(new ValueEventListener(){

            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    QuestionScore ques = data.getValue(QuestionScore.class);
                    sum+=Integer.parseInt(ques.getScore()); // Integer.parseInt() - Convert String to Integer
                }
                Ranking ranking = new Ranking(userName,sum);
                // Firebase is an async DB. Therefore, the processing of the "sum" variable must be written under the method. If not, the value(sum) will reset to 0
                callBack.callBack(ranking);
            }

            @Override
            public void onCancelled(DatabaseError databaseError){

            }
        });
    }
}
