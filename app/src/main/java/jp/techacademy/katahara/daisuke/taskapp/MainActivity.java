package jp.techacademy.katahara.daisuke.taskapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private ListView mListView; // ListViewのメンバ変数。
    private TaskAdapter mTaskAdapter; // TaskAdapterのメンバ変数。

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // ListViewの設定
        mTaskAdapter = new TaskAdapter(MainActivity.this); // TaskAdapterのインスタンス。
        mListView = (ListView) findViewById(R.id.listView1);

        // ListViewをタップした時の処理
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 入力・編集する画面に遷移させる
            }
        });

        // ListViewを長押しした時の処理
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // タスクを削除する
                return true;
            }
        });

        reloadListView(); // ページのリロード（再読み込み）を行う。
    }

    private void reloadListView() {

        // 後でTaskクラスに変更する
        List<String> taskList = new ArrayList<String>(); // ArrayListクラスのインスタンス。
        taskList.add("aaa");
        taskList.add("bbb");
        taskList.add("ccc");

        mTaskAdapter.setTaskList(taskList);
        mListView.setAdapter(mTaskAdapter);
        mTaskAdapter.notifyDataSetChanged();
    }
}
