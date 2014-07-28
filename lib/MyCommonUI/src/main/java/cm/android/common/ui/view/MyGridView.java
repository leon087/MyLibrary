package cm.android.common.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.GridView;

/**
 * 自适应高度的GridView
 *
 * @author Administrator
 */
public class MyGridView extends GridView {
    public final static int SCROOL_UNKNOW = 0;
    public final static int SCROOL_UP = SCROOL_UNKNOW + 1;
    public final static int SCROOL_DOWN = SCROOL_UP + 1;
    public final static int SCROOL_LEFT = SCROOL_DOWN + 1;
    public final static int SCROOL_RIGHT = SCROOL_LEFT + 1;
    private int scrollDirctionX = SCROOL_UNKNOW;
    private int scrollDirctionY = SCROOL_UNKNOW;
    private GestureDetector gestureDetector = new GestureDetector(
            new GestureDetector.SimpleOnGestureListener() {
                public boolean onDown(MotionEvent arg0) {
                    return true;
                }

                ;

                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2,
                                       float velocityX, float velocityY) {

                    return super.onFling(e1, e2, velocityX, velocityY);
                }

                @Override
                public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                        float distanceX, float distanceY) {

                    if (distanceY > 0) {
                        scrollDirctionY = SCROOL_UP;
                    } else if (distanceY < 0) {
                        scrollDirctionY = SCROOL_DOWN;
                    }
                    if (distanceX > 0) {
                        scrollDirctionX = SCROOL_RIGHT;

                    } else if (distanceX < 0) {
                        scrollDirctionX = SCROOL_LEFT;
                    }
                    return super.onScroll(e1, e2, distanceX, distanceY);
                }

            });
    private boolean allowScrollYAxis = true;
    private boolean allowScroll = true;

    public MyGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public MyGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyGridView(Context context) {
        super(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // gestureDetector.onTouchEvent(ev);
        if (!allowScrollYAxis && ev.getAction() == MotionEvent.ACTION_MOVE) {
            return true;

        }
        return super.onTouchEvent(ev);
    }

    public int getScroolDirctionX() {
        return scrollDirctionX;
    }

    public int getScroolDirctionY() {
        return scrollDirctionY;
    }

    public boolean isAllowScroolYAxis() {
        return allowScrollYAxis;
    }

    public void setAllowScroolYAxis(boolean allowScroolYAxis) {
        this.allowScrollYAxis = allowScroolYAxis;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!allowScroll) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(
                    Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setAllowScrool(boolean allowScrool) {
        this.allowScroll = allowScrool;
    }

}
