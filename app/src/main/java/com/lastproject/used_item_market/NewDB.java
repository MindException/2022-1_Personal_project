package com.lastproject.used_item_market;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class NewDB extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    FirebaseFirestore firestore;
    CollectionReference userDocument;
    CollectionReference productRef;

    Button addProductBtn;
    Button addProductBtn2;

    private int limit = 8;         //요청 상품 수
    private boolean isScrolling = false;
    private boolean isLastItemReached = false;
    private DocumentSnapshot lastVisible;       //마지막 스냅샷 커서 저장
    RecyclerView recyclerView;
    RecycleSellAdapter recycleSellAdapter;
    RecyclerView.OnScrollListener onScrollListener;

    List<Product> productList = new ArrayList<>();

    String category = "모두보기";       //기본값

    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_db);

        //DB 생성
        firestore = FirebaseFirestore.getInstance();
        userDocument = firestore.collection("Users");
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeLayout);

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
        //리사이클 뷰 기초 세팅
        recyclerView = (RecyclerView)findViewById(R.id.products_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recycleSellAdapter = new RecycleSellAdapter(productList);
        recyclerView.setAdapter(recycleSellAdapter);
        swipeRefreshLayout.setOnRefreshListener(this);

        //쿼리 시작
        productRef = firestore.collection("Product");
        Query query = productRef.whereEqualTo("purpose", "판매")
                .whereEqualTo("category", "남성 의류")
                .orderBy("time", Query.Direction.DESCENDING).limit(limit);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){           //가져오는데 성공
                    if (task.getResult().size() <= 0){          //물건이 없는 경우
                        Toast.makeText(NewDB.this, "상품 없음", Toast.LENGTH_SHORT).show();
                    }else{      //물건이 있다.
                        for(DocumentSnapshot document : task.getResult()){
                            Product product = document.toObject(Product.class);
                            productList.add(product);
                        }
                        //상품 추가했으니 어뎁터 갱신
                        recycleSellAdapter.notifyDataSetChanged();
                        lastVisible = task.getResult().getDocuments().get(task.getResult().size() - 1);

                        //스크롤 리스너 추가
                        onScrollListener = new RecyclerView.OnScrollListener() {
                            @Override
                            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                                super.onScrollStateChanged(recyclerView, newState);
                                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                                    isScrolling = true;
                                }
                            }

                            //스크롤이 limit만큼 내려갈 시 다음 데이터를 limit만큼 읽어서 출력
                            @Override
                            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                                super.onScrolled(recyclerView, dx, dy);

                                //위치정보 가져오기
                                LinearLayoutManager linearLayoutManager = (LinearLayoutManager)recyclerView.getLayoutManager();

                                int firstVisiblePosition = linearLayoutManager.findFirstVisibleItemPosition();
                                int visibleItemCount = linearLayoutManager.getChildCount();
                                int totalItemCount = linearLayoutManager.getItemCount();

                                //스크롤 조건 시작
                                if(isScrolling && (firstVisiblePosition + visibleItemCount == totalItemCount) && !isLastItemReached ){

                                    isScrolling = false;
                                    //추가 쿼리
                                    Query nextQuery = productRef.whereEqualTo("purpose", "판매")
                                            .whereEqualTo("category", "남성 의류")
                                            .orderBy("time", Query.Direction.DESCENDING)
                                            .startAfter(lastVisible).limit(limit);
                                    nextQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> nextTask) {
                                            if(nextTask.isSuccessful()){   //가져오는거 성공
                                              if(nextTask.getResult().size() > 0) {
                                                  for(DocumentSnapshot nextDocument : nextTask.getResult()){
                                                      Product product = nextDocument.toObject(Product.class);
                                                      productList.add(product);
                                                  }
                                                  //어뎁터 또 갱신
                                                  recycleSellAdapter.notifyDataSetChanged();
                                                  lastVisible = nextTask.getResult().getDocuments().get(nextTask.getResult().size() - 1);

                                                  if(nextTask.getResult().size() < limit){      //더 이상 갱신할 필요가 없다.
                                                      isLastItemReached = true;
                                                  }
                                              }
                                            }
                                        }
                                    });

                                }//스크롤 조건 끝

                            }
                        };
                        //스크롤 리스너 끝

                        //스크롤 리스너 추가
                        recyclerView.addOnScrollListener(onScrollListener);

                    }
                }//수신 성공
            }
        });//메인 쿼리











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

    @Override
    public void onRefresh() {       //새로고침 코드

        //초기화
        isScrolling = true;
        isLastItemReached = false;
        category = "남성 의류";
        productList = new ArrayList<>();
        //어뎁터를 새로 설치해줘야 한다.
        recycleSellAdapter = new RecycleSellAdapter(productList);
        recyclerView.setAdapter(recycleSellAdapter);

        if (category.equals("모두보기")){
            //카테고리가 정해지지 않은 기본보기
        }else{
            productRef = firestore.collection("Product");
            Query query = productRef.whereEqualTo("purpose", "판매")
                    .whereEqualTo("category", "남성 의류")
                    .orderBy("time", Query.Direction.DESCENDING).limit(limit);
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()){           //가져오는데 성공
                        if (task.getResult().size() <= 0){          //물건이 없는 경우
                            Toast.makeText(NewDB.this, "상품 없음", Toast.LENGTH_SHORT).show();
                        }else{      //물건이 있다.
                            for(DocumentSnapshot document : task.getResult()){
                                Product product = document.toObject(Product.class);
                                productList.add(product);
                            }
                            //상품 추가했으니 어뎁터 갱신
                            recycleSellAdapter.notifyDataSetChanged();
                            lastVisible = task.getResult().getDocuments().get(task.getResult().size() - 1);

                            //스크롤 리스너 추가
                            onScrollListener = new RecyclerView.OnScrollListener() {
                                @Override
                                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                                    super.onScrollStateChanged(recyclerView, newState);
                                    if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                                        isScrolling = true;
                                    }
                                }

                                //스크롤이 limit만큼 내려갈 시 다음 데이터를 limit만큼 읽어서 출력
                                @Override
                                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                                    super.onScrolled(recyclerView, dx, dy);

                                    //위치정보 가져오기
                                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager)recyclerView.getLayoutManager();

                                    int firstVisiblePosition = linearLayoutManager.findFirstVisibleItemPosition();
                                    int visibleItemCount = linearLayoutManager.getChildCount();
                                    int totalItemCount = linearLayoutManager.getItemCount();

                                    //스크롤 조건 시작
                                    if(isScrolling && (firstVisiblePosition + visibleItemCount == totalItemCount) && !isLastItemReached ){

                                        isScrolling = false;
                                        //추가 쿼리
                                        Query nextQuery = productRef.whereEqualTo("purpose", "판매")
                                                .whereEqualTo("category", "남성 의류")
                                                .orderBy("time", Query.Direction.DESCENDING)
                                                .startAfter(lastVisible).limit(limit);
                                        nextQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> nextTask) {
                                                if(nextTask.isSuccessful()){   //가져오는거 성공
                                                    if(nextTask.getResult().size() > 0) {
                                                        for(DocumentSnapshot nextDocument : nextTask.getResult()){
                                                            Product product = nextDocument.toObject(Product.class);
                                                            productList.add(product);
                                                        }
                                                        //어뎁터 또 갱신
                                                        recycleSellAdapter.notifyDataSetChanged();
                                                        lastVisible = nextTask.getResult().getDocuments().get(nextTask.getResult().size() - 1);

                                                        if(nextTask.getResult().size() < limit){      //더 이상 갱신할 필요가 없다.
                                                            isLastItemReached = true;
                                                        }
                                                    }
                                                }
                                            }
                                        });

                                    }//스크롤 조건 끝

                                }
                            };
                            //스크롤 리스너 끝

                            //스크롤 리스너 추가
                            recyclerView.addOnScrollListener(onScrollListener);

                        }
                    }else{  //수신 실패
                        System.out.println("수신 실패");
                    }
                }
            });//메인 쿼리

            swipeRefreshLayout.setRefreshing(false);        //업데이트 끝

        }//if문 끝

    }
}