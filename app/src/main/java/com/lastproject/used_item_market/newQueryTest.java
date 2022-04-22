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

/*
    이거 방향성을 swipeRefreshLayout으로 가는데 이게 위로 스크롤인지 아래로 스크롤인지 구분이 불가능하다
    따라서 생각한 방법이 Recycleview가 마지막으로 터치된 곳을 기준으로 그곳이 딱 반을 나누어서 아래이면 아래 새로고침 30개 더 가져오고
    위면 새로 고침하여 초기화하는 방식으로 프로젝트를 진행하면 될 것 같다.
    지금 연구하게 되는것은 Recycleview가 되어진다.
    onRefresh 안에서 손가락이 떨어졌을 경우의 마지막 값을 저장하면 된다.
 */


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







    }
}