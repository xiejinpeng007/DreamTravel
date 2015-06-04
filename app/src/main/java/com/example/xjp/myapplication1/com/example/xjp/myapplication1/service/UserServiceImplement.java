package com.example.xjp.myapplication1.com.example.xjp.myapplication1.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 * Created by XJP on 15/3/24.
 */
public class UserServiceImplement implements UserService {
    public final static String TAG="UserServiceImplement";
    @Override
    public void userlogin(String UserName, String UserPassword) throws Exception {

        String result = null;
        HttpClient client = new DefaultHttpClient();
        String uri = "http://mxly.wicp.net/DreamtimeTravel/servlet/ClientLogin?UserName="
                + UserName + "&UserPassword="+ UserPassword;
        HttpGet get = new HttpGet(uri);
        HttpResponse response = client.execute(get);
        int statuscode = response.getStatusLine().getStatusCode();
        if (statuscode == HttpStatus.SC_OK) {
            result = EntityUtils.toString(response.getEntity(), "UTF-8");

            if(result.equals("1")){

            }
            else if(result.equals("0")){
                throw new ServiceRulesException("用户名不存在");
            }
            else {
                throw new ServiceRulesException2("密码错误");

            }
        }

//        本地判断
//        if((username.equals("xjp"))&&(password.equals("123")))
//        {}
//        else{
//            throw new ServiceRulesException("用户名或密码错误");
//        }

    }
}
