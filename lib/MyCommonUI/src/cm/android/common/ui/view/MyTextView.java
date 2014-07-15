package cm.android.common.ui.view;

import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.widget.TextView;

public class MyTextView extends TextView {

    public static final int TYPE_MARQUEE = 0x01;
    public static final int TYPE_END = TYPE_MARQUEE + 1;
    public static final int TYPE_DEFAULT = TYPE_MARQUEE + 2;
    private int type = TYPE_MARQUEE;
    private boolean isFocused = true;

    public MyTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyTextView(Context context) {
        super(context);
    }

    @Override
    public boolean isFocused() {
        if (type == TYPE_DEFAULT) {
            return super.isFocused();
        }
        return isFocused;
    }

    public void setFocused(boolean isFocused) {
        this.isFocused = isFocused;
    }

    public void setType(int type) {
        this.type = type;
        if (type == TYPE_MARQUEE) {
            this.setEllipsize(TruncateAt.MARQUEE);
            this.setMarqueeRepeatLimit(-1);
            this.setSingleLine();
            this.setFocused(true);
        } else if (type == TYPE_END) {
            this.setEllipsize(TruncateAt.END);
            this.setFocused(false);
            this.setMarqueeRepeatLimit(3);
            // this.setSingleLine();
        } else {
            this.type = TYPE_DEFAULT;
            this.setMarqueeRepeatLimit(3);
            // this.setSingleLine(false);
        }
    }
}
