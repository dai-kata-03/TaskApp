package jp.techacademy.katahara.daisuke.taskapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.provider.Settings;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import io.realm.Realm;

/**
 * Created by Daisuke on 2017/04/21.
 * ブロードキャストとは特定のアプリに向けてIntentを発行するのでなく、システム全体に発行する仕組みです。
 * 受け取る側が「このIntentに反応する」というように設定しておき、そのブロードキャストを受け取ります。
 * そしてBroadcastReceiverはそのブロードキャストされた暗黙的Intentに応答するための仕組みです。
 * また、BroadcastReceiverは明示的Intentを受け取ることもできます。今回は自分のアプリに向かって投げると分かっているため、明示的Intentを使います。
 * BroadcastReceiverは極めてシンプルな構造で、Intentを受け取った時にonReceiveメソッドが呼び出されるだけです。
 * また、PendingIntentとはIntentの一種で、すぐに発行するのではなく特定のタイミングで後から発行させるIntentです。
 *
 * Serviceを起動するIntentの作成
 * スケジュールされた日時でブロードキャストするための、PendingIntentを作成
 * AlarmManagerに時刻を指定してPendingIntentを登録する
 *
 *
 *
 *
 * AndroidManifest.xmlに作成したTaskAlarmReceiverを<receiver android:name=".TaskAlarmReceiver"/>と定義します。
 * 画面スリープ中でもブロードキャストを受け取ることができるようにパーミッションを <uses-permission android:name="android.permission.WAKE_LOCK"/>と指定します。
 *
 * 通知はNotificationクラスを作成して、NotificationManagerにセットすることで表示することが出来ます。
 * アラームを受け取ったときに通知を出したいのでTaskAlarmReceiverに処理を実装します。NotificationはNotificationCompat.Builderクラスを使って作成します。
 *
 * メソッド名	内容
 * setSmallIcon	ステータスバーに表示されるアイコンのリソースを設定する。
 * setLargeIcon	通知に表示する大きなアイコンをBitmapで指定する。指定されていない場合はsetSmallIconメソッドで指定したリソースが使われる。
 * setWhen	いつ表示するか指定する。
 * setDefaults	通知時の音・バイブ・ライトについて指定する。
 * setAutoCancel	trueの場合はユーザがタップしたら通知が消える。falseの場合はコード上で消す必要がある。
 * setTicker	ステータスバーに流れる文字を指定する。5.0以降では表示されない。
 * setContentTitle	アイコンの横に太文字で表示される文字列を指定する。
 * setContentText	contentTitleの下に表示される文字列を指定する。
 * setContentIntent	ユーザが通知をタップしたときに起動するIntentを指定する。
 *
 *
 *
 */

public class TaskAlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        // 通知の設定を行う。
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.drawable.small_icon);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.large_icon));
        builder.setWhen(System.currentTimeMillis());

        // タップするとキャンセルされる
        builder.setAutoCancel(true);

        // EXTRA_TASKからTaskのidを取得して、idからTaskのインスタンスを取得する。
        int taskId = intent.getIntExtra(MainActivity.EXTRA_TASK, -1);
        Realm realm = Realm.getDefaultInstance();
        Task task = realm.where(Task.class).equalTo("id", taskId).findFirst();
        realm.close();

        // タスクの情報を設定する。
        builder.setTicker(task.getTitle()); // 5.0以降は表示されない。
        builder.setContentTitle(task.getTitle());
        builder.setContentText(task.getContents());

        // 通知をタップしたらアプリを起動する。
        Intent startAppIntent = new Intent(context, MainActivity.class);
        startAppIntent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,startAppIntent,0);
        builder.setContentIntent(pendingIntent);

        // 通知を表示する
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(task.getId(), builder.build());

    }
}
