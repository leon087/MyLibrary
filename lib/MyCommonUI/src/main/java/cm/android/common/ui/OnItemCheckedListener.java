package cm.android.common.ui;

/**
 * 提供item check状态
 */
public interface OnItemCheckedListener {

    void onItemChecked(int position, boolean checked);

    /**
     * 单选Holder
     */
    public static class SingleItemCheckHolder {

        private static final int INVALID = -1;

        private int checkedPosition = INVALID;

        private OnItemCheckedListener onItemCheckedListener;

        public SingleItemCheckHolder() {
        }

        /**
         * 选中
         */
        public void check(int position) {
            if (onItemCheckedListener != null) {
                if (checkedPosition != position) {
                    onItemChecked(checkedPosition, false);
                }
                onItemChecked(position, true);
            }
            checkedPosition = position;
        }

        private void onItemChecked(int position, boolean checked) {
            if (position != INVALID) {
                onItemCheckedListener.onItemChecked(position, checked);
            }
        }

        public void setOnItemCheckedListener(
                OnItemCheckedListener onItemCheckedListener) {
            this.onItemCheckedListener = onItemCheckedListener;
        }
    }
}
