package com.robotemi.sdk.map

import android.os.Environment
import android.util.Log
import com.github.swrirobotics.bags.reader.BagReader
import com.github.swrirobotics.bags.reader.exceptions.BagReaderException
import com.github.swrirobotics.bags.reader.messages.serialization.ArrayType
import com.github.swrirobotics.bags.reader.messages.serialization.MessageType
import com.github.swrirobotics.bags.reader.messages.serialization.UInt32Type
import com.robotemi.sdk.Robot
import java.io.File


open class RosUtils {
    companion object {
        private const val TAG: String = "RosUtils"
        @Throws(BagReaderException::class)
        @JvmStatic
        fun readBag(mapListener: Robot.MapListener) {
            val file1 =
                File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path + File.separator + "map.bag")
            val file = BagReader.readFile(file1)
            file.forMessagesOfType("nav_msgs/OccupancyGrid") { messageType, connection ->
                Log.d(TAG, "test")
                val bytes = messageType!!.getField<ArrayType>("data").asBytes
                val width =
                    messageType.getField<MessageType>("info").getField<UInt32Type>("width")
                        .value.toInt()
                val height =
                    messageType.getField<MessageType>("info").getField<UInt32Type>("height")
                        .value.toInt()
                mapListener.onMap(bytes, width, height)
                true
            }
        }
    }
}