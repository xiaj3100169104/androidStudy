package test.bean;


import java.io.Serializable;
public class User implements Serializable {

	private String userId;
	private String telPhone;
	private String account; // 帐号
	private String password; // 密码
	private String userName;
	private String sex;
	private String signKey;

	@Override
	public String toString() {
		return "User{" +
				"userId='" + userId + '\'' +
				", telPhone='" + telPhone + '\'' +
				", account='" + account + '\'' +
				", password='" + password + '\'' +
				", userName='" + userName + '\'' +
				", sex='" + sex + '\'' +
				", signKey='" + signKey + '\'' +
				'}';
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTelPhone() {
		return telPhone;
	}

	public void setTelPhone(String telPhone) {
		this.telPhone = telPhone;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getSignKey() {
		return signKey;
	}

	public void setSignKey(String signKey) {
		this.signKey = signKey;
	}
}
