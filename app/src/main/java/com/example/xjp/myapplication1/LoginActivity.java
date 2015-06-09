package com.example.xjp.myapplication1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.xjp.myapplication1.com.example.xjp.myapplication1.service.ServiceRulesException;
import com.example.xjp.myapplication1.com.example.xjp.myapplication1.service.ServiceRulesException2;
import com.example.xjp.myapplication1.com.example.xjp.myapplication1.service.UserService;
import com.example.xjp.myapplication1.com.example.xjp.myapplication1.service.UserServiceImplement;

import java.lang.ref.WeakReference;

/**
 * Created by XJP on 15/3/13.
 */


public  class LoginActivity extends Activity {

    private IHandler handler = new IHandler(this);
    ProgressBar progressBar4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏actionbar
        setContentView(R.layout.activity_login);
        final EditText et1=(EditText)findViewById(R.id.editText);
        final EditText et2=(EditText)findViewById(R.id.editText2);
        final Button bt3=(Button)findViewById(R.id.button3);
        progressBar4=(ProgressBar)findViewById(R.id.progressBar4);
        final UserService userService=new UserServiceImplement();
        final SharedPreferences sharedPreferences = getSharedPreferences("flag", Context.MODE_PRIVATE); //私有数据
        //设置按下效果
        bt3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction()==MotionEvent.ACTION_DOWN)
                    bt3.setBackgroundResource(R.drawable.button_shape2);
                else if (motionEvent.getAction()==MotionEvent.ACTION_UP)
                    bt3.setBackgroundResource(R.drawable.button_shape);
                return false;
            }
        });

        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar4.setVisibility(View.VISIBLE);
                InputMethodManager inputMethodManager =(InputMethodManager)LoginActivity.this.getApplicationContext().
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(et1.getWindowToken(), 0); //隐藏键盘

//                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);//隐藏软键盘
                final String UserName=et1.getText().toString();
                final String UserPassword=et2.getText().toString();
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("username",UserName);
                editor.commit();
//副线程
                final Thread thread=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            userService.userlogin(UserName,UserPassword);//数据传到userlogin进行判断
                            handler.sendEmptyMessage(1);
                            SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
                            editor.putBoolean("loginflag", true);
                            editor.commit();//提交修改
                            
                        }
                        catch (ServiceRulesException e){
                            handler.sendEmptyMessage(2);
                        }
                        catch (ServiceRulesException2 e){
                            handler.sendEmptyMessage(3);
                        }
                        catch (Exception e){
                            e.printStackTrace();
                            handler.sendEmptyMessage(0);
                        }

                    }
                });
                thread.start();

            }
        });

    }

//    private static Handler handler=new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            int flag=msg.what;
//            switch (flag){
//                case 0:
//                    Toast.makeText(, "欢迎使用梦想旅游！", Toast.LENGTH_SHORT).show();
//
//            }
//        }
//    };
    private void jump(){
        MainActivity.mainActivity.finish();//销毁MainActivity
        Intent intent=new Intent();
        intent.setClass(LoginActivity.this,MainActivity.class);
        startActivity(intent);
    }

    private void showTip(String str){
            Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
//本可使用Handler，这样弱引用LoginActivity可以使用其方法
    private static class IHandler extends Handler {
    private final WeakReference<Activity> mActivity;
    public IHandler(LoginActivity activity) {
        mActivity = new WeakReference<Activity>(activity);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        int flag = msg.what;
        switch (flag) {
            case 0:
                ((LoginActivity) mActivity.get()).showTip("登录失败");
                ((LoginActivity) mActivity.get()).progressBar4.setVisibility(View.INVISIBLE);

                break;
            case 1:
                ((LoginActivity) mActivity.get()).showTip("登录成功");
                ((LoginActivity) mActivity.get()).jump();
                ((LoginActivity) mActivity.get()).progressBar4.setVisibility(View.INVISIBLE);
                break;
            case 2:
                ((LoginActivity) mActivity.get()).showTip("用户名不存在");
                ((LoginActivity) mActivity.get()).progressBar4.setVisibility(View.INVISIBLE);
                break;
            case 3:
                ((LoginActivity)mActivity.get()).showTip("密码错误");
                ((LoginActivity) mActivity.get()).progressBar4.setVisibility(View.INVISIBLE);
                break;
            default:
                break;
        }

    }


}



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
