package wd.android.example.apis.common.dbdemo;

import wd.android.common.db.BaseBean;

import com.j256.ormlite.field.DatabaseField;

public class TestBean extends BaseBean {
	@DatabaseField
	private String userName;
	@DatabaseField
	private int age;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

}
