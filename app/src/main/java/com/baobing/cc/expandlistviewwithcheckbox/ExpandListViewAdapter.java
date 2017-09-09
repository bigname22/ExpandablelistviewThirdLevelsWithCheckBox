package com.baobing.cc.expandlistviewwithcheckbox;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

/**
 * author:Created by LiangSJ
 * date: 2017/9/7.
 * description:？
 */

public class ExpandListViewAdapter extends BaseExpandableListAdapter {
    private List<FirstModel> mListData;
    private LayoutInflater mInflate;
    private Context context;

    public ExpandListViewAdapter(List<FirstModel> mListData, Context context) {
        this.mListData = mListData;
        this.context = context;
        this.mInflate = LayoutInflater.from(context);
    }

    @Override
    public int getGroupCount() {
        return mListData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mListData.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mListData.get(groupPosition).getListSecondModel().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        FirstHolder holder = null;
        if (convertView == null) {
            holder = new FirstHolder();
            convertView = mInflate.inflate(R.layout.item_expand_lv_first, parent, false);
            holder.tv = ((TextView) convertView.findViewById(R.id.tv));
            holder.cb = ((CheckBox) convertView.findViewById(R.id.cb));
            convertView.setTag(holder);
        } else {
            holder = (FirstHolder) convertView.getTag();
        }
        holder.tv.setText(mListData.get(groupPosition).getTitle());
        final FirstHolder finalHolder = holder;
        finalHolder.cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = finalHolder.cb.isChecked();
                Log.d("bigname", "onclick: first:" + groupPosition + "," + isChecked);
                mListData.get(groupPosition).setCheck(isChecked);
                for (int i = 0; i < mListData.get(groupPosition).getListSecondModel().size(); i++) {
                    SecondModel secondModel = mListData.get(groupPosition).getListSecondModel().get(i);
                    secondModel.setCheck(isChecked);
                    for (int j = 0; j < secondModel.getListThirdModel().size(); j++) {
                        ThirdModel thirdModel = secondModel.getListThirdModel().get(j);
                        thirdModel.setCheck(isChecked);
                    }
                }
                notifyDataSetChanged();
            }
        });
        finalHolder.cb.setChecked(mListData.get(groupPosition).isCheck());
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
//        SecondHolder holder = null;
//        if (convertView == null) {
//            holder = new SecondHolder();
//            convertView = mInflate.inflate(R.layout.item_expand_lv_second, parent, false);
//            holder.tv = ((TextView) convertView.findViewById(R.id.tv));
//            holder.cb = ((CheckBox) convertView.findViewById(R.id.cb));
//            convertView.setTag(holder);
//        } else {
//            holder = (SecondHolder) convertView.getTag();
//        }
//        holder.tv.setText(mListData.get(groupPosition).getListSecondModel().get(childPosition).getTitle());
//        holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                mListData.get(groupPosition).getListSecondModel().get(childPosition).setCheck(isChecked);
//            }
//        });
//        holder.cb.setChecked(mListData.get(groupPosition).getListSecondModel().get(childPosition).isCheck());
//        return convertView;
//        Object object= (Object) getChild(groupPosition, childPosition);
//        CustomExpandableListView subObjects= () convertView;;
//        if (convertView==null) {
//            subObjects= new CustomExpandableListView(activity);
//        }
//        Adapter2 adapter= new Adapter2(activity, object);
//        subObjects.setAdapter(adapter);
//
//        return subObjects
        CustomExpandableListView lv = ((CustomExpandableListView) convertView);
        if (convertView == null) {
            lv = new CustomExpandableListView(context);
        }
        SecondAdapter secondAdapter = new SecondAdapter(context, mListData.get(groupPosition).getListSecondModel());
        lv.setAdapter(secondAdapter);
        return lv;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
    /*
  *   第二层的适配器
  * */
    class SecondAdapter extends BaseExpandableListAdapter {
        Context context;
        LayoutInflater inflater;
        List<SecondModel> listSecondModel;

        public SecondAdapter(Context context,List<SecondModel> listSecondModel) {
            this.context = context;
            this.listSecondModel = listSecondModel;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getGroupCount() {
            int size = listSecondModel.size();
            Log.d("bigname", "getGroupCount: "+size);
            return size;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return listSecondModel.get(groupPosition).getListThirdModel().size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return listSecondModel.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return listSecondModel.get(groupPosition).getListThirdModel().get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            SecondHolder holder = null;
            if (convertView == null) {
                holder = new SecondHolder();
                convertView = mInflate.inflate(R.layout.item_expand_lv_second, parent, false);
                holder.tv = ((TextView) convertView.findViewById(R.id.tv));
                holder.cb = ((CheckBox) convertView.findViewById(R.id.cb));
                convertView.setTag(holder);
            } else {
                holder = (SecondHolder) convertView.getTag();
            }
            holder.tv.setText(listSecondModel.get(groupPosition).getTitle());
            final SecondHolder secondHolder = holder;
            secondHolder.cb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isChecked = secondHolder.cb.isChecked();
                    Log.d("bigname", "onCheckedChanged: second:" + groupPosition + "," + isChecked);
                    listSecondModel.get(groupPosition).setCheck(isChecked);
                    for (int i = 0; i < listSecondModel.get(groupPosition).getListThirdModel().size(); i++) {
                        ThirdModel thirdModel = listSecondModel.get(groupPosition).getListThirdModel().get(i);
                        thirdModel.setCheck(isChecked);
                    }
                    notifyDataSetChanged();
                }
            });
            secondHolder.cb.setChecked(listSecondModel.get(groupPosition).isCheck());
            return convertView;
        }

        @Override
        public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            ThirdHolder holder = null;
            if (convertView == null) {
                holder = new ThirdHolder();
                convertView = mInflate.inflate(R.layout.item_expand_lv_third, parent, false);
                holder.tv = ((TextView) convertView.findViewById(R.id.tv));
                holder.cb = ((CheckBox) convertView.findViewById(R.id.cb));
                convertView.setTag(holder);
            } else {
                holder = (ThirdHolder) convertView.getTag();
            }
            holder.tv.setText(listSecondModel.get(groupPosition).getListThirdModel().get(childPosition).getTitle());
            final ThirdHolder thirdHolder = holder;
            thirdHolder.cb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isChecked = thirdHolder.cb.isChecked();
                    Log.d("bigname", "onCheckedChanged: third:" + groupPosition + "," + isChecked);
                    listSecondModel.get(groupPosition).getListThirdModel().get(childPosition).setCheck(isChecked);
                }
            });
            thirdHolder.cb.setChecked(listSecondModel.get(groupPosition).getListThirdModel().get(childPosition).isCheck());
            return convertView;
        }


        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }


    class FirstHolder {
        TextView tv;
        CheckBox cb;
    }

    class SecondHolder {
        TextView tv;
        CheckBox cb;
    }

    class ThirdHolder{
        TextView tv;
        CheckBox cb;
    }
}
