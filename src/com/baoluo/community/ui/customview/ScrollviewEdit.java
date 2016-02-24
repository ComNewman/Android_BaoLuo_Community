package com.baoluo.community.ui.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

import com.baoluo.community.util.L;
/**
 * 解决edittext 与scrollview滑动冲突
 * @author xiangyang.fu
 *
 */
public class ScrollviewEdit extends ScrollView {  
	  
    private static final String TAG = "ScrollviewEdit";     
    private ScrollView parent_scrollview;   
    public ScrollView getParent_scrollview() {  
        return parent_scrollview;  
    }  
    public void setParent_scrollview(ScrollView parent_scrollview) {  
        this.parent_scrollview = parent_scrollview;  
    }  
  
    public ScrollviewEdit(Context context) {  
        super(context);  
    }  
      
    public ScrollviewEdit(Context context, AttributeSet attrs) {  
        super(context, attrs);  
    }  
  
    int currentY;  
    @Override  
    public boolean onInterceptTouchEvent(MotionEvent ev) {  
        L.i(TAG, "onInterceptTouchEvent--------");      
        if (parent_scrollview == null) {  
            return super.onInterceptTouchEvent(ev);  
        } else {  
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {  
                // 将父scrollview的滚动事件拦截  
                currentY = (int) ev.getY();  
                setParentScrollAble(false);  
                L.i(TAG, "将父scrollview的滚动事件拦截-----");      
                return super.onInterceptTouchEvent(ev);    
            } else if (ev.getAction() == MotionEvent.ACTION_UP) {  
                // 把滚动事件恢复给父Scrollview  
                setParentScrollAble(true);  
                L.i(TAG, "把滚动事件恢复给父Scrollview-----");    
            } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {  
            }  
        }  
        return super.onInterceptTouchEvent(ev);    
    }  
    /** 
     * 是否把滚动事件交给父scrollview 
     * @param flag 
     */  
    private void setParentScrollAble(boolean flag) {  
        parent_scrollview.requestDisallowInterceptTouchEvent(!flag);  
    }  
}  
