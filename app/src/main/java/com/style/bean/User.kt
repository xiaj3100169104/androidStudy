package com.style.bean


import java.io.Serializable

/**
 * 一个类 被实例化 也就是被new的时候 最先执行的是 构造函数，如果你有留心。你会发现很多类里面根本没有写构造函数。
 * 在java类中，如果不显示声明构造函数，JVM 会给该类一个默认的构造函数。一个类 可以有多个构造函数。
 * 构造函数的主要作用 一是用来实例化该类。二是 让该类实例化的时候执行哪些方法，初始化哪些属性。
 * 当一个类声明了构造函数以后，JVM 是不会再给该类分配默认的构造函数。
 */
class User : Serializable {

    var id: Long? = null
    var userId: String? = null// 帐号
    var telPhone: String? = null
    var password: String? = null // 密码
    var userName: String? = null
    var sex: String? = null
    var signKey: String? = null

    constructor() {}

    constructor(userId: String, password: String) {
        this.userId = userId
        this.password = password
    }

    override fun toString(): String {
        return "User(id=$id, userId='$userId', telPhone='$telPhone', password='$password', userName='$userName', sex='$sex', signKey='$signKey')"
    }


}
