package wd.android.framework.global;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户保存全局数据
 */
public class GlobalData {
	private Map<String, Object> mData = new HashMap<String, Object>();

	public GlobalData() {
		// 默认保存AccountData
		putData(CommonTag.ACCOUNT_DATA, new AccountData());
	}

	public void release() {
		AccountData accountData = getData(CommonTag.ACCOUNT_DATA);
		accountData.release();
		mData.clear();
	}

	/**
	 * 获取全局数据
	 * 
	 * @param <T>
	 * 
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T getData(String key) {
		return (T) mData.get(key);
	}

	/**
	 * 保存全局数据
	 * 
	 * @param <T>
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public <T> Object putData(String key, T value) {
		return mData.put(key, value);
	}
}
