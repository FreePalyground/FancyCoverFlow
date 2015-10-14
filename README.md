CustomFancyCoverFlow
==============
在Github的FancyCoverFlow项目基础上，增加了以下功能。

1.水平时间滑动组件，显示三种日期：历史日期，未来日期，当前日期，其中当前日期背景特定。

2.实现滑动阻断：只能滑动到当前日期，不可滑动到未来日期

3.支持点击选中，历史日期除外。

4.解决了滑动阻断和滑动过程每个项目均会经历选中状态的冲突。利用setCallbackDuringFling(false)则滑动阻断实现不了，才用线程睡眠时间间隔判断的方式.

原项目地址:https://github.com/davidschreiber/FancyCoverFlow


FancyCoverFlow
==============

THIS PROJECT IS NO LONGER MAINTAINED!
=====================================

## What is FancyCoverFlow?
FancyCoverFlow is a flexible Android widget providing out of the box view transformations to give your app a unique look and feel. Curious about what FancyCoverFlow can do for you? Check out the FancyCoverFlow examples on Google Play.

[![Google Play Link](http://davidschreiber.github.io/FancyCoverFlow/en_generic_rgb_wo_45.png)](https://play.google.com/store/apps/details?id=at.technikum.mti.fancycoverflow.samples)
![FancyCoverFlow Framed Screenshot](http://davidschreiber.github.io/FancyCoverFlow/screenshot2.png)

## How to use?
Using FancyCoverFlow in your Android app is as simple as

	fancyCoverFlow = new FancyCoverFlow(context);
	fancyCoverFlow.setMaxRotation(45);
	fancyCoverFlow.setUnselectedAlpha(0.3f);
	fancyCoverFlow.setUnselectedSaturation(0.0f);
	fancyCoverFlow.setUnselectedScale(0.4f);

You can also inflate FancyCoverFlow from XML:

	<at.technikum.mti.fancycoverflow.FancyCoverFlow
	        android:layout_width="match_parent"
        	android:layout_height="match_parent"
	        fcf:maxRotation="45"
	        fcf:unselectedAlpha="0.3"
        	fcf:unselectedSaturation="0.0"
	        fcf:unselectedScale="0.4" />
