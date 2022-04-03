package com.lastproject.used_item_market;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class PostPage extends AppCompatActivity {

    String mykey;
    String myUniv;

    //스피너만 따로
    Spinner purposeSpinner;          //모집인원
    Spinner categorySpinner;         //년도

    //가져온 스피너 결과 값
    String result_purpose = "";
    String result_category = "";

    //DB 관련
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    //이미지 관련
    ArrayList<String> imgarray = new ArrayList<String>();       //이미지 이진화 모음
    int requestCode;
    private RecyclerView rv;
    public RecyclePostAdapter adapter;
    TextView img_countView;                                //사진 개수




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_page);

        //파이어 베이스 데이터베이스 연동
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        rv = (RecyclerView)findViewById(R.id.prouduct_recycleview);
        img_countView = (TextView)findViewById(R.id.count_img);




        Back();
        Done();
        setPurposeSpinner();
        setCategorySpinner();
        saveImage();
    }

    void Back(){
        ImageButton back = (ImageButton)findViewById(R.id.xbtn);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PostPage.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    void Done(){ // 체크버튼 클릭 시 게시글 작성 완료
        ImageButton done = (ImageButton)findViewById(R.id.done);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {












            }
        });
    }

    void setPurposeSpinner(){
        purposeSpinner = (Spinner) findViewById(R.id.purpose);
        ArrayAdapter ppAdapter = ArrayAdapter.createFromResource(this,R.array.purpose, android.R.layout.simple_spinner_dropdown_item);
        ppAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //선택목록이 나타날 때 사용할 레이아웃 지정
        purposeSpinner.setAdapter(ppAdapter);  //스피너에 어댑터 적용

        purposeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                result_purpose = purposeSpinner.getSelectedItem().toString(); // 스피너 선택값 가져오기
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    void setCategorySpinner(){
        categorySpinner = (Spinner) findViewById(R.id.category);
        ArrayAdapter cgAdapter = ArrayAdapter.createFromResource(this,R.array.category, android.R.layout.simple_spinner_dropdown_item);
        cgAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //선택목록이 나타날 때 사용할 레이아웃 지정
        categorySpinner.setAdapter(cgAdapter);  //스피너에 어댑터 적용

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                result_category = categorySpinner.getSelectedItem().toString(); // 스피너 선택값 가져오기
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    void saveImage(){

        ImageButton bt_saveimg = (ImageButton)findViewById(R.id.add_img_btn);
        bt_saveimg.setOnClickListener(new View.OnClickListener() {      //사진 추가를 위해 이미지가 눌렸을 경우
            @Override
            public void onClick(View view) {

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
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);        //비트맵으로 가져온다.
            ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.WEBP, 100, stream1);
            byte[] img1Byte = stream1.toByteArray();
            String img = ParseIMG.byteArrayToBinaryString(img1Byte); //(성공)

            if(imgarray.size() < 10){       //사진을 10개까지 받는다.
                //배열에 저장
                imgarray.add(img);
                //사진 개수 카운트
                img_countView.setText("사진 추가(" + imgarray.size() + "/10)");
                //여기서 리사이클 뷰 실행
                init();
            }

        }
    }

    //리사이클 뷰 동작
    void init(){

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);             //이렇게 하면 수평으로 생성
        rv.setLayoutManager(linearLayoutManager);
        adapter = new RecyclePostAdapter();
        for(int i = 0; i < imgarray.size(); i++){
            adapter.addItem(imgarray.get(i));
        }
        rv.addItemDecoration(new RecyclerDecoration(5));       //간격을 추가한다.
        rv.setAdapter(adapter);

    }


}