package com.example.yuanshuai.nao.activity;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.yuanshuai.nao.R;
import com.example.yuanshuai.nao.model.Output;
import com.example.yuanshuai.nao.net.Net;
import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class Register extends AppCompatActivity {

    @BindView(R.id.number)
    EditText number;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.confirm)
    EditText confirm;
    @BindView(R.id.reg)
    Button reg;




//    private CoutDownTimerUtils coutDownTimerUtils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        ButterKnife.bind(this);
        init();
        initEvent();
    }
    public void showSnackBar(String message){
        final Snackbar snackbar=Snackbar.make(getWindow().getDecorView(),message,Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("知道了",new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private  void init(){
//        coutDownTimerUtils=new CoutDownTimerUtils(getcode,60000,1000);
    }

    private boolean confirmNumber(){
        String num=number.getText().toString();
        return true;

    }
    private void initEvent(){
//        RxView.clicks(getcode).subscribe(new Action1<Void>() {
//            @Override
//            public void call(Void aVoid) {
//                if(confirmNumber()){
//                    Net.getNet().sendSms(number.getText().toString())
//                            .subscribeOn(Schedulers.io())
//                            .observeOn(Schedulers.immediate())
//                            .subscribe(new Action1<Output>() {
//                                @Override
//                                public void call(Output output) {
//                                    Log.e("sms",""+output.getCode()+output.getCodeInfo()+output.getData());
//                                    if(output.getCode()==0){
//                                        coutDownTimerUtils.start();
//                                    }
//                                    else{
//                                        showSnackBar("发送错误");
//                                    }
//                                }
//                            }, new Action1<Throwable>() {
//                                @Override
//                                public void call(Throwable throwable) {
//                                    showSnackBar(throwable.getMessage());
//                                }
//                            });
//                }
//                else
//                {
//                    number.setError("请输入正确手机号");
//                }
//            }
//
//
//        });
        RxView.clicks(reg).throttleFirst(1000, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if(validate()){
                    Net.getNet().register(number.getText().toString(),password.getText().toString())
                            .subscribeOn(Schedulers.io())
                            .observeOn(Schedulers.immediate())
                            .subscribe(new Action1<Output>() {
                                           @Override
                                           public void call(Output output) {
//                                               Log.e("reg",""+output.getCode());
                                               if(output.getCode()==200){
                                                   showSnackBar("注册成功");
                                               }
                                               else if(output.getCode()==500){
                                                   showSnackBar("该账号已经被注册");
                                               }
                                               else{
                                                   showSnackBar("失败");
                                               }
                                           }
                                       },
                                    new Action1<Throwable>() {
                                        @Override
                                        public void call(Throwable throwable) {
                                            showSnackBar(throwable.getMessage());
                                        }
                                    });
                }
            }
        });
    }
    //验证密码
    private boolean validate(){
        if(password.getText().toString().length()<6){
            password.setError("密码长度应大于6");
            return false;
        }
        else if(!password.getText().toString().equals(confirm.getText().toString())){
            confirm.setError("两次密码不一致");
            return false;
        }
        else
            return true;
    }
}
