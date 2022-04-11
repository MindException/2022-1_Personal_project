package com.lastproject.used_item_market;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class newQueryTest extends AppCompatActivity {  //클라이언트 부담을 줄이기 위한 데이터베이스 상향

    //DB 관련
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    //정보 저장
    ArrayList<Product> products = new ArrayList<Product>();

    boolean trigger = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_query_test);

        //파이어 베이스 데이터베이스 연동
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        myRef.child("Product").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.hasChild("-MznFkHB3ddPUCsCjhJK")){
                    trigger = true;
                    System.out.println("가지고 있음");
                };

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if(trigger == true){

            System.out.println("trigger 들어옴");
            Query test = myRef.child("Product").endBefore("-MznFkHB3ddPUCsCjhJK");
            test.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    for(DataSnapshot ds : snapshot.getChildren()){

                        products.add(ds.getValue(Product.class));

                    }

                    System.out.println(products.size());


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }else{
            System.out.println("안들어옴");
        }





    }
}