/*
 * @Project: GZJK
 * @Author: BMR
 * @Date: 2015年8月4日
 * @Copyright: 2000-2015 CMCC . All rights reserved.
 */
package com.ranlib.customfancycoverflow;

import com.ran.tools.DisplayUtil;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import at.technikum.mti.fancycoverflow.R;


/**
 * @author BMR
 * @ClassName: PlanCoverFlowAdapter
 * @Description: 滑动日期控件数据adapter
 * @date 2015年8月4日 上午9:39:53
 */
public class CustomPlanCoverFlowAdapter extends CustomFancyCoverFlowAdapter {
    private Context context;
    private int totalDays; // 总天数
    private int currentDay;// 已参与天数
    private int selectedDay;// 当前选中天数
    private int size = 0;// px,图片大小

    // =============================================================================
    // Private members
    // =============================================================================

    // =============================================================================
    // Constructor
    // =============================================================================
    public CustomPlanCoverFlowAdapter(Context context) {
        this.context = context;
        this.size = DisplayUtil.dip2px(context, 63);
    }

    public void update(int totalDays, int currentDay) {
        this.totalDays = totalDays;
        this.currentDay = currentDay - 1;
        this.selectedDay = this.currentDay;

        notifyDataSetChanged();
    }


    // =============================================================================
    // Supertype overrides
    // =============================================================================

    @Override
    public int getCount() {
        return Math.min(Math.max(0, currentDay + 3), totalDays);
    }

    @Override
    public Object getItem(int position)  {
        return position;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getCoverFlowItem(int i, View reuseableView, ViewGroup viewGroup) {
        PlanViewGroup customViewGroup = null;
        int sizeImg = 84;
        if (reuseableView != null) {
            customViewGroup = (PlanViewGroup) reuseableView;
        } else {
            customViewGroup = new PlanViewGroup(viewGroup.getContext());
            customViewGroup.setLayoutParams(new CustomFancyCoverFlow.LayoutParams(size, size));
        }
        customViewGroup.setBackgroundColor(Color.WHITE);
        if (i <= currentDay) {
            customViewGroup.getImageView().setImageResource(R.drawable.icon_plan_bg_his);
        } else {
            customViewGroup.getImageView().setImageResource(R.drawable.icon_plan_bg_fu);
        }
        if (i == selectedDay) {
            customViewGroup.getTextView().setText("第" + (i + 1) + "天");
            customViewGroup.getImageView().setImageResource(R.drawable.icon_plan_bg_curr);

            customViewGroup.setLayoutParams(new Gallery.LayoutParams(DisplayUtil.dip2px(context, sizeImg), DisplayUtil.dip2px
                    (context, sizeImg)));
            customViewGroup.getImageView().setAdjustViewBounds(true);

            // 调整当前天数的位置，防止背景带阴影的图片时字体偏下
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT);
            params.addRule(RelativeLayout.CENTER_HORIZONTAL);
            params.setMargins(0, (size - DisplayUtil.dip2px(context, sizeImg))/2, 0, 0);
            customViewGroup.getTextView().setLayoutParams(params);
        } else {
            customViewGroup.getTextView().setText(String.format("%d", i + 1));
        }
        return customViewGroup;
    }

    @Override
    public boolean getCoverFlowItemReflection(int position) {
        // if (position == selectedDay)
        // {
        // return true;
        // }
        return false;
    }

    public void setSelectedDay(int selectedDay) {
        this.selectedDay = selectedDay;
    }

    public int getSizeDip() {
        return size;
    }

    public void setSizeDip(int size) {
        this.size = DisplayUtil.dip2px(context, size);
    }

}

class PlanViewGroup extends RelativeLayout {

    // =============================================================================
    // Child views
    // =============================================================================

    private TextView textView;

    private ImageView imageView;

    private boolean isReflection = false;

    // =============================================================================
    // Constructor
    // =============================================================================

    public PlanViewGroup(Context context) {
        super(context);

        this.textView = new TextView(context);
        this.imageView = new ImageView(context);
        this.setGravity(Gravity.CENTER);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        this.textView.setLayoutParams(layoutParams);
        this.imageView.setLayoutParams(layoutParams);

        this.textView.setGravity(Gravity.CENTER);
        this.textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        this.textView.setTextColor(Color.WHITE);

        this.imageView.setAdjustViewBounds(false);
        this.imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        this.imageView.setVisibility(View.VISIBLE);

        this.addView(this.imageView);
        this.addView(this.textView);
    }

    // =============================================================================
    // Getters
    // =============================================================================
    public TextView getTextView() {
        return textView;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public boolean isReflection() {
        return isReflection;
    }

    public void setReflection(boolean isReflection) {
        this.isReflection = isReflection;
    }

}
