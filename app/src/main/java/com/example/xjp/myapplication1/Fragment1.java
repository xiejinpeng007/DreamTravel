package com.example.xjp.myapplication1;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


import java.util.ArrayList;

/**
 * Created by XJP on 15/3/12.
 */

public class Fragment1 extends Fragment {
    View view;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment1, container, false);

        ImageButton im=(ImageButton)view.findViewById(R.id.imageButton);
        ImageButton im2=(ImageButton)view.findViewById(R.id.imageButton2);
        ImageButton im3=(ImageButton)view.findViewById(R.id.imageButton3);
        ImageButton im4=(ImageButton)view.findViewById(R.id.imageButton4);
        ImageButton im5=(ImageButton)view.findViewById(R.id.imageButton5);
        ImageButton im6=(ImageButton)view.findViewById(R.id.imageButton6);
        ImageButton im7=(ImageButton)view.findViewById(R.id.imageButton7);
        ImageButton im8=(ImageButton)view.findViewById(R.id.imageButton8);
        ImageButton im9=(ImageButton)view.findViewById(R.id.imageButton9);

        im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(getActivity(),F1Activity.class);
                startActivity(intent);
            }
        });
        im2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(getActivity(),F2Activity.class);
                startActivity(intent);
            }
        });
        im3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(getActivity(),F3Activity.class);
                startActivity(intent);
            }
        });
        im4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(getActivity(),F4Activity.class);
                startActivity(intent);
            }
        });
        im5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(getActivity(),F5Activity.class);
                startActivity(intent);
            }
        });
        im6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(getActivity(),F6Activity.class);
                startActivity(intent);
            }
        });
        im7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(getActivity(),F7Activity.class);
                startActivity(intent);
            }
        });
        im8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(getActivity(),F8Activity.class);
                startActivity(intent);
            }
        });
        im9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(getActivity(),F9Activity.class);
                startActivity(intent);
            }
        });

        return view;}
}