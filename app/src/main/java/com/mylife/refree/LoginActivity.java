package com.mylife.refree;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    EditText email, password;
    Button login_button, register_button;
    FirebaseAuth firebaseAuth;
    Context context;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        context=this;
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        login_button=findViewById(R.id.login_button);
        register_button=findViewById(R.id.register_button);

        firebaseAuth=FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()==null){
            login_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String emailstr=email.getText().toString().trim();
                    String passwordstr=password.getText().toString().trim();
                    if(TextUtils.isEmpty(emailstr)){
                        Toast.makeText(context, "이메일을 입력해 주세요", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(TextUtils.isEmpty(passwordstr)){
                        Toast.makeText(context, "비밀번호를 입력해 주세요", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    firebaseAuth.signInWithEmailAndPassword(emailstr, passwordstr)
                            .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        finish();
                                        startActivity(new Intent(context, MainActivity.class));
                                    }
                                    else{
                                        Toast.makeText(context, "다시 로그인해주세요", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            });

            register_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    startActivity(new Intent(context, Signup.class));
                }
            });
        }
        else{
            System.out.println(firebaseAuth.getCurrentUser());
            System.out.println(firebaseAuth.getCurrentUser().getEmail());
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }


    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setCancelable(false)
                .setMessage("앱을 종료하시겠어요?")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        moveTaskToBack(true);
                        finish();
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }
}
