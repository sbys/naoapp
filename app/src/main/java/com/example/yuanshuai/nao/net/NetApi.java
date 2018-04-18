package com.example.yuanshuai.nao.net;


import com.example.yuanshuai.nao.model.Family;
import com.example.yuanshuai.nao.model.Finfo;
import com.example.yuanshuai.nao.model.Output;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

import static com.example.yuanshuai.nao.net.NetApi.pubcommon;


/**
 * Created by 12917 on 2017/7/18.
 */

public interface NetApi {

    String pubcommon="user/";
    @POST(pubcommon+"register")
    @FormUrlEncoded
    Observable<Output> register(@Field("name")String name,@Field("pass") String pass);
    @POST(pubcommon+"login")
    @FormUrlEncoded
    Observable<Output> login(@Field("name") String username, @Field("pass") String password);
//    获取family
    @POST(pubcommon+"getFu")
    @FormUrlEncoded
    Observable<Output<Family>> getFu(@Field("uid") String name);
//    获取数据
    @POST(pubcommon+"getData")
    @FormUrlEncoded
    Observable<Output<Finfo>> getData(@Field("fid")String name);
//    获取图片
    @POST("family/downpic")
    @FormUrlEncoded
    Observable<ResponseBody> getPic(@Field("fid")String fid);
//    命令
    @POST(pubcommon+"commond")
    @FormUrlEncoded
    Observable<Output> commond(@Field("cid")String cid,@Field("fid")String fid);
//    获取列表
    @POST(pubcommon+"getFamily")
    Observable<Output<List<Family>>> getFList();
//    绑定关系
    @POST(pubcommon+"fu")
    @FormUrlEncoded
    Observable<Output> fu(@Field("fid")Integer fid,@Field("uid")String uid);
//    解除关系
    @POST(pubcommon+"release")
    @FormUrlEncoded
    Observable<ResponseBody> release(@Field("fid")Integer fid,@Field("uid")String uid);
//    自动手动控制
    @POST(pubcommon+"control")
    @FormUrlEncoded
    Observable<Output> control(@Field("fid")Integer fid,@Field("control")Boolean b);








    //    获取数据
    @POST(pubcommon+"getData")
    @FormUrlEncoded
    Observable<Output<ResponseBody>> getData1(@Field("name")String name);
    @POST(pubcommon+"login")
    @FormUrlEncoded
    Observable<ResponseBody> login1(@Field("name") String username, @Field("pass") String password);
    @GET
    Observable<Output> logout();
    /*请求发送验证码*/
    @GET("app/getcode")
    Observable<Output> getCode(@Query("number") String number);
    /*注册*/
    @GET("app/reg")
    Observable<Output> reg(@Query("username") String username, @Query("password") String password, @Query("name") String name, @Query("code") String code);



}
