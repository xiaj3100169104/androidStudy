package com.style.entity

import android.arch.persistence.room.*
import android.arch.persistence.room.ForeignKey.CASCADE
import java.io.Serializable

@Entity(tableName = "user_info_table",
        foreignKeys = [ForeignKey(entity = UserBean::class, parentColumns = arrayOf("id"), childColumns = arrayOf("user_id"), onDelete = CASCADE)])
open class UserInfo(var sex: String?) : Serializable {

    @PrimaryKey
    @ColumnInfo(name = "user_id")
    var userId: String? = null
    @ColumnInfo(name = "tel_phone")
    var telPhone: String? = null
    @ColumnInfo(name = "password")
    var password: String? = null
    @ColumnInfo(name = "username")
    var userName: String? = null
    @Ignore
    var signKey: String? = null

    constructor() : this(null)

    constructor(userId: String, password: String) : this() {
        this.userId = userId
        this.password = password
    }

}

data class SubUserInfo(var weight: String) : UserInfo("") {

}