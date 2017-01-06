package test.bean;


import java.io.Serializable;
public class User implements Serializable {

	private long id;
	private long userId;
	private String telPhone;
	private String account; // 帐号
	private String password; // 密码
	private String userName;
	private String sex;
	private String signKey;

	public User() {
	}

	public User(long userId, String account, String password, String userName, String signKey) {
		this.userId = userId;
		this.account = account;
		this.password = password;
		this.userName = userName;
		this.signKey = signKey;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
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


	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", userId=" + userId +
				", telPhone='" + telPhone + '\'' +
				", account='" + account + '\'' +
				", password='" + password + '\'' +
				", userName='" + userName + '\'' +
				", sex='" + sex + '\'' +
				", signKey='" + signKey + '\'' +
				'}';
	}

}
