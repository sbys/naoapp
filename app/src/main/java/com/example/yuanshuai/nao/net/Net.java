package com.example.yuanshuai.nao.net;


import android.content.Intent;
import android.util.Log;


import com.example.yuanshuai.nao.model.Family;
import com.example.yuanshuai.nao.model.Finfo;
import com.example.yuanshuai.nao.model.Output;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * Created by 12917 on 2017/7/15.
 */

public class Net {
//    用户属性

    private String name;
    private int fid=1;
    private static Net net;
    private String url="http://139.199.68.47:8080/nao/";
        private Retrofit retrofit;
        private OkHttpClient.Builder okHttpClient;
        private NetApi ret;
    public static synchronized Net getNet(){
        if(net==null)
            net=new Net();
        return net;
    }
    private Net(){
        okHttpClient=new OkHttpClient.Builder();
        okHttpClient.cookieJar(new CookieJar() {
            private final HashMap<String, List<Cookie>> cookieStore = new HashMap<String, List<Cookie>>();
            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                cookieStore.put(url.host(), cookies);
                Log.e("a",""+cookies.get(0).toString());
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {
                List<Cookie> cookies = cookieStore.get(url.host());
                return cookies != null ? cookies : new ArrayList<Cookie>();
            }
        });


        retrofit=new Retrofit.Builder()
                .baseUrl(url)
                .client(okHttpClient.build())
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        ret=retrofit.create(NetApi.class);
    }


//    注册
    public Observable<Output> register(String name,String pass){
        return ret.register(name,pass);
    }
//    登陆
    public Observable<Output> login(String name,String password){
        this.name=name;
        return  ret.login(name,password);
    }

//    获取fu
    public Observable<Output<Family>> getFu(){
        Log.e("name",name);
        return ret.getFu(name);
    }
//    获取数据
    public Observable<Output<Finfo>> getData(Integer  fid){
        return ret.getData(Integer.toString(fid));
    }
//    获取图片
    public Observable<ResponseBody> getPic(){
        return ret.getPic(Integer.toString(fid));
    }
//    控制
    public Observable<Output> commond(Integer cid){
        return  ret.commond(Integer.toString(cid),Integer.toString(fid));
    }
    public Observable<ResponseBody> getFu2(){
        return  ret.getFu2(name);
    }





//    登陆1
    public Observable<ResponseBody> login1(String name,String password){
       return ret.login1(name,password);
    }
    public void setFid(int fid) {
        this.fid = fid;
    }

    public int getFid() {
        return fid;
    }

    public OkHttpClient.Builder getOkHttpClient() {
        return okHttpClient;
    }

    /*登出*/
    public Observable<Output> logout(){
        return ret.logout();
    }
    /*请求验证码*/
    public Observable<Output> getcode(String number){
        return ret.getCode(number);
    }
    /*注册*/
    public Observable<Output> reg(String username,String password,String code){
        return ret.reg(username,password,"name",code);
    }

}
