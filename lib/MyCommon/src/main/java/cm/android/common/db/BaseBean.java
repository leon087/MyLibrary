package cm.android.common.db;

import com.j256.ormlite.field.DatabaseField;

public abstract class BaseBean {

    @DatabaseField(generatedId = true)
    private int _id;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

}
