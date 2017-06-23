package com.style.bean;


import java.io.Serializable;
/**
 *一个类 被实例化 也就是被new的时候 最先执行的是 构造函数，如果你有留心。你会发现很多类里面根本没有写构造函数。
 在java类中，如果不显示声明构造函数，JVM 会给该类一个默认的构造函数。一个类 可以有多个构造函数。
 构造函数的主要作用 一是用来实例化该类。二是 让该类实例化的时候执行哪些方法，初始化哪些属性。
 当一个类声明了构造函数以后，JVM 是不会再给该类分配默认的构造函数。
 */
public class User implements Serializable {

	private long id;
	private String userId;// 帐号
	private String telPhone;
	private String password; // 密码
	private String userName;
	private String sex;
	private String signKey;

	public User() {
	}

	public User(String userId, String password) {
		this.userId = userId;
		this.password = password;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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
				", password='" + password + '\'' +
				", userName='" + userName + '\'' +
				", sex='" + sex + '\'' +
				", signKey='" + signKey + '\'' +
				'}';
	}

}
