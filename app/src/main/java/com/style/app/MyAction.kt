package com.style.app


import android.os.Environment

/**
 * 避免广播action重复混乱，广播action最好统一放在这里
 * Created by xiajun on 2016/11/25.
 */

class MyAction {


    companion object {

        /**
         * 广播action
         */
        const val ACTION_REFRESH_CONVERSATION = "action.refresh.conversation"
        const val ACTION_FILE_PREPARE_DOWNLOAD = "action.file.prepare.download"
        const val ACTION_FILE_GET_FAIL = "action.file.get.fail"
        const val ACTION_FILE_CREATE_FAIL = "action.file.create.fail"
        const val ACTION_FILE_DOWNING = "action.file.downing"
        const val ACTION_FILE_CANCEL_DOWNLOAD = "action.file.cancel.download"
        const val ACTION_FILE_DOWN_COMPLETE = "action.file.down.complete"
        const val ACTION_FILE_DOWN_FAIL = "action.file.down.fail"
    }

}
