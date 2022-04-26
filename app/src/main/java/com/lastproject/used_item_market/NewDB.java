package com.lastproject.used_item_market;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class NewDB extends AppCompatActivity {

    FirebaseFirestore firestore;
    CollectionReference userDocument;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_db);

        //DB 생성
        firestore = FirebaseFirestore.getInstance();
        userDocument = firestore.collection("Users");


        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ  저장 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
        //데이터 샘플
        //User user = new User("test","1234", "admin");
        //push 값 입력
        //firestore.collection("Users").add(user);
        //key 값 지정 입력
        //firestore.collection("Users").document("a").set(user);

        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ 불러오기 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

        //1번만 읽어오기는 addOnCompleteListener


        //.whereEqualTo("google_email", "test"
        Query test = userDocument.orderBy("google_email").endBefore("a");
        test.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                System.out.println(task);

                if(task.isSuccessful()){
                    System.out.println("성공");
                    for(QueryDocumentSnapshot document : task.getResult()){

                        User user = document.toObject(User.class);   //class는 이렇게 가져온다.
                        System.out.println(user.nickname);

                    }
                }

            }
        });



    }





}