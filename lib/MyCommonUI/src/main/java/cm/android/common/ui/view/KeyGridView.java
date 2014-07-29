package cm.android.common.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;
import cm.android.common.ui.MyViewExtend.IRefreshListener;
import cm.android.common.ui.MyViewExtend.MyOnFocusChangeListener;
import cm.android.common.ui.MyViewExtend.OnAtEndListener;

public class KeyGridView extends GridView {
    private IRefreshListener onSlidingAtEndListener;
    private OnAtEndListener onAtEndListener = new OnAtEndListener() {
        @Override
        public boolean onAtEnd(int type) {
            switch (type) {
                case OnAtEndListener.TYPE_KEY_LEFT:
                    // 如果有上一行，切换到上一行
                    if (getRowIndex() > 1) {
                        setSelection(getSelectedItemPosition() - 1);
                        return true;
                    } else {
                        // 重新加载
                    }
                    break;
                case OnAtEndListener.TYPE_KEY_RIGHT:
                    // 如果有下一行，切换到下一行
                    if (getRowIndex() < getNumRows()) {
                        setSelection(getSelectedItemPosition() + 1);
                        return true;
                    } else {
                        // 加载更多
                        int refreshType = getRefreshType(type);
                        if (onSlidingAtEndListener != null) {
                            return onSlidingAtEndListener.onRefresh(refreshType);
                        }
                    }
                case OnAtEndListener.TYPE_KEY_UP:
                    // 重新加载
                    break;
                case OnAtEndListener.TYPE_KEY_DOWN:
                    // 加载更多
                    int refreshType = getRefreshType(type);
                    if (onSlidingAtEndListener != null) {
                        return onSlidingAtEndListener.onRefresh(refreshType);
                    }
                default:
                    break;
            }
            return false;
        }
    };
    private int keyType = OnAtEndListener.TYPE_KEY_UP
            | OnAtEndListener.TYPE_KEY_DOWN | OnAtEndListener.TYPE_KEY_LEFT
            | OnAtEndListener.TYPE_KEY_RIGHT;

    public KeyGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public KeyGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    // private OnAtEndListener onAtEndListener;

    // public void setKeyListenType(int type) {
    // keyType = type;
    // }

    public KeyGridView(Context context) {
        super(context);
    }

    private boolean dispatchKeyLeft() {
        return dispatchKey(OnAtEndListener.TYPE_KEY_LEFT);
    }

    private boolean dispatchKeyRight() {
        return dispatchKey(OnAtEndListener.TYPE_KEY_RIGHT);
    }

    private boolean dispatchKeyUp() {
        return dispatchKey(OnAtEndListener.TYPE_KEY_UP);
    }

    private boolean dispatchKeyDown() {
        return dispatchKey(OnAtEndListener.TYPE_KEY_DOWN);
    }

    private boolean dispatchKey(int keyType) {
        if (keyType == OnAtEndListener.TYPE_KEY_LEFT) {
            // 判断是否是第一列
            if (getColumnIndex() == 1) {
                return onAtEndListener.onAtEnd(OnAtEndListener.TYPE_KEY_LEFT);
            }
        } else if (keyType == OnAtEndListener.TYPE_KEY_RIGHT) {
            // 判断是否是最后一列或者是最后一个
            if (getColumnIndex() == getNumColumns() || isLast()) {
                return onAtEndListener.onAtEnd(OnAtEndListener.TYPE_KEY_RIGHT);
            }
        }
        if (keyType == OnAtEndListener.TYPE_KEY_UP) {
            // 判断是否是第一行
            if (getRowIndex() == 1) {
                return onAtEndListener.onAtEnd(OnAtEndListener.TYPE_KEY_UP);
            }
        } else if (keyType == OnAtEndListener.TYPE_KEY_DOWN) {
            // 如果是倒数第二行，则跳到最后一个；如果是最后一行，则翻页
            if (getRowIndex() == getNumRows()) {
                // 到头
                return onAtEndListener.onAtEnd(OnAtEndListener.TYPE_KEY_DOWN);
            }
        }
        return false;
    }

    /**
     * 获取列索引，从1开始
     *
     * @return
     */
    public int getColumnIndex() {
        return getSelectedItemPosition() % getNumColumns() + 1;
    }

    /**
     * 获取行索引，从1开始
     *
     * @return
     */
    public int getRowIndex() {
        return getSelectedItemPosition() / getNumColumns() + 1;
    }

    /**
     * 获取行数
     *
     * @return
     */
    public int getNumRows() {
        return ((getAdapter().getCount() - 1) / getNumColumns()) + 1;
    }

    public boolean isLast() {
        return (getSelectedItemPosition() == (getAdapter().getCount() - 1));
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int keyCode = event.getKeyCode();
        int keyAction = event.getAction();
        if (keyAction == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                if (dispatchKeyLeft()) {
                    return true;
                }
            } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                if (dispatchKeyRight()) {
                    return true;
                }
            } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                if (dispatchKeyUp()) {
                    return true;
                }
            } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                if (dispatchKeyDown()) {
                    return true;
                }
            }
        }

        return super.dispatchKeyEvent(event);
    }

    private int getRefreshType(int keyTpye) {
        switch (keyTpye) {
            case OnAtEndListener.TYPE_KEY_LEFT:
            case OnAtEndListener.TYPE_KEY_UP:
                return IRefreshListener.LOAD_PREV;
            case OnAtEndListener.TYPE_KEY_RIGHT:
            case OnAtEndListener.TYPE_KEY_DOWN:
                return IRefreshListener.LOAD_NEXT;
            default:
                break;
        }
        return IRefreshListener.LOAD_PREV;
    }

    public boolean selectPrevView() {
        int prev = getSelectedItemPosition() - 1;
        if (hasView(prev)) {
            setSelection(prev);
            return true;
        }
        return false;
    }

    public boolean selectNextView() {
        int next = getSelectedItemPosition() + 1;
        if (hasView(next)) {
            setSelection(next);
            return true;
        }
        return false;
    }

    public boolean hasView(int position) {
        if (position < getAdapter().getCount() && position >= 0) {
            return true;
        }
        return false;
    }

    public void setOnSlidingAtEndListener(IRefreshListener l) {
        this.onSlidingAtEndListener = l;
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
        setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
    }

    @Override
    public void setFocusable(boolean focusable) {
        super.setFocusable(focusable);
        if (focusable) {
            setOnFocusChangeListener(getOnFocusChangeListener());
        }
    }

    @Override
    public void setOnFocusChangeListener(OnFocusChangeListener l) {
        super.setOnFocusChangeListener(new MyOnFocusChangeListener(this, l));
    }
}
