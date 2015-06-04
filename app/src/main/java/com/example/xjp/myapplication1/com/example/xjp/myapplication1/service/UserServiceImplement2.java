package com.example.xjp.myapplication1.com.example.xjp.myapplication1.service;

import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 * Created by XJP on 15/3/24.
 */
public class UserServiceImplement2 implements UserService2 {
    @Override
    public void userregister(String UserName, String UserPassword,String UserSex,String UserPhone,String UserTrueName) throws Exception {

        String result = null;
        HttpClient client = new DefaultHttpClient();

        String uri = "http://mxly.wicp.net/DreamtimeTravel/servlet/ClientRegister?UserName="
                + UserName+ "&UserPassword="+ UserPassword+ "&UserSex="+ UserSex
                + "&UserPhone="+ UserSex+ "&UserTrueName="+ UserTrueName;
        HttpPost post = new HttpPost(uri);
        HttpResponse response = client.execute(post);
        int statuscode = response.getStatusLine().getStatusCode();
        if (statuscode == HttpStatus.SC_OK) {
            result = EntityUtils.toString(response.getEntity(), "UTF-8");

            if(result.equals("0")){//注册成功

            }
            else if(result.equals("1")){
                throw new ServiceRulesException("注册失败");//注册失败
            }
            else {
                throw new ServiceRulesException2("用户名存在");//用户名存在

            }
        }



    }
}
