package com.mylife.refree;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Signup extends AppCompatActivity {
    EditText emailedit, passwordedit, repassword;
    Button register, cancel;
    Context context;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        context=this;
        emailedit=findViewById(R.id.email);
        passwordedit=findViewById(R.id.password);
        repassword=findViewById(R.id.repassword);
        register=findViewById(R.id.register);
        cancel=findViewById(R.id.cancel);

        firebaseAuth=FirebaseAuth.getInstance();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(context, LoginActivity.class));
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=emailedit.getText().toString().trim();
                String password=passwordedit.getText().toString().trim();
                String repasswordstr=repassword.getText().toString().trim();
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(context, "이메일을 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(context, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(repasswordstr)){
                    Toast.makeText(context, "비밀번호 재입력을 해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    firebaseAuth.signOut();
                                    finish();
                                    startActivity(new Intent(context, LoginActivity.class));
                                }
                                else{
                                    Toast.makeText(context, "오류가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}
