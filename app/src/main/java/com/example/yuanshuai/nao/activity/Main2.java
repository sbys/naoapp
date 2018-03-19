package com.example.yuanshuai.nao.activity;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.yuanshuai.nao.R;
import com.example.yuanshuai.nao.model.Family;
import com.example.yuanshuai.nao.model.Finfo;
import com.example.yuanshuai.nao.model.Output;
import com.example.yuanshuai.nao.net.Net;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class Main2 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    TextView fName;
    ImageView imageView;
    TextView t1;
    TextView t2;
    TextView t3;
    Boolean b1;
    Boolean b2;
    Boolean b3;
    SwitchCompat s1;
    SwitchCompat s2;
    SwitchCompat s3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
////                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        fName=(TextView)(navigationView.getHeaderView(0).findViewById(R.id.familyName));
        t1=(TextView)findViewById(R.id.t1);
        t2=(TextView)findViewById(R.id.t2);
        t3=(TextView)findViewById(R.id.t3);
        s1=(SwitchCompat)findViewById(R.id.b1);
        s2=(SwitchCompat)findViewById(R.id.b2);
        s3=(SwitchCompat)findViewById(R.id.b3);
        imageView=(ImageView)findViewById(R.id.image);
        init();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            AlertDialog.Builder builder=new AlertDialog.Builder(Main2.this);
            View view= LayoutInflater.from(Main2.this).inflate(R.layout.fs,null);
            ListView listView=(ListView) view.findViewById(R.id.list);
            builder.setView(R.layout.fs);
            builder.create().show();

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }
    private void init(){
        Net.getNet().getFu().subscribeOn(Schedulers.io())
                .observeOn(Schedulers.immediate())
                .subscribe(new Action1<Output<Family>>() {
                    @Override
                    public void call(Output<Family> familyOutput) {
                        if(familyOutput.getCode()==200){
                            fName.setText((familyOutput.getResult().getFname()));
                            Net.getNet().setFid(familyOutput.getResult().getFid());
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
//                        showSnackBar(throwable.getMessage());
                    }
                });
        Net.getNet().getData(Net.getNet().getFid()).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.immediate())
                .subscribe(new Action1<Output<Finfo>>() {
                    @Override
                    public void call(Output<Finfo> finfoOutput) {
                        Log.e("getData",""+finfoOutput.getResult().toString());
                        t1.setText(""+finfoOutput.getResult().getHumidity().toString());
                        t2.setText(""+finfoOutput.getResult().getTemperature().toString());
                        t3.setText(""+finfoOutput.getResult().getSmoke().toString());
                        s1.setChecked(finfoOutput.getResult().getD());
                        s2.setChecked(finfoOutput.getResult().getE());
                        s3.setChecked(finfoOutput.getResult().getF());
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
//                        showSnackBar("data"+throwable.getMessage());
                    }
                });
//        Net.getNet().getPic().subscribeOn(Schedulers.io())
//                .observeOn(Schedulers.immediate())
//                .subscribe(new Action1<ResponseBody>() {
//                    @Override
//                    public void call(ResponseBody body) {
//                        Bitmap bitmap= BitmapFactory.decodeStream(body.byteStream());
//                        imageView.setBackground(new BitmapDrawable(bitmap));
//                    }
//                }, new Action1<Throwable>() {
//                    @Override
//                    public void call(Throwable throwable) {
//                        showSnackBar("pic"+throwable.getMessage());
//                    }
//                });

//        Net.getNet().getPic().subscribeOn(Schedulers.io())
//                .observeOn(Schedulers.immediate())
//                .subscribe(new Action1<ResponseBody>() {
//                    @Override
//                    public void call(ResponseBody body) {
//                        Bitmap bitmap= BitmapFactory.decodeStream(body.byteStream());
//                        imageView.setBackground(new BitmapDrawable(bitmap));
//                    }
//                }, new Action1<Throwable>() {
//                    @Override
//                    public void call(Throwable throwable) {
//                        showSnackBar("pic"+throwable.getMessage());
//                    }
//                });
        Net.getNet().getPic().map(new Func1<ResponseBody, Bitmap>() {
            @Override
            public Bitmap call(ResponseBody body) {
                Bitmap bitmap=BitmapFactory.decodeStream(body.byteStream());
                return bitmap;
            }
        }).subscribeOn(Schedulers.io()).observeOn(Schedulers.immediate())
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

        s1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int commond=0;
                if(isChecked){
                   commond=0;
                }
                else
                    commond=1;
                Net.getNet().commond(commond).subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.immediate())
                        .subscribe(new Action1<Output>() {
                            @Override
                            public void call(Output output) {
//                                showSnackBar(""+output.getCode());
                            }
                        });
            }
        });
        s2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                int commond=2;
                if(isChecked){
                    commond=2;
                }
                else
                    commond=3;
                Net.getNet().commond(commond).subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.immediate())
                        .subscribe(new Action1<Output>() {
                            @Override
                            public void call(Output output) {
//                                showSnackBar(""+output.getCode());
                            }
                        });

            }
        });
        s3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int commond=4;
                if(isChecked){
                    commond=4;
                }
                else
                    commond=5;
                Net.getNet().commond(commond).subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.immediate())
                        .subscribe(new Action1<Output>() {
                            @Override
                            public void call(Output output) {
//                                showSnackBar(""+output.getCode());
                            }
                        });

            }
        });
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
}
