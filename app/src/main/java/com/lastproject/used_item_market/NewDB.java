package com.lastproject.used_item_market;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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

    Button addProductBtn;
    Button addProductBtn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_db);

        //DB 생성
        firestore = FirebaseFirestore.getInstance();
        userDocument = firestore.collection("Users");

        /*
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
        */
        //여기부터 리사이클 뷰

        CollectionReference productRef = firestore.collection("Proudct");
        //Query query = productRef.orderBy()













        //리사이클 뷰 끝

        addUser1();
        addUser2();
    }

    void addUser1(){

        //push 값 입력
        //firestore.collection("Users").add(user);
        //key 값 지정 입력
        //firestore.collection("Users").document("a").set(user);

        addProductBtn = (Button)findViewById(R.id.product_add_btn);
        addProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Product product = new Product();
                product.seller = "test";
                product.title = "테스트용 목록";
                product.cost = Integer.toUnsignedLong(99999999);
                product.category = "남성 의류";
                product.text = "test";
                product.time = Time.nowTime();
                firestore.collection("Product").add(product);

            }
        });
    }

    void addUser2(){

        //push 값 입력
        //firestore.collection("Users").add(user);
        //key 값 지정 입력
        //firestore.collection("Users").document("a").set(user);

        addProductBtn2 = (Button)findViewById(R.id.product_add_btn2);
        addProductBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Product product = new Product();
                product.seller = "test";
                product.title = "테스트용 목록";
                product.cost = Integer.toUnsignedLong(99999999);
                product.category = "여성 의류";
                product.text = "test";
                product.time = Time.nowTime();
                firestore.collection("Product").add(product);


            }
        });
    }


}