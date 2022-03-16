package com.lastproject.used_item_market;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class Login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    //여기에서는 구글 로그인 연동에 관해서만 연구한다.
    //구글 로그인 연동에 성공하였을 경우 구글 API에서 구글 이메일 값을 반환 받아서 toast값으로 화면에 출력한다.

    private SignInButton btn_google;                //구글 로그인 버튼
    private FirebaseAuth auth;                      //파이어 베이스 인증 객체
    private GoogleApiClient googleApiClient;        //구글 API 클라이언트 객체
    private static final int REQ_SIGN_GOOGLE = 100; //구글 로그인 결과 코드

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();

        auth = FirebaseAuth.getInstance();      //파이어베이스 인증 객체 초기화

        btn_google = findViewById(R.id.btn_google);
        btn_google.setOnClickListener(new View.OnClickListener() {      //버튼이 눌렸을 경우
            @Override
            public void onClick(View view) {        //구글 인증으로 넘어감

                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, REQ_SIGN_GOOGLE);


            }
        });
    }

    //구글 로그인 인증을 요청할 경유 결과 값을 되돌려 받는 곳이다
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQ_SIGN_GOOGLE){     //결과를 가져온다.
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess()){  //결과가 성공한 경우

                GoogleSignInAccount account = result.getSignInAccount();   //구글로 부터 온 결과가 다 담겨있다.
                resultLogin(account);   //로그인 결과값을 수행하는 메소드



            }
        }


    }

    private void resultLogin(GoogleSignInAccount account){

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCanceledListener(this, new OnCompleteListener<AuthResult>(){    //이 부분 다시 하기

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        System.out.println("lala")



                    }
                })
    }



    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {



    }
}