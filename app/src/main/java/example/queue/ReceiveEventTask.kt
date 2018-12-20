package example.queue

import android.os.Handler
import java.lang.reflect.InvocationTargetException
import java.util.HashMap
import java.util.HashSet

/**
 * 接收事件任务
 */
class ReceiveEventTask : Runnable {

    private var code: Int
    private var data: Any
    private var subscriberMap: HashMap<Any, HashSet<Int>>

    private var mUIHandler: Handler

    constructor(code: Int, data: Any, subscriberMap: HashMap<Any, HashSet<Int>>, mUIHandler: Handler) {
        this.code = code
        this.data = data
        this.subscriberMap = subscriberMap
        this.mUIHandler = mUIHandler
    }

    override fun run() {
        for (subscriber in subscriberMap.keys) {
            val codes = subscriberMap[subscriber]
            for (eventCode in codes!!) {
                if (code == eventCode) {
                    dispatchEvent(subscriber, code, data)
                    break
                }
            }
        }
    }

    /**
     * 分发事件
     */
    private fun dispatchEvent(subscriber: Any, eventCode: Int, data: Any) {
        mUIHandler.post {
            try {
                val clazz = subscriber.javaClass
                val method = clazz.getMethod("onMainThreadEvent", Int::class.java, Any::class.java)
                method.invoke(subscriber, eventCode, data)
            } catch (e: NoSuchMethodException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: InvocationTargetException) {
                e.printStackTrace()
            }
        }
    }
}