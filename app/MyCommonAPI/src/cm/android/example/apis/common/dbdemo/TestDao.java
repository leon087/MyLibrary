package cm.android.example.apis.common.dbdemo;

import cm.android.common.db.BaseDao;
import com.j256.ormlite.dao.Dao.CreateOrUpdateStatus;

public class TestDao extends BaseDao<TestBean> {

    public TestBean queryByUserName(String userName) {
        return super.queryForFirst("userName", userName);
    }

    @Override
    public CreateOrUpdateStatus insertOrUpdate(TestBean bean) {
        return super.insertOrUpdate(bean, "userName", bean.getUserName());
    }
}
