package com.lastproject.used_item_market;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class testintentLife extends AppCompatActivity {

    //여기로 이동한 다음에도 콘솔에서 계속 되는지 확인
    //인탠트를 하더라도 그 전 화면이 살아있는 것을 확인할 수 있다.
    //인탠트 직후에 바로 System.exit(0); 으로 없애야 한다.

    //서버 관련
    private FirebaseDatabase database;
    private DatabaseReference myRef;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testintent_life);

        //파이어 베이스 데이터베이스 연동
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        Test t1 = new Test();

        Button b1 = (Button)findViewById(R.id.insert_button);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            myRef.child("Test").push().setValue(t1);


            }
        });


    }
}