package com.example.fleeting;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.fleeting.db.dbcontrol;
import com.example.fleeting.db.schedule;
import com.example.fleeting.utils.RecyclerItemView;
import com.example.fleeting.utils.RecyclerUtils;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter
        extends RecyclerView.Adapter<RecyclerViewAdapter.NormalViewHolder>
        implements RecyclerItemView.onSlidingButtonListener{

    private List<schedule> schedules=new ArrayList<>();
    private onSlidingViewClickListener onSvcl;
    private RecyclerItemView recyclers;
    private LayoutInflater mLayoutInflater;
    private Context mContext;

    //通过构造方法将以及文字，上下文传递过去
    public RecyclerViewAdapter(Context context,List<schedule> schedules){
        this.mContext = context;
        this.schedules = schedules;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void notifyDataSetChanged(List<schedule> Infos) {
        this.schedules = Infos;
        super.notifyDataSetChanged();
    }

    //我们创建一个ViewHolder并返回，ViewHolder必须有一个带有View的构造函数，这个View就是我们Item的根布局，在这里我们使用自定义Item的布局；
    public NormalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NormalViewHolder(mLayoutInflater.inflate(R.layout.schedule,parent,false));
    }

    //将数据与界面进行绑定的操作

    public void onBindViewHolder(final NormalViewHolder holder, final int position) {
            schedule temp=schedules.get(position);
            holder.titleTextView.setText(temp.getTag());
            holder.timeTextView.setText(temp.getRemindline().substring(10));
            holder.msgTextView.setText(temp.getContent());

            if (temp.getFlag().equals("紧急")) {
                holder.colorTextView.setBackgroundColor(Color.parseColor("#ff4040"));
            } else if (temp.getFlag().equals("一般")) {
                holder.colorTextView.setBackgroundColor(Color.parseColor("#ffd700"));
            } else {
                holder.colorTextView.setBackgroundColor(Color.parseColor("#c1ffc1"));
            }
            holder.layout_left.getLayoutParams().width = RecyclerUtils.getScreenWidth(mContext);

            holder.layout_left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //判断是否有删除菜单打开
                    if (menuIsOpen()) {
                        closeMenu();//关闭菜单
                    } else {
                        //获得布局下标（点的哪一个）
                        int subscript = holder.getLayoutPosition();
                        schedule temp = schedules.get(subscript);
                        dbcontrol.delete(temp);
                        onSvcl.onItemClick(view, subscript);
                    }
                }
            });
            holder.other.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    schedule temp=schedules.get(position);
                    temp.setDone();
                    schedules.remove(position);
                    notifyDataSetChanged(schedules);
                    temp.save();
                }
            });
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int subscript;
                    subscript = holder.getLayoutPosition();
                    onSvcl.onDeleteBtnCilck(view, subscript);
                }
            });
            //因为没有item给我设置点击事件了，所以只能点击标题跳转到该日程的详细信息，为了搜索功能的结果显示而设置的
            holder.timeTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "跳转到该日程详细信息" + position, Toast.LENGTH_SHORT).show();
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    schedule temp = schedules.get(position);
                }
            });

    }


    //获取数据的数量
    @Override
    public int getItemCount() {
        return  schedules.size();
    }

    @Override
    public void onMenuIsOpen(View view) {
        recyclers = (RecyclerItemView) view;
    }

    @Override
    public void onDownOrMove(RecyclerItemView recycler) {
        if(menuIsOpen()){
            if(recyclers != recycler){
                closeMenu();
            }
        }
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public   class NormalViewHolder extends RecyclerView.ViewHolder{
        public TextView colorTextView;
        public TextView titleTextView;
        public TextView msgTextView;
        public TextView timeTextView;
        public TextView other;
        public TextView delete;
        public LinearLayout layout_left;
        public NormalViewHolder(View itemView) {
            super(itemView);
            colorTextView=itemView.findViewById(R.id.color);
            titleTextView=itemView.findViewById(R.id.title);
            msgTextView=itemView.findViewById(R.id.msg);
            timeTextView=itemView.findViewById(R.id.time);
            other =  itemView.findViewById(R.id.other);
            delete =  itemView.findViewById(R.id.delete);
            layout_left =  itemView.findViewById(R.id.layout_left);

            ((RecyclerItemView)itemView).setSlidingButtonListener(RecyclerViewAdapter.this);
        }
    }
    //删除数据
    public void removeData(int position){
        schedule temp=schedules.get(position);
        schedules.remove(position);
        dbcontrol.delete(temp);
//        notifyDataSetChanged();
        notifyItemRemoved(position);
    }

    //关闭菜单
    public void closeMenu() {
        recyclers.closeMenu();
        recyclers = null;

    }

    // 判断是否有菜单打开
    public Boolean menuIsOpen() {
        if(recyclers != null){
            return true;
        }
        return false;
    }

    //设置在滑动侦听器上
    public void setOnSlidListener(onSlidingViewClickListener listener) {
        onSvcl = listener;
    }

    // 在滑动视图上单击侦听器
    public interface onSlidingViewClickListener {
        void onItemClick(View view, int position);
        void onDeleteBtnCilck(View view, int position);
    }
}
