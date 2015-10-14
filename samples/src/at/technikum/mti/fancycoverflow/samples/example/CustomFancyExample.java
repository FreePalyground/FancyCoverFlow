
package at.technikum.mti.fancycoverflow.samples.example;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import at.technikum.mti.fancycoverflow.samples.R;

import com.ranlib.customfancycoverflow.CustomFancyCoverFlow;
import com.ranlib.customfancycoverflow.CustomPlanCoverFlowAdapter;

/**
 * 
* @ClassName: CustomFancyExample 
* @Description: 定制化的滑动组件：圆形日期图标-滑动及阻断（人马君app+大姨吗app）
* @author BMR
* @date 2015年9月11日 下午2:24:40
 */
public class CustomFancyExample extends Activity {

    // =============================================================================
    // Child views
    // =============================================================================

    private CustomFancyCoverFlow mFancyCoverFlow;
    private CustomPlanCoverFlowAdapter mFlowAdapter;
    int totalDays = 8;//总共的天数
    int currentDay = 7;//从1开始计数，1是第一天
    
    int selectedDay = 0;
    // 解决快速滑动过程中的多次请求问题
    private int mShowingPos = -1;
    private int mToShowPos = 0;
    private static final int TIME_OUT_DISPLAY =300;
    private static final int MSG_CHECK_CHANGE =100;
    // =============================================================================
    // Supertype overrides
    // =============================================================================

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_fancy);

        mFancyCoverFlow = (CustomFancyCoverFlow) this.findViewById(R.id.custom_fancy);
        mFlowAdapter = new CustomPlanCoverFlowAdapter(this);
        mFlowAdapter.update(totalDays, currentDay);
        setupFancyFlow();

    }

    // =============================================================================
    // Private classes
    // =============================================================================
    private void setupFancyFlow() {
        final int fancyCurrentDay = currentDay - 1;//控件里面从0开始计数，0是第一天
        // 设置图片大小
        mFancyCoverFlow.setAdapter(mFlowAdapter);
        mFancyCoverFlow.setSelection(fancyCurrentDay);
        mFancyCoverFlow.setMaxPosScrollTo(fancyCurrentDay);
        mFancyCoverFlow
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        mFlowAdapter.setSelectedDay(position);
                        mFancyCoverFlow.setCurrentPos(position);
                        // onChanged(position); 底部View数据刷新接口
                        mFlowAdapter.notifyDataSetChanged();
                        if (position >= fancyCurrentDay) {
                            mFancyCoverFlow.setScrollingEnabled(false);
                        } else {
                            mFancyCoverFlow.setScrollingEnabled(true);
                        }

                        // 解决快速滑动过程中的多次请求问题
                        mToShowPos = position;
                        final Handler handler = new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                if (msg.what == MSG_CHECK_CHANGE) {
                                    if (mShowingPos != mToShowPos) {
                                        mShowingPos = mToShowPos;
                                        // 重新更新ui---此处做选中的业务
                                        selectedDay = mShowingPos + 1;
                                        refreshData();
                                    }
                                }
                            }
                        };
                        Thread checkChange = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                int index = mToShowPos;
                                try {
                                    Thread.sleep(TIME_OUT_DISPLAY);
                                    if (index == mToShowPos) {
                                        handler.sendEmptyMessage(MSG_CHECK_CHANGE);
                                    }

                                } catch (InterruptedException e) {

                                    e.printStackTrace();
                                }
                            }
                        });
                        checkChange.start();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
    }


    private void refreshData(){
        Toast.makeText(this, "当前天： " + selectedDay, Toast.LENGTH_SHORT).show();
    }
}
