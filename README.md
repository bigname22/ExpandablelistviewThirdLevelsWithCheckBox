##ExpandableListView三级列表实现(带CheckBox全选功能)
[csdn文章链接](http://blog.csdn.net/bigname22/article/details/77919025 "csdn文章链接")

场景：多选车辆，并且在同一个页面实现。
*（数据结构：公司-线路-车辆三层）*

那么第一时间想到的只能是 **ExpandableListView** 这个神奇的组件了，但常规就两层，那要怎么办？ExpandableListView嵌套ExpandableListView呗！

然后翻阅了一下网上的案例。嗯嗯，很好，案例还算多，总结使用时最关键的两点是：

1.第一个ExpandableListView中的getChildCount()要返回 1；因为这个1是用来装第二个ExpandableListView的。
2.第一个ExpandableListView中的getChildView()要返回ExpandableListView.

但网上没有结合三级结合checkbox的案例，所以又的多动下脑子了。

继续讲，第一个ExpandableListView的GroupItem对应的数据是公司，ChildItem是第二个ExpandableListView，他的GroupItem对应的数据是线路，ChildItem是车辆。也就是说，第一个关心的是公司，第二个关心的是线路、车辆。

效果图打头阵了：
![这里写图片描述](http://img.blog.csdn.net/20170909232239057?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvYmlnbmFtZTIy/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

下面贴我的代码：
首先设计数据源：FirstModel，SecondModel，ThirdModel。分别代表第一、二、三层数据，上级当然也是包含下级的。（demo在下方贴）
接下来主要说最最主要的适配器：

```

/**
 * author:Created by LiangSJ
 * date: 2017/9/7.
 * description:牛逼的适配器
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
	    //关键的一步，别侧漏
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
	    //正常流程，第一层的逻辑
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
        /**
	        checkbox关键点，第一层checkbox的选中状态监听，
	        如果勾选中，则把其下的第二层以及第三层都选上，否则相反。
        */
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
        /**通过isCheck属性控制checkbox
        的选中状态，2，3层同理*/
     finalHolder.cb.setChecked(mListData.get(groupPosition).isCheck());
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		 //关键的第二步，返回第二个expandablelistview
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
	 *   第二层的适配器，这里更是重点，圈起来，要考！
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
	        //第二层逻辑
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
            /**
			*  checkbox关键点，第一层checkbox的选中状态监听，
	        *  如果勾选中，则把其下的第三层都选上，否则相反。
			*/
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

```
实现三级联动是很简单的，但是再加入checkbox的时候有一点小问题出现。我考虑用数据源中的isCheck的布尔值属性取专门控制checkbox的状态，**①***然后当选中checkbox时，要把其对应的第二、三层的数据源的isCheck属性设置为同样的状态，然后执行notifydatachange()就可以了*。
这里有个问题就是：你得监听到checkbox的状态改变，然后才能执行①中的操作。起初我使用了的监听手法时checkbox.setOnCheckChangeListener()，这会导致一些问题，像回收机制出现时都会被监到，从而①中的代码又被不按套路的执行一遍。所以后来使用onClickListener()来监听勾选状态的改变，问题也就迎刃而解。
再总结，无论是三级还是二级、一级的列表，都可以使用在数据源中利用boolean属性专门控制其勾选状态；三级和二级要实现上下级勾选联动的功能思路也无非就是改变数据源中的boolean属性再重绘列表。

注意：checkbox会强焦点导致列表无法正常展开，把focus设为false就好了。
