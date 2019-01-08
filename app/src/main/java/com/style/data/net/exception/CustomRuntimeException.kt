package com.style.data.net.exception

/**
 * 因为RuntimeException不会在编译时提示捕获，相对于隐式传递异常，为了安全起见。
 * 最好在需要时抛出此异常以便捕获。
 * 注：kotlin不提示抛出任何异常。
 */
class CustomRuntimeException(message: String?) : Exception(message) {
}