package com.example.yuanshuai.nao.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yuanshuai.nao.R;
import com.example.yuanshuai.nao.adapter.MyAdapter;
import com.example.yuanshuai.nao.model.Family;
import com.example.yuanshuai.nao.model.Finfo;
import com.example.yuanshuai.nao.model.Output;
import com.example.yuanshuai.nao.net.Net;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class Main2 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Log.e("db","init");
            init();
            return false;
        }
    });
    Thread thread;
    TextView fName;
    TextView t1;
    TextView t2;
    TextView t3;
    TextView t4;
    Boolean b1;
    Boolean b2;
    Boolean b3;
    Boolean b4;
    Boolean b5;
    Boolean b6;
    Boolean b7;
    SwitchCompat s1;
    SwitchCompat s2;
    SwitchCompat s3;
    SwitchCompat s4;
    SwitchCompat s5;
    SwitchCompat s6;
    SwitchCompat s7;
    SwitchCompat s8;
    Button door;
    ImageButton go;
    ImageButton left;
    ImageButton right;
    ImageButton stop;
    ImageButton say;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Main2.this,Pic.class);
                startActivity(intent);
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
        t4=(TextView)findViewById(R.id.t4);
        s1=(SwitchCompat)findViewById(R.id.b1);
        s2=(SwitchCompat)findViewById(R.id.b2);
        s3=(SwitchCompat)findViewById(R.id.b3);
        s4=(SwitchCompat)findViewById(R.id.b4);
        s5=(SwitchCompat)findViewById(R.id.b5);
        s6=(SwitchCompat)findViewById(R.id.b6);
        s7=(SwitchCompat)findViewById(R.id.b7);
        s8=(SwitchCompat)findViewById(R.id.b8);
        go=(ImageButton)findViewById(R.id.go);
        stop=(ImageButton)findViewById(R.id.stop);
        left=(ImageButton)findViewById(R.id.left);
        right=(ImageButton)findViewById(R.id.right);
        say=(ImageButton)findViewById(R.id.say);

        init();
        thread=new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(2000);
                        Message message=new Message();
                        message.what=1;
                        handler.sendMessage(message);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
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
            Net.getNet().getFList()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Output<List<Family>>>() {
                        @Override
                        public void call(final Output<List<Family>> listOutput) {
                            if(listOutput.getCode()==200){
                                MyAdapter myAdapter=new MyAdapter(listOutput.getResult(),Main2.this);
                                AlertDialog.Builder builder=new AlertDialog.Builder(Main2.this);
                                View view= LayoutInflater.from(Main2.this).inflate(R.layout.fs,null);
                                ListView listView=(ListView) view.findViewById(R.id.list);
                                listView.setAdapter(myAdapter);
                                builder.setView(view);
                                final AlertDialog alertdialog=builder.create();
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        Net.getNet().fu(listOutput.getResult().get(position).getFid())
                                                .subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(new Action1<Output>() {
                                                    @Override
                                                    public void call(Output output) {
                                                        showSnackBar(output.getResult().toString());
                                                        alertdialog.dismiss();
                                                    }
                                                }, new Action1<Throwable>() {
                                                    @Override
                                                    public void call(Throwable throwable) {
                                                        showSnackBar(throwable.getMessage());
                                                    }
                                                });
                                    }
                                });
                                alertdialog.show();
                            }

                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            showSnackBar(throwable.getMessage());
                        }
                    });

        } else if (id == R.id.nav_gallery) {
            Net.getNet().getFu()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Output<Family>>() {
                        @Override
                        public void call(final Output<Family> familyOutput) {
                            AlertDialog.Builder builder=new AlertDialog.Builder(Main2.this);
                            builder.setMessage("你确定取消与"+familyOutput.getResult().getFname()+"的关系吗？");
                            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(final DialogInterface dialog, int which) {
                                    Net.getNet().release(familyOutput.getResult().getFid())
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(new Action1<ResponseBody>() {
                                                @Override
                                                public void call(ResponseBody output) {
                                                    dialog.dismiss();
//                                                    showSnackBar(output.getResult().toString());
                                                    Log.e("db",output.toString());
                                                }
                                            }, new Action1<Throwable>() {
                                                @Override
                                                public void call(Throwable throwable) {
                                                    showSnackBar(throwable.getMessage());
                                                }
                                            });
                                }
                            });
                            builder.create().show();
                        }
                    });


        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_share) {
            Intent intent=new Intent(Main2.this,Login.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
        return true;

    }
    private void init(){
        Net.getNet().getFu().subscribeOn(Schedulers.io())
                .observeOn(Schedulers.immediate())
                .subscribe(new Action1<Output<Family>>() {
                    @Override
                    public void call(final Output<Family> familyOutput) {
                        if(familyOutput.getCode()==200){
                            fName.setText((familyOutput.getResult().getFname()));
                            Net.getNet().setFid(familyOutput.getResult().getFid());
                            Net.getNet().getData(Net.getNet().getFid()).subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Action1<Output<Finfo>>() {
                                        @Override
                                        public void call(Output<Finfo> finfoOutput) {
                                            Log.e("getData",""+finfoOutput.getResult().toString());
                                            t1.setText(""+finfoOutput.getResult().getHumidity().toString());
                                            t2.setText(""+finfoOutput.getResult().getTemperature().toString());
                                            t3.setText(""+finfoOutput.getResult().getSmoke().toString());
                                            t4.setText(""+finfoOutput.getResult().getA());
                                            s1.setChecked(finfoOutput.getResult().getD());
                                            s2.setChecked(finfoOutput.getResult().getE());
                                            s3.setChecked(finfoOutput.getResult().getF());
                                            s4.setChecked(finfoOutput.getResult().getG());
                                            s5.setChecked(finfoOutput.getResult().getH());
                                            s6.setChecked(finfoOutput.getResult().getI());
                                            s7.setChecked(finfoOutput.getResult().getJ());
                                            s8.setChecked(finfoOutput.getResult().getM());
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
//                            Net.getNet().getPic().map(new Func1<ResponseBody, Bitmap>() {
//                                @Override
//                                public Bitmap call(ResponseBody body) {
//                                    Bitmap bitmap=BitmapFactory.decodeStream(body.byteStream());
//                                    return bitmap;
//                                }
//                            }).subscribeOn(Schedulers.io()).observeOn(Schedulers.immediate())
//                                    .subscribe(new Action1<Bitmap>() {
//                                        @Override
//                                        public void call(Bitmap bitmap) {
//                                            final Bitmap b=bitmap;
//                                            runOnUiThread(new Runnable() {
//                                                @Override
//                                                public void run() {
//                                                    imageView.setBackground(new BitmapDrawable(b));
//                                                }
//                                            });
//                                        }
//                                    }, new Action1<Throwable>() {
//                                        @Override
//                                        public void call(Throwable throwable) {
////                        showSnackBar(throwable.getMessage());
//                                        }
//                                    });

                            s1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    int commond=2;
                                    if(isChecked){
                                        commond=21;
                                    }
                                    else
                                        commond=2;
                                    Net.getNet().commond(commond).subscribeOn(Schedulers.io())
                                            .observeOn(Schedulers.immediate())
                                            .subscribe(new Action1<Output>() {
                                                @Override
                                                public void call(Output output) {
//                                showSnackBar(""+output.getCo.de());
                                                }
                                            });
                                }
                            });
                            s2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                                    int commond=4;
                                    if(isChecked){
                                        commond=3;
                                    }
                                    else
                                        commond=4;
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
                                    int commond=6;
                                    if(isChecked){
                                        commond=5;
                                    }
                                    else
                                        commond=6;
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
                            s4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    int commond=10;
                                    if(isChecked){
                                        commond=9;
                                    }
                                    else
                                        commond=10;
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
                            s5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    int commond=12;
                                    if(isChecked){
                                        commond=11;
                                    }
                                    else
                                        commond=12;
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
                            s6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    int commond=8;
                                    if(isChecked){
                                        commond=7;
                                    }
                                    else
                                        commond=8;
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
                            s7.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    int commond=52;
                                    if(isChecked){
                                        commond=53;
                                    }
                                    else
                                        commond=52;
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
                            s8.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    boolean b=false;
                                    if(isChecked){
//                                        手动控制
                                        b=true;

                                    }
                                    else{
                                        b=false;
                                    }
                                    Net.getNet().control(b)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(new Action1<Output>() {
                                                @Override
                                                public void call(Output output) {
                                                    if (output.getCode() == 200){
                                                        Log.e("control",""+output.getCode());
                                                    }
                                                }
                                            }, new Action1<Throwable>() {
                                                @Override
                                                public void call(Throwable throwable) {
                                                    showSnackBar(throwable.getMessage());
                                                }
                                            });

                                }
                            });

                        }
                        else
                        {
                            fName.setText("未绑定关系");
                            showSnackBar("你还没有与family绑定关系");
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
//                        showSnackBar(throwable.getMessage());
                    }
                });

    }
    private void init2(){
        door.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Net.getNet().commond(51).subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.immediate())
                        .subscribe(new Action1<Output>() {
                            @Override
                            public void call(Output output) {
//                                showSnackBar(""+output.getCode());
                            }
                        });
            }
        });
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Net.getNet().commond(31).subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.immediate())
                        .subscribe(new Action1<Output>() {
                            @Override
                            public void call(Output output) {
//                                showSnackBar(""+output.getCode());
                            }
                        });
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Net.getNet().commond(32).subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.immediate())
                        .subscribe(new Action1<Output>() {
                            @Override
                            public void call(Output output) {
//                                showSnackBar(""+output.getCode());
                            }
                        });
            }
        });
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Net.getNet().commond(33).subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.immediate())
                        .subscribe(new Action1<Output>() {
                            @Override
                            public void call(Output output) {
//                                showSnackBar(""+output.getCode());
                            }
                        });
            }
        });
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Net.getNet().commond(34).subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.immediate())
                        .subscribe(new Action1<Output>() {
                            @Override
                            public void call(Output output) {
//                                showSnackBar(""+output.getCode());
                            }
                        });
            }
        });
        say.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Net.getNet().commond(35).subscribeOn(Schedulers.io())
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

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
