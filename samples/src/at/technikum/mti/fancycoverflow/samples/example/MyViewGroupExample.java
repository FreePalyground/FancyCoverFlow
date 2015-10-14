/*
 * Copyright 2013 David Schreiber
 *           2013 John Paul Nalog
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package at.technikum.mti.fancycoverflow.samples.example;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import at.technikum.mti.fancycoverflow.FancyCoverFlow;
import at.technikum.mti.fancycoverflow.FancyCoverFlowAdapter;
import at.technikum.mti.fancycoverflow.samples.R;

import com.ran.tools.DisplayUtil;

public class MyViewGroupExample extends Activity
{
	private int totalDays = 23;
	private int currentDay = 20;
	private int currentPos;

	private TextView tvOne;
	private TextView tvTwo;
	private LinearLayout layout;

	// =============================================================================
	// Supertype overrides
	// =============================================================================

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.layout_example);

		final FancyCoverFlow fancyCoverFlow = (FancyCoverFlow) findViewById(R.id.fancyCoverFlow);

		layout = (LinearLayout) findViewById(R.id.layout);
		tvOne = (TextView) findViewById(R.id.tv_one);
		tvTwo = (TextView) findViewById(R.id.tv_two);
		tvTwo.setText("" + totalDays);
		final MyViewGroupAdapter adapter = new MyViewGroupAdapter(this);
		fancyCoverFlow.setAdapter(adapter);
		fancyCoverFlow.setSpacing(DisplayUtil.dip2px(MyViewGroupExample.this,
				5));
		fancyCoverFlow.setSelection(15);

//		layout.setOnTouchListener(new OnTouchListener()
//		{
//
//			@Override
//			public boolean onTouch(View v, MotionEvent event)
//			{
//				return fancyCoverFlow.dispatchTouchEvent(event);
//
//			}
//		});
		//fancyCoverFlow.setCallbackDuringFling(true);
		fancyCoverFlow.setOnItemSelectedListener(new OnItemSelectedListener()
		{

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id)
			{
				//				MyViewGroup viewGroup = adapter.getItem(position);
				//				viewGroup.getTextView().setText("第" + (position + 1) + "天");
				currentPos = position;
				onChanged(position);
				adapter.notifyDataSetChanged();
				//				if (currentPos >= currentDay)
				//				{
				//					fancyCoverFlow.setScrollingEnabled(false);
				//				} else
				//				{
				//					fancyCoverFlow.setScrollingEnabled(true);
				//				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0)
			{
			}
		});
	}

	// =============================================================================
	// Private classes
	// =============================================================================

	private class MyViewGroupAdapter extends FancyCoverFlowAdapter
	{

		// =============================================================================
		// Private members
		// =============================================================================

		// =============================================================================
		// Constructor
		// =============================================================================
		public MyViewGroupAdapter(Context context)
		{
			super();
		}

		// =============================================================================
		// Supertype overrides
		// =============================================================================

		@Override
		public int getCount()
		{
			return totalDays;
		}

		@Override
		public Integer getItem(int i)
		{
			return i;
		}

		@Override
		public long getItemId(int i)
		{
			return i;
		}

		@Override
		public View getCoverFlowItem(int i, View reuseableView,
				ViewGroup viewGroup)
		{
			MyViewGroup customViewGroup = null;
			if (reuseableView != null)
			{
				customViewGroup = (MyViewGroup) reuseableView;
			} else
			{
				customViewGroup = new MyViewGroup(viewGroup.getContext());
				customViewGroup
				.setLayoutParams(new FancyCoverFlow.LayoutParams(
						DisplayUtil.dip2px(MyViewGroupExample.this, 65),
						DisplayUtil.dip2px(MyViewGroupExample.this, 65)));
			}
			if (i == currentPos)
			{
				customViewGroup.getTextView().setText("第" + (i + 1) + "天");
				customViewGroup.setReflection(true);
			} else
			{
				customViewGroup.getTextView().setText(
						String.format("%d", i + 1));
			}
			if (i <= currentDay)
			{
				customViewGroup.getImageView().setImageResource(
						R.drawable.icon_day_his);
			} else
			{
				customViewGroup.getImageView().setImageResource(
						R.drawable.icon_day_fu);
			}
			return customViewGroup;
		}

	}

	public class MyViewGroup extends RelativeLayout
	{

		// =============================================================================
		// Child views
		// =============================================================================

		private TextView textView;

		private ImageView imageView;

		private boolean isReflection = false;

		// =============================================================================
		// Constructor
		// =============================================================================

		public MyViewGroup(Context context)
		{
			super(context);

			this.textView = new TextView(context);
			this.imageView = new ImageView(context);
			this.setGravity(Gravity.CENTER);
			RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.MATCH_PARENT,
					RelativeLayout.LayoutParams.MATCH_PARENT);
			this.textView.setLayoutParams(layoutParams);
			this.imageView.setLayoutParams(layoutParams);

			this.textView.setGravity(Gravity.CENTER);
			this.textView.setTextColor(Color.WHITE);

			this.imageView.setAdjustViewBounds(false);
			this.imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
			this.imageView.setVisibility(View.VISIBLE);

			this.addView(this.imageView);
			this.addView(this.textView);
		}

		// =============================================================================
		// Getters
		// =============================================================================
		public TextView getTextView()
		{
			return textView;
		}

		public ImageView getImageView()
		{
			return imageView;
		}

		public boolean isReflection()
		{
			return isReflection;
		}

		public void setReflection(boolean isReflection)
		{
			this.isReflection = isReflection;
		}

	}

	private void onChanged(int pos)
	{
		tvOne.setText("the current is " + pos);
		tvTwo.setTextColor(Color.RED);
	}

}
