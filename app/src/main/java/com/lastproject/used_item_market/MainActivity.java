package com.lastproject.used_item_market;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    Button lifebutton;
    Button login_button;

    //DB 관련
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //파이어 베이스 데이터베이스 연동
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        //생명주기 연습
        lifebutton = (Button)findViewById(R.id.lifecycle_button);
        lifebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent a = new Intent(MainActivity.this, testintentLife.class);
                startActivity(a);

                System.exit(0);

                //gmyRef.child("Users").setValue()



            }
        });


        //구글 로그인 연습
        login_button = (Button)findViewById(R.id.login_button);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent a = new Intent(MainActivity.this, Login.class);
                startActivity(a);

            }
        });

        testQuery();

    }//onCreate() 끝

    void testQuery(){


        //쿼리를 이용하여 특정 데이터 가져오기 성공
        Button bt_query = (Button)findViewById(R.id.query_button);
        bt_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Query testQuery = myRef.child("User").orderByChild("google_email").equalTo("tourer97@sunmoon.ac.kr");
                testQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {


                        for(DataSnapshot ds : snapshot.getChildren()){

                            System.out.println(ds.getKey());
                            User user = ds.getValue(User.class);
                            System.out.println( "받음: " + user.google_email);
                            if(user.google_email.equals("tourer97@sunmoon.ac.kr")){

                                Toast.makeText(MainActivity.this, "쿼리 성공", Toast.LENGTH_SHORT).show();

                            }else{
                                Toast.makeText(MainActivity.this, "쿼리 실패", Toast.LENGTH_SHORT).show();
                            }

                        }


                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });


    }//testQuery





}