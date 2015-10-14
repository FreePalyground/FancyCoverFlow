/*
 * Copyright 2013 David Schreiber 2013 John Paul Nalog Licensed under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package com.ranlib.customfancycoverflow;

import java.lang.reflect.Field;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Transformation;
import android.widget.Gallery;
import android.widget.SpinnerAdapter;
import at.technikum.mti.fancycoverflow.R;


/**
 * 
* @ClassName: CustomFancyCoverFlow 
* @Description: 定制化的滑动组件：圆形日期图标-滑动及阻断（人马君app+大姨吗app）
* @author BMR
* @date 2015年9月11日 下午3:42:10
 */
@SuppressWarnings("deprecation")
public class CustomFancyCoverFlow extends Gallery {
    
    // =============================================================================
    // Constants
    // =============================================================================
    public static final String TAG = "[FancyCoverFlow]";// for hifiit
    
    public static final int ACTION_DISTANCE_AUTO = Integer.MAX_VALUE;
    
    public static final float SCALEDOWN_GRAVITY_TOP = 0.0f;
    
    public static final float SCALEDOWN_GRAVITY_CENTER = 0.5f;
    
    public static final float SCALEDOWN_GRAVITY_BOTTOM = 1.0f;
    
    // =============================================================================
    // Private members
    // =============================================================================
    
    private float reflectionRatio = 0.4f;
    
    private int reflectionGap = 20;
    
    private boolean reflectionEnabled = false;
    // for hifiit
    private boolean stuck;
    private Point startPoint = new Point();
    private Point endPoint = new Point();
    private int maxPosScrollTo = Integer.MAX_VALUE;
    private int currentPos;
    /**
     * TODO: Doc
     */
    private float unselectedAlpha;
    
    /**
     * Camera used for view transformation.
     */
    private Camera transformationCamera;
    
    /**
     * TODO: Doc
     */
    private int maxRotation = 75;
    
    /**
     * Factor (0-1) that defines how much the unselected children should be
     * scaled down. 1 means no scaledown.
     */
    private float unselectedScale;
    
    /**
     * TODO: Doc
     */
    private float scaleDownGravity = SCALEDOWN_GRAVITY_CENTER;
    
    /**
     * Distance in pixels between the transformation effects (alpha, rotation,
     * zoom) are applied.
     */
    private int actionDistance;
    
    /**
     * Saturation factor (0-1) of items that reach the outer effects distance.
     */
    private float unselectedSaturation;
    
    // =============================================================================
    // Constructors
    // =============================================================================
    
    public CustomFancyCoverFlow(Context context) {
        super(context);
        this.initialize();
    }
    
    public CustomFancyCoverFlow(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initialize();
        this.applyXmlAttributes(attrs);
    }
    
    public CustomFancyCoverFlow(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.initialize();
        this.applyXmlAttributes(attrs);
    }
    
    private void initialize() {
        this.transformationCamera = new Camera();
        this.setSpacing(0);
    }
    
    private void applyXmlAttributes(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.FancyCoverFlow);
        
        this.actionDistance = a.getInteger(R.styleable.FancyCoverFlow_actionDistance,
            ACTION_DISTANCE_AUTO);
        this.scaleDownGravity = a.getFloat(R.styleable.FancyCoverFlow_scaleDownGravity, 1.0f);
        this.maxRotation = a.getInteger(R.styleable.FancyCoverFlow_maxRotation, 45);
        this.unselectedAlpha = a.getFloat(R.styleable.FancyCoverFlow_unselectedAlpha, 0.3f);
        this.unselectedSaturation = a.getFloat(R.styleable.FancyCoverFlow_unselectedSaturation,
            0.0f);
        this.unselectedScale = a.getFloat(R.styleable.FancyCoverFlow_unselectedScale, 0.75f);
    }
    
    // =============================================================================
    // Getter / Setter
    // =============================================================================
    
    public float getReflectionRatio() {
        return reflectionRatio;
    }
    
    public void setReflectionRatio(float reflectionRatio) {
        if (reflectionRatio <= 0 || reflectionRatio > 0.5f) {
            throw new IllegalArgumentException(
                "reflectionRatio may only be in the interval (0, 0.5]");
        }
        
        this.reflectionRatio = reflectionRatio;
        
        if (this.getAdapter() != null) {
            ((CustomFancyCoverFlowAdapter)this.getAdapter()).notifyDataSetChanged();
        }
    }
    
    public int getReflectionGap() {
        return reflectionGap;
    }
    
    public void setReflectionGap(int reflectionGap) {
        this.reflectionGap = reflectionGap;
        
        if (this.getAdapter() != null) {
            ((CustomFancyCoverFlowAdapter)this.getAdapter()).notifyDataSetChanged();
        }
    }
    
    public boolean isReflectionEnabled() {
        return reflectionEnabled;
    }
    
    public void setReflectionEnabled(boolean reflectionEnabled) {
        this.reflectionEnabled = reflectionEnabled;
        
        if (this.getAdapter() != null) {
            ((CustomFancyCoverFlowAdapter)this.getAdapter()).notifyDataSetChanged();
        }
    }
    
    /**
     * Use this to provide a {@link CustomFancyCoverFlowAdapter} to the coverflow.
     * This method will throw an {@link ClassCastException} if the passed
     * adapter does not subclass {@link CustomFancyCoverFlowAdapter}.
     * 
     * @param adapter
     */
    @Override
    public void setAdapter(SpinnerAdapter adapter) {
        if (!(adapter instanceof CustomFancyCoverFlowAdapter)) {
            throw new ClassCastException(CustomFancyCoverFlow.class.getSimpleName()
                                         + " only works in conjunction with a "
                                         + CustomFancyCoverFlowAdapter.class.getSimpleName());
        }
        
        super.setAdapter(adapter);
    }
    
    /**
     * Returns the maximum rotation that is applied to items left and right of
     * the center of the coverflow.
     * 
     * @return
     */
    public int getMaxRotation() {
        return maxRotation;
    }
    
    /**
     * Sets the maximum rotation that is applied to items left and right of the
     * center of the coverflow.
     * 
     * @param maxRotation
     */
    public void setMaxRotation(int maxRotation) {
        this.maxRotation = maxRotation;
    }
    
    /**
     * TODO: Write doc
     * 
     * @return
     */
    public float getUnselectedAlpha() {
        return this.unselectedAlpha;
    }
    
    /**
     * TODO: Write doc
     * 
     * @return
     */
    public float getUnselectedScale() {
        return unselectedScale;
    }
    
    /**
     * TODO: Write doc
     * 
     * @param unselectedScale
     */
    public void setUnselectedScale(float unselectedScale) {
        this.unselectedScale = unselectedScale;
    }
    
    /**
     * TODO: Doc
     * 
     * @return
     */
    public float getScaleDownGravity() {
        return scaleDownGravity;
    }
    
    /**
     * TODO: Doc
     * 
     * @param scaleDownGravity
     */
    public void setScaleDownGravity(float scaleDownGravity) {
        this.scaleDownGravity = scaleDownGravity;
    }
    
    /**
     * TODO: Write doc
     * 
     * @return
     */
    public int getActionDistance() {
        return actionDistance;
    }
    
    /**
     * TODO: Write doc
     * 
     * @param actionDistance
     */
    public void setActionDistance(int actionDistance) {
        this.actionDistance = actionDistance;
    }
    
    /**
     * TODO: Write doc
     * 
     * @param unselectedAlpha
     */
    @Override
    public void setUnselectedAlpha(float unselectedAlpha) {
        super.setUnselectedAlpha(unselectedAlpha);
        this.unselectedAlpha = unselectedAlpha;
    }
    
    /**
     * TODO: Write doc
     * 
     * @return
     */
    public float getUnselectedSaturation() {
        return unselectedSaturation;
    }
    
    /**
     * TODO: Write doc
     * 
     * @param unselectedSaturation
     */
    public void setUnselectedSaturation(float unselectedSaturation) {
        this.unselectedSaturation = unselectedSaturation;
    }
    
    // =============================================================================
    // Supertype overrides
    // =============================================================================
    
    @Override
    protected boolean getChildStaticTransformation(View child, Transformation t) {
        // We can cast here because FancyCoverFlowAdapter only creates wrappers.
        CustomFancyCoverFlowItemWrapper item = (CustomFancyCoverFlowItemWrapper)child;
        
        // Since Jelly Bean childs won't get invalidated automatically, needs to
        // be added for the smooth coverflow animation
        if (android.os.Build.VERSION.SDK_INT >= 16) {
            item.invalidate();
        }

        final int coverFlowWidth = this.getWidth();
        final int coverFlowCenter = coverFlowWidth / 2;
        final int childWidth = item.getWidth();
        final int childHeight = item.getHeight();
        final int childCenter = item.getLeft() + childWidth / 2;
        
        // Use coverflow width when its defined as automatic.
        final int actionDistance = (this.actionDistance == ACTION_DISTANCE_AUTO) ? (int)((coverFlowWidth + childWidth) / 2.0f)
            : this.actionDistance;
        
        // Calculate the abstract amount for all effects.
        final float effectsAmount = Math.min(1.0f,
            Math.max(-1.0f, (1.0f / actionDistance) * (childCenter - coverFlowCenter)));

        // Clear previous transformations and set transformation type (matrix +
        // alpha).
        t.clear();
        t.setTransformationType(Transformation.TYPE_BOTH);
        
        // Alpha
        if (this.unselectedAlpha != 1) {
            final float alphaAmount = (this.unselectedAlpha - 1) * Math.abs(effectsAmount) + 1;
            t.setAlpha(alphaAmount);
        }
        
        // Saturation
        if (this.unselectedSaturation != 1) {
            // Pass over saturation to the wrapper.
            final float saturationAmount = (this.unselectedSaturation - 1)
                                           * Math.abs(effectsAmount) + 1;
            item.setSaturation(saturationAmount);
        }
        
        final Matrix imageMatrix = t.getMatrix();
        
        // Zoom.
        if (this.unselectedScale != 1) {
            final float zoomAmount = (this.unselectedScale - 1) * Math.abs(effectsAmount) + 1;
            // Calculate the scale anchor (y anchor can be altered)
            final float translateX = childWidth / 2f;
            final float translateY = childHeight * this.scaleDownGravity;

            imageMatrix.preTranslate(-translateX, -translateY);
            imageMatrix.postScale(zoomAmount, zoomAmount);
            imageMatrix.postTranslate(translateX, translateY);
        }
        
        return true;
    }
    
    // =============================================================================
    // Public classes
    // =============================================================================
    
    @SuppressWarnings("deprecation")
    public static class LayoutParams extends Gallery.LayoutParams {
        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }
        
        public LayoutParams(int w, int h) {
            super(w, h);
        }
        
        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startPoint.set((int)event.getRawX(), (int)event.getRawX());
                break;
            case MotionEvent.ACTION_MOVE:
                endPoint.set((int)event.getRawX(), (int)event.getRawX());
                if (Math.abs(startPoint.x - endPoint.x) > 8 && (startPoint.x > endPoint.x)) {
                    return stuck || super.onTouchEvent(event);
                }
            case MotionEvent.ACTION_UP:
                break;
        }
        
        return super.onTouchEvent(event);
    }
    
    public void setScrollingEnabled(boolean enabled) {
        stuck = !enabled;
    }

    public void setCurrentPos(int currentPos) {
        this.currentPos = currentPos;
    }
    public void setMaxPosScrollTo(int maxPosScrollTo) {
        this.maxPosScrollTo = maxPosScrollTo;
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Field f;
        try {
            f = CustomFancyCoverFlow.class.getSuperclass().getDeclaredField("mDownTouchPosition");
            f.setAccessible(true);
            int position = f.getInt(this);
            if (position > maxPosScrollTo) {
                return false;
            }
        } catch (SecurityException e0) {
            e0.printStackTrace();
        } catch (NoSuchFieldException e1) {
            e1.printStackTrace();
        } catch (IllegalArgumentException e2) {
            e2.printStackTrace();
        } catch (IllegalAccessException e3) {
            e3.printStackTrace();
        }
        return super.onSingleTapUp(e);
    }
    
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        // 去掉Gallery的惯性滑动
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        // stop the scroll to right when it is greater than currentDay
        if(distanceX > 0){
            float dis = (maxPosScrollTo - currentPos) * 60;
            distanceX = distanceX > dis? dis : distanceX;
        }
        return (stuck && (distanceX > 0))|| super.onScroll(e1, e2, distanceX, distanceY);
    }


}
