package com.example.xjp.myapplication1;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.xjp.myapplication1.DATABASE.DB;

import java.util.ArrayList;
import java.util.HashMap;


public class Favorite extends Activity {

    ListView listView;
    ArrayList<HashMap<String,Object>>list=new ArrayList<>();
    HashMap<String,Object>map=new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        listView=(ListView)findViewById(R.id.listView);

        final SharedPreferences sharedPreferences=getSharedPreferences("flag", Context.MODE_PRIVATE);
        String usrname=sharedPreferences.getString("username", "visitor");
        DB db=new DB(this);
        SQLiteDatabase dbRead=db.getReadableDatabase();
        Cursor cursor=dbRead.query("userfavorite", null, null, null, null, null, null);
        while (cursor.moveToNext()){
            String ScenicName=cursor.getString(cursor.getColumnIndex("ScenicName"));
            String ScenicPrice="价格:"+cursor.getString(cursor.getColumnIndex("ScenicPrice"));
            String ScenicAddress=cursor.getString(cursor.getColumnIndex("ScenicAddress"));
            String ScenicClass=cursor.getString(cursor.getColumnIndex("ScenicClass"));
            String date=cursor.getString(cursor.getColumnIndex("date"));
            switch (ScenicClass){
                case "weekend":ScenicClass="周末游";break;
                case "foreign":ScenicClass="出境游";break;
                case "ship":ScenicClass="邮轮游";break;
                case "secnic":ScenicClass="普通游";break;
            }
            map.put("ScenicName", ScenicName);
            map.put("ScenicPrice", ScenicPrice);
            map.put("ScenicClass", ScenicClass);
            map.put("date", date);
            list.add(map);
        }


        SimpleAdapter adapter=new SimpleAdapter (this,list, R.layout.listview_favorite,
                new String[]{"ScenicPrice","ScenicClass","date","ScenicName"},
                new int[]{R.id.textView86,R.id.textView87,R.id.textView88,R.id.textView89});
        listView.setAdapter(adapter);
        db.close();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_favorite, menu);
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
