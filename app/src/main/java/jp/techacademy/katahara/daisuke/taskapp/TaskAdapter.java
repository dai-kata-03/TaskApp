package jp.techacademy.katahara.daisuke.taskapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Daisuke on 2017/04/19.
 */

public class TaskAdapter extends BaseAdapter {

    private LayoutInflater mLayoutInflater;
    /** 他のxmlリソースのViewを取り扱うための仕組みであるLayoutInflateをメンバ変数で定義。動的にLayout.xmlファイルをセットすることができる。
     * セットしてないlayout.xmlの中の要素(TextViewやButton)を書き換えたりもできる。
     * とくになにもせずAndroidのListViewを表示したときは、単一のテキストまたはボタンなどが行になって表示されます。
     * 色々な要素をリスト表示したいときには、たとえば、Webサイトの場合だとループの中にHTMLを書くだけで済むのですが、Androidの場合は、LayoutInflaterを利用してListViewの中に動的にxmlを差し込んで表示したりする必要があります。
     */

    /**
     * リストをスクロールして、新しい行が表示されるタイミングでgetViewが実行されます。
     * レイアウトが初めて作成される場合のみLayoutInflaterを実行します。それ以外は、表示されなくなったViewが引数に渡されてくるので再利用します。
     */



    private List<Task> mTaskList; // リストの定義（メンバ変数）。

    public TaskAdapter(Context context) {

        // xmlで定義したレイアウトを取得
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setTaskList(List<Task> taskList) {
        mTaskList = taskList;
    }

    @Override
    public int getCount() {
        return mTaskList.size();
    }

    @Override
    public Object getItem(int position) {
        return mTaskList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mTaskList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // レイアウトが初めて作成される場合のみ作成
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(android.R.layout.simple_list_item_2, null);
        }

        TextView textView1 = (TextView) convertView.findViewById(android.R.id.text1);
        TextView textView2 = (TextView) convertView.findViewById(android.R.id.text2);

        textView1.setText(mTaskList.get(position).getTitle());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.JAPANESE);
        Date date = mTaskList.get(position).getDate();
        textView2.setText(simpleDateFormat.format(date));

        return convertView;
    }
}
