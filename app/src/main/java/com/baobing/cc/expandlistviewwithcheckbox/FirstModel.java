package com.baobing.cc.expandlistviewwithcheckbox;

import java.util.List;

/**
 * author:Created by LiangSJ
 * date: 2017/9/7.
 * description:？
 */

public class FirstModel {
    private boolean isCheck;
    private String title;
    private List<SecondModel> listSecondModel;

    public FirstModel() {
    }

    public boolean isCheck() {

        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public List<SecondModel> getListSecondModel() {
        return listSecondModel;
    }

    public void setListSecondModel(List<SecondModel> listSecondModel) {
        this.listSecondModel = listSecondModel;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public FirstModel(boolean isCheck, String title, List<SecondModel> listSecondModel) {

        this.isCheck = isCheck;
        this.title = title;
        this.listSecondModel = listSecondModel;
    }
}
