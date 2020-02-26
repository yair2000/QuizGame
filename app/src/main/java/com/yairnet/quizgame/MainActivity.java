package com.yairnet.quizgame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.yairnet.quizgame.common.Common;
import com.yairnet.quizgame.model.User;

public class MainActivity extends AppCompatActivity
{
    MaterialEditText newUser, newPass, newMail; // Signing Up
    MaterialEditText addUsername, addPassword; // Signing In
    TextView signUpBtn, signInBtn;
    FirebaseDatabase database;
    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // The Firebase Database
        database = FirebaseDatabase.getInstance();
        users = database.getReference("Users"); // What appears in the firebase Database

        addUsername = (MaterialEditText)findViewById(R.id.addUsername);
        addPassword = (MaterialEditText)findViewById(R.id.addPassword);

        signInBtn = (TextView)findViewById(R.id.signInBtn);
        signUpBtn = (TextView)findViewById(R.id.signUpBtn);

        signUpBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                SignUpDialog();
            }
        });

        signInBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                signIn(addUsername.getText().toString(), addPassword.getText().toString());
            }
        });
    }

    // Sign Up process
    private void SignUpDialog(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Sign Up");
        alertDialog.setMessage("Fill in full information");

        LayoutInflater inflater = this.getLayoutInflater();
        View sign_up = inflater.inflate(R.layout.sign_up, null);

        newUser = (MaterialEditText)sign_up.findViewById(R.id.newUsername);
        newPass = (MaterialEditText)sign_up.findViewById(R.id.newPassword);
        newMail = (MaterialEditText)sign_up.findViewById(R.id.newEmail);

        alertDialog.setView(sign_up);
        alertDialog.setIcon(R.drawable.ic_account_circle_black_24dp);

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i){
                dialogInterface.dismiss();
            }
        });

        alertDialog.setPositiveButton("Register", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i){
                final User user = new User(newUser.getText().toString(),
                                           newPass.getText().toString(),
                                           newMail.getText().toString());

                users.addListenerForSingleValueEvent(new ValueEventListener(){

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot){
                        if(dataSnapshot.child(user.getUsername()).exists()){ // If the user exists or not
                            Toast.makeText(MainActivity.this, "User already exists", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            users.child(user.getUsername()).setValue(user);
                            Toast.makeText(MainActivity.this, "User successfully registered", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError){

                    }
                });
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }

    // Sign In process
    private void signIn(final String un, final String pwd){
        users.addListenerForSingleValueEvent(new ValueEventListener(){

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot){

                if(dataSnapshot.child(un).exists()){

                    User login = dataSnapshot.child(un).getValue(User.class);

                    if(!un.isEmpty()){ // If the username exists or not

                        if((pwd.equals(login.getPassword()))){ // If the password is correct or not
                        
                            // Showing the "HomeActivity.class" with its "activity_home.xml"
                            Intent homeActivity = new Intent(MainActivity.this, HomeActivity.class);
                            Common.currentUser = login;
                            startActivity(homeActivity);
                            finish();
                        }
                        else
                            Toast.makeText(MainActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(MainActivity.this, "Enter your username", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                    Toast.makeText(MainActivity.this, "This username doesn't exist", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError){

            }
        });
    }
}
