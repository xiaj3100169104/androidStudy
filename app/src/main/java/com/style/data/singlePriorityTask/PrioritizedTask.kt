package com.style.data.singlePriorityTask

import android.util.Log

class PrioritizedTask() : Runnable, Comparable<PrioritizedTask> {

    public var priority: Int = 0
    public lateinit var id: String

    constructor(id: String, priority: Int) : this() {
        this.id = id
        this.priority = priority
    }

    override fun run() {
        Log.e(id, toString())
        try {
            Thread.sleep(2)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    override fun toString(): String {
        return "任务id:$id-->优先级:$priority"
    }

    override fun compareTo(other: PrioritizedTask): Int {
        if (priority < other.priority) {
            return -1
        } else {
            if (priority > other.priority) {
                return 1
            } else {
                return 0
            }
        }
    }
}