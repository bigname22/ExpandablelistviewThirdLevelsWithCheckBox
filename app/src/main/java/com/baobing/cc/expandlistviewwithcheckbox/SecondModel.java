package com.baobing.cc.expandlistviewwithcheckbox;

import java.util.List;

/**
 * author:Created by LiangSJ
 * date: 2017/9/7.
 * description:？
 */

public class SecondModel {
    private boolean isCheck;
    private String title;
    private List<ThirdModel> listThirdModel;

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ThirdModel> getListThirdModel() {
        return listThirdModel;
    }

    public void setListThirdModel(List<ThirdModel> listThirdModel) {
        this.listThirdModel = listThirdModel;
    }

    public SecondModel(boolean isCheck, String title, List<ThirdModel> listThirdModel) {

        this.isCheck = isCheck;
        this.title = title;
        this.listThirdModel = listThirdModel;
    }

    public SecondModel() {
    }
}
