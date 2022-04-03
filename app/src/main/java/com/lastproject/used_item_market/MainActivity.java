package com.lastproject.used_item_market;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button lifebutton;
    Button login_button;

    //DB 관련
    private FirebaseDatabase database;
    private DatabaseReference myRef;


    ImageView iv;
    private int requestCode;

    //테스트용
    University test1 = new University("b100대학교 인문관", 30.1);
    University test2 = new University("b100대학교", 30.0);
    University test3 = new University("c100대학", 40);
    University test4 = new University("a100대학교 인문관", 20.4);
    University test5 = new University("a100대학교 본관", 20.1);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //파이어 베이스 데이터베이스 연동
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        /*
        //생명주기 연습
        myRef.addValueEventListener(new ValueEventListener() {      //테스트로 추가될 때마다
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {



                System.out.println("아직 살아있음");




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        */

        //생명주기 이동
        lifebutton = (Button)findViewById(R.id.lifecycle_button);
        lifebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent a = new Intent(MainActivity.this, testintentLife.class);
                startActivity(a);
                System.out.println("인탠트: 화면 전환");
                System.exit(0);

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
        imageButton();
        writePostButton();


        //거리 순 정렬 테스트
        ArrayList<University> univlist = new ArrayList<University>();
        univlist.add(test1);
        univlist.add(test2);
        univlist.add(test3);
        univlist.add(test4);
        univlist.add(test5);
        univlist.sort(new CompareUnivDistance<University>());               //거리 정렬은 정확히 잘 됨
        for(int i = 0; i < univlist.size(); i++){
            System.out.println(univlist.get(i).university);
        }
        System.out.println("ㅡㅡㅡㅡㅡㅡㅡ");
        //여기서 이제 중복 대학을 없애는 코드를 생성한다.
        ArrayList<University> stackUniv = new ArrayList<University>();      //순서대로 되었으니 대학만 받을 것이다.
        int trigger = 0;            //1이 되면 저장한적이 있음으로 저장을 안한다.
        for(int i = 0; i < univlist.size(); i++){

            int endIndex = 0;       //대학교라는 글자의 시작 인덱스를 가져온다.
            endIndex = univlist.get(i).university.indexOf("대학교");       //대학교에서 대의 시작 인덱스를 가져온다.
            if(endIndex != -1){         //대학교로 검색하여 나온 경우

                univlist.get(i).university = univlist.get(i).university.substring(0,endIndex+3);  //이렇게 하면 대학교까지의 이름만 가져온다.
                if(stackUniv.size() == 0){       //아무것도 없을 경우인 처음에는 그냥 넣는다.
                    stackUniv.add(univlist.get(i));
                }else{
                    for(int j = 0; j < stackUniv.size(); j++){

                        if(univlist.get(i).university.equals(stackUniv.get(j).university)){     //서로 대학이 같지 않을 경우만 추가
                            trigger = 1;        //저장한 적이 있다.
                        }
                    }
                    if(trigger == 0){
                        stackUniv.add(univlist.get(i));
                    }
                }

            }else{                 //대학으로 검색하여 나온 경우

                endIndex = univlist.get(i).university.indexOf("대학");       //대학에서 대의 시작 인덱스를 가져온다.
                univlist.get(i).university = univlist.get(i).university.substring(0,endIndex+2);  //이렇게 하면 대학교까지의 이름만 가져온다.
                if(stackUniv.size() == 0){       //아무것도 없을 경우인 처음에는 그냥 넣는다.
                    stackUniv.add(univlist.get(i));
                }else{
                    for(int j = 0; j < stackUniv.size(); j++){

                        if(univlist.get(i).university.equals(stackUniv.get(j).university)){     //서로 대학이 같지 않을 경우만 추가
                            trigger = 1;        //저장한 적이 있다.
                        }
                    }
                    if(trigger == 0){
                        stackUniv.add(univlist.get(i));
                    }
                }

            }

            trigger = 0;

        }//대학교 중복 없애기(성공)
        System.out.println(stackUniv.size());
        for(int i = 0; i < stackUniv.size(); i++){
            System.out.println(stackUniv.get(i).university);
        }




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

    void imageButton(){         //프로필 사진 변경 및 저장

        iv = (ImageView)findViewById(R.id.img);
        iv.setOnClickListener(new View.OnClickListener() {      //프로필 사진이 눌렸을 경우
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                requestCode = 1;
                startActivityForResult(intent, 1);      //코드 번호로 아래에서 동작한다.

            }
        });

    }

    //갤러리에서 가져온 이미지 저장(완성)
    @Override
    protected void onActivityResult(int requstCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            InputStream inputStream = null;
            try {
                inputStream = getContentResolver().openInputStream(uri);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            iv.setImageBitmap(bitmap);


            //이제 여기서 서버에도 저장
            //먼저 바이트 처리하기
            BitmapDrawable drawable = (BitmapDrawable)iv.getDrawable();
            Bitmap bitmap1 = drawable.getBitmap();
            ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
            bitmap1.compress(Bitmap.CompressFormat.WEBP, 100, stream1);     //구글에서 만든 무손실(WEBP)
            byte[] img1Byte = stream1.toByteArray();
            String img = ParseIMG.byteArrayToBinaryString(img1Byte); //(성공)


            //여기는 이진화된 코드를 다시 이미지로 만들어준다.
            Bitmap bmp;
            byte[] bytes = ParseIMG.binaryStringToByteArray(img);
            bmp = BitmapFactory.decodeByteArray(bytes, 0 , bytes.length);
            iv.setImageBitmap(bmp);
            iv.setClipToOutline(true);              //모양에 맞게 사진 자르기

        }
    }

    //게시글 작성 연습
    void writePostButton(){

        Button btn_post = findViewById(R.id.postpage_btn);
        btn_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent a12 = new Intent(MainActivity.this, PostPage.class);
                startActivity(a12);

            }
        });

    }


}