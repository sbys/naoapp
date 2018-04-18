package com.example.yuanshuai.nao.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.yuanshuai.nao.R;
import com.example.yuanshuai.nao.net.Net;

import okhttp3.ResponseBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class Pic extends AppCompatActivity {
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic);
        imageView=(ImageView)findViewById(R.id.pic);
        showPic();
    }
    private void showPic(){
        Net.getNet().getPic().map(new Func1<ResponseBody, Bitmap>() {
            @Override
            public Bitmap call(ResponseBody body) {
                Bitmap bitmap= BitmapFactory.decodeStream(body.byteStream());
                return bitmap;
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Bitmap>() {
                    @Override
                    public void call(Bitmap bitmap) {
                        final Bitmap b=bitmap;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                imageView.setBackground(new BitmapDrawable(b));
                            }
                        });
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
//                        showSnackBar(throwable.getMessage());
                    }
                });
    }
}
