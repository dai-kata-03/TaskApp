package jp.techacademy.katahara.daisuke.taskapp;

import android.app.Application;

import io.realm.Realm;

/**
 * Created by Daisuke on 2017/04/19.
 */

public class TaskApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this); // Realm.init(this);をしてRealmを初期化します。特別な設定を行わずデフォルトの設定を使う場合はこのように記述します。
    }
}
