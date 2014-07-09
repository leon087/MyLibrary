package cm.android.custom;

import cm.android.app.model.TestModel;
import cm.android.util.MyLog;
import cm.android.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;

import java.util.Map;

public class TestModelTest {
	public static void test() {
		TestModel testModel = genModel();
		String str = JSON.toJSONString(testModel);
		MyLog.e("ggggg str = " + str);
		TestModel testModel1 = JSON.parseObject(str, TestModel.class);
		MyLog.e("gggg tesModel1 = " + testModel1.toString());
	}

	public static void tes2() {
		TestModel testModel = genModel();
		Gson gson = new Gson();
		String str = gson.toJson(testModel);
		MyLog.e("ggggg str = " + str);
		TestModel testModel1 = gson.fromJson(str, TestModel.class);
		MyLog.e("gggg tesModel1 = " + testModel1.toString());
	}

	private static TestModel genModel() {
		TestModel testModel = new TestModel();
		TestModel.Test test = new TestModel.Test();
		test.setAge(12);
		test.setName("test");
		testModel.setCode(1234);
		testModel.setMsg("succeed");
		testModel.setTest(test);
		Map<String, String> map = ObjectUtil.newHashMap();
		map.put("key1", "value1");
		map.put("k2", "v2");
		map.put("k3", "v3");
		testModel.setValue(map);
		return testModel;
	}
}
