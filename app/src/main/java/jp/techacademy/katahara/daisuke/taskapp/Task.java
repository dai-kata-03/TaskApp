package jp.techacademy.katahara.daisuke.taskapp;

import java.io.Serializable;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Daisuke on 2017/04/19.
 * これがデータベースファイルとなる。つまり、ここにデータを保持していく。
 */

public class Task extends RealmObject implements Serializable {
    private String title; // タイトル
    private String contents; // 内容
    private Date date; // 日時

    // idをプライマリーキーとして設定
    @PrimaryKey
    private int id;
    private String category; // カテゴリ追加・課題用

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title =  title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // 課題用・カテゴリ追加
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
