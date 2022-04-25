package com.lastproject.used_item_market;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/*
    이거 방향성을 swipeRefreshLayout으로 가는데 이게 위로 스크롤인지 아래로 스크롤인지 구분이 불가능하다
    따라서 생각한 방법이 Recycleview가 마지막으로 터치된 곳을 기준으로 그곳이 딱 반을 나누어서 아래이면 아래 새로고침 30개 더 가져오고
    위면 새로 고침하여 초기화하는 방식으로 프로젝트를 진행하면 될 것 같다.
    지금 연구하게 되는것은 Recycleview가 되어진다.
    onRefresh 안에서 손가락이 떨어졌을 경우의 마지막 값을 저장하면 된다.
 */

/*
*    지금까지의 생각으로는 리사이클 뷰가 맨 아래에 도착했을 경우 30개를 더 받아오는 것을 기준으로 한다.
*    Prodcut라는 Arraylist를 만든다.
*
*    뭔가 조건이 변경할때마다 init()으로 화면을 초기화해줘야 한다.
*
* */


public class newQueryTest extends AppCompatActivity {  //클라이언트 부담을 줄이기 위한 데이터베이스 상향

    //DB 관련
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    //정보 저장
    ArrayList<Product> products = new ArrayList<Product>();                 //물품 정보
    ArrayList<String> product_key = new ArrayList<String>();          //물품 키 값 저장

    //리사이클뷰
    RecyclerView recyclerView;
    RecycleSellAdapter adapter;

    //
    int lastposition = 0;           //데이터를 더 불러오기 전에 마지막 위치(인덱스 번호)

    String category = "";           //카테고리 별로 볼 수 있다.
    String search_keyword = "";     //검색 내용 저장

    boolean trigger = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_query_test);

        //파이어 베이스 데이터베이스 연동
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();







        Product sampel_product = new Product();


        //파이어 베이스 데이터베이스 연동
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        myRef.child("Product").push().setValue(sampel_product);


        //리사이클 뷰
        recyclerView = (RecyclerView)findViewById(R.id.product_list);

        //아직 조건이 없기때문에 처음에는
        Query firstDataSet = myRef.child("Product").orderByChild("purpose").equalTo("판매")
                .limitToLast(6);     //마지막 몇개씩 가져올 것인가.
        firstDataSet.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                System.out.println( "자식 개수" + snapshot.getChildrenCount());
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    products.add(dataSnapshot.getValue(Product.class));
                    product_key.add(dataSnapshot.getKey());
                }
                //최신순을 위하여 역방향 정렬
                Collections.reverse(products);
                Collections.reverse(product_key);
                for(String x : product_key){
                    System.out.println(x);
                }
                init();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //스크롤 뷰가 맨아래에 도착하였을 경우
        recyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {

                if (!view.canScrollVertically(1)){      //최하단을 가르킨다.
                    lastposition = products.size() - 1;         //최하단 인덱스를 저장
                    System.out.println(lastposition);

                    //여기서 이제 개수 더 추가하기
                    if(!category.equals("")){                 //카테고리가 있는 경우

                    }else if(!search_keyword.equals("")){     //검색 내용이 있는 것

                    }else{          //검색 카테고리가 다 없다.

                        String last_product_key = product_key.get(0);
                        System.out.println(last_product_key);
                        Query addData = myRef.child("Product").endBefore(last_product_key)
                                .orderByChild("purpose").equalTo("판매").limitToLast(5);
                        addData.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                    System.out.println( "뒤에 추가" + dataSnapshot.getKey());
                                }



                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }//조건문 끝
                }//최하단 반응 if문 끝
            }
        });//스크롤 뷰 세팅 끝

    }//

    //리사이클 뷰 구성
    void init(){

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new RecycleSellAdapter();
        adapter.setOnItemClickListener(new RecycleSellAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                System.out.println(pos + "번 클릭");       //시작이 0번이다.

                //화면 이동시 여기로 인탠트


            }
        });
        for(int i = 0; i < products.size(); i++){
            adapter.addItem(products.get(i));
        }
        recyclerView.setAdapter(adapter);
        //recyclerView.scrollToPosition(products.size() - 1); 화면 이동

    }
}