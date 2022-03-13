package com.lastproject.used_item_market;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class testintentLife extends AppCompatActivity {

    //여기로 이동한 다음에도 콘솔에서 계속 되는지 확인
    //인탠트를 하더라도 그 전 화면이 살아있는 것을 확인할 수 있다.
    //인탠트 직후에 바로 System.exit(0); 으로 없애야 한다.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testintent_life);

        System.out.println("여기로 넘어;");
        Button b1 = (Button)findViewById(R.id.back_button);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent a = new Intent(testintentLife.this, MainActivity.class);
                startActivity(a);

                System.exit(0);





            }
        });


    }
}