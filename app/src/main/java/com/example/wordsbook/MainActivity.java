package com.example.wordsbook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class MainActivity extends Activity {

    private EditText word;
    private EditText wordMean;
    private EditText input1;
    private EditText input2;
    private EditText input3;
    private Button searchBut;
    private Button insertBut;
    private Button deleteBut;
    private Button updateBut;
    private EditText input;
    private Button help;
    private ListView listView;
    private MyDataBaseHelper dbHelper;
    private SQLiteDatabase sqlDB;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        word = (EditText) findViewById(R.id.editText1);
        wordMean = (EditText) findViewById(R.id.editText2);
        input = (EditText) findViewById(R.id.editText3);
        input1 = (EditText) findViewById(R.id.editText4);
        input2 = (EditText) findViewById(R.id.editText5);
        input3 = (EditText)findViewById(R.id.editText6);
        insertBut = (Button) findViewById(R.id.button1);
        searchBut = (Button) findViewById(R.id.button2);
        deleteBut = (Button) findViewById(R.id.button3);
        updateBut = (Button) findViewById(R.id.button4);
        listView = (ListView) findViewById(R.id.listView1);
        help = (Button) findViewById(R.id.help);
        dbHelper = new MyDataBaseHelper(MainActivity.this, "myDict.db3", 1);

        insertBut.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                insertMethod();
            }
        });
        searchBut.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                searchMethod();
            }
        });
        deleteBut.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteMethod();
            }
        });
        updateBut.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                updateMethod();
            }
        });

        help.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"帮助",Toast.LENGTH_LONG).show();
            }
        });
    }

    // 插入单词
    protected void insertMethod() {
        String strWord = word.getText().toString();
        String strMean = wordMean.getText().toString();
        // 插入生词记录
        sqlDB = dbHelper.getReadableDatabase();
        sqlDB.execSQL("INSERT INTO dict VALUES(NULL,?,?)", new String[] {
                strWord, strMean });

        Toast.makeText(getApplicationContext(), "插入成功", Toast.LENGTH_LONG)
                .show();
    }


    //删除单词
    protected void deleteMethod(){
        String word1 = input1.getText().toString();
        sqlDB = dbHelper.getReadableDatabase();
        sqlDB.execSQL("delete from dict where word = ?",new String[]{word1});
        Toast.makeText(getApplicationContext(),"删除成功",Toast.LENGTH_LONG).show();
    }


    //更改单词
    protected  void updateMethod(){
        String word2 = input2.getText().toString();
        String word3 = input3.getText().toString();
        sqlDB = dbHelper.getReadableDatabase();
        sqlDB.execSQL("update dict set detail = ? where word = ?",new String[] {word3,word2});
        Toast.makeText(getApplicationContext(),"更改成功",Toast.LENGTH_LONG).show();
    }

    // 查找单词
    protected void searchMethod() {
        String key = input.getText().toString();
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery(
                "SELECT * FROM dict WHERE word LIKE ? OR detail LIKE ?",
                new String[] { "%" + key + "%", "%" + key + "%" });
        ArrayList<Map<String, String>> result = new ArrayList<Map<String, String>>();
        while (cursor.moveToNext()) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("word", cursor.getString(1));
            map.put("detail", cursor.getString(2));
            result.add(map);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(
                getApplicationContext(), result, R.layout.line, new String[] {
                "word", "detail" }, new int[] { R.id.textView1,
                R.id.textView2 });
        listView.setAdapter(simpleAdapter);
    }

 /*   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }*/
}