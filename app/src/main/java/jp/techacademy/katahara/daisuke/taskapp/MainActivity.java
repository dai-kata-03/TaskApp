package jp.techacademy.katahara.daisuke.taskapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;


public class MainActivity extends AppCompatActivity {

    // タスクデータを保存しているファイル（クラス）を明示。Realm DBの宣言。
    public final static String EXTRA_TASK = "jp.techacademy.katahara.daisuke.taskapp.TASK";

    private Realm mRealm; // Realmのメンバ変数。

    // RealmChangeListenerクラスのmRealmListenerはRealmのデータベースに追加や削除など変化があった場合に呼ばれるリスナーです。
    private RealmChangeListener mRealmListener = new RealmChangeListener() {
      @Override
        public void onChange(Object element) {
            reloadListView();
        }
    };

    private ListView mListView; // ListViewのメンバ変数。
    private TaskAdapter mTaskAdapter; // TaskAdapterのメンバ変数。
    private EditText mCategoryFilter; // カテゴリ追加・課題用
    private String mFilter; // カテゴリ追加・課題用

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, InputActivity.class);
                startActivity(intent);
            }
        });

        // Realmの設定
        mRealm = Realm.getDefaultInstance(); // getDefaultInstanceメソッドでオブジェクトを取得。
        mRealm.addChangeListener(mRealmListener);

        // ListViewの設定
        mTaskAdapter = new TaskAdapter(MainActivity.this); // TaskAdapterのインスタンス。
        mListView = (ListView) findViewById(R.id.listView1);

        // カテゴリフィルタの設定。課題用。
        mCategoryFilter = (EditText) findViewById(R.id.category_filter_text);
        mCategoryFilter.addTextChangedListener(watchHandler);

        // ListViewをタップした時の処理
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 入力・編集する画面に遷移させる
                // parent.getAdapter()でListViewにセットされたAdapterが取得出来る
                Task task = (Task) parent.getAdapter().getItem(position);

                Intent intent = new Intent(MainActivity.this, InputActivity.class);
                intent.putExtra(EXTRA_TASK, task.getId());

                startActivity(intent);
            }
        });

        // ListViewを長押しした時の処理
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // タスクを削除する

                final Task task = (Task) parent.getAdapter().getItem(position);

                // ダイアログを表示する
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setTitle("削除");
                builder.setMessage(task.getTitle() + "を削除しますか");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        RealmResults<Task> results = mRealm.where(Task.class).equalTo("id", task.getId()).findAll();

                        mRealm.beginTransaction();
                        results.deleteAllFromRealm();
                        mRealm.commitTransaction();

                        // タスク削除時にアラームも一緒に削除する。

                        Intent resultIntent = new Intent(getApplicationContext(), TaskAlarmReceiver.class);
                        PendingIntent resultPendingIntent = PendingIntent.getBroadcast(
                                MainActivity.this,
                                task.getId(),
                                resultIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );

                        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                        alarmManager.cancel(resultPendingIntent);

                        reloadListView();

                    }
                });
                builder.setNegativeButton("CANCEL", null);

                AlertDialog dialog = builder.create();
                dialog.show();

                return true;
            }
        });

        reloadListView(); // ページのリロード（再読み込み）を行う。
    }

    // フィルタに入力された際にはフィルタ処理を走らせる。課題用。
    private TextWatcher watchHandler = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // フィルタ入力前の処理は今回はなし。
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // フィルタ入力中の処理は今回はなし。
        }

        @Override
        public void afterTextChanged(Editable s) {
            mFilter = mCategoryFilter.getText().toString();
            reloadListView(); // ページのリロード（再読み込み）を行う。
        }
    };


    private void reloadListView() {

        Log.d("DEBUG1", String.valueOf(mCategoryFilter));
        Log.d("DEBUG2", String.valueOf(mCategoryFilter.length()));

        // カテゴリフィルタの有無判断。課題用。

        // カテゴリフィルタなし
            if (mCategoryFilter.length() == 0) {
                // Realmデータベースから、「全てのデータを取得して新しい日時順に並べた結果」を取得。
                RealmResults<Task> taskRealmResults = mRealm.where(Task.class).findAllSorted("date", Sort.DESCENDING);
                // 上記の結果を、TaskListとしてセットする。
                mTaskAdapter.setTaskList(mRealm.copyFromRealm(taskRealmResults));
            } else {
                // カテゴリフィルタあり
                RealmResults<Task> taskRealmResults = mRealm.where(Task.class).equalTo("category", mFilter).findAllSorted("date", Sort.DESCENDING);
                // 上記の結果を、TaskListとしてセットする。
                mTaskAdapter.setTaskList(mRealm.copyFromRealm(taskRealmResults));
            }

        // TaskのListView用のアダプタに渡す。
        mListView.setAdapter(mTaskAdapter);
        // 表示を更新するために、アダプターにデータが変更されたことを知らせる。
        mTaskAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealm.close(); // getDefaultInstanceメソッドで取得したRealmクラスのオブジェクトはcloseメソッドで終了させる必要があります。
    }
}
