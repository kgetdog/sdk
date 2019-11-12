package com.robotemi.sdk.map

import android.os.Environment
import android.util.Log
import com.github.swrirobotics.bags.reader.BagReader
import com.github.swrirobotics.bags.reader.exceptions.BagReaderException
import com.github.swrirobotics.bags.reader.messages.serialization.ArrayType
import com.github.swrirobotics.bags.reader.messages.serialization.MessageType
import com.github.swrirobotics.bags.reader.messages.serialization.UInt32Type
import com.robotemi.sdk.Robot
import java.io.*
import org.apache.commons.compress.utils.IOUtils
import org.ros.math.RosMath


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

        @JvmStatic
        fun test(){
            val file =
                File(Environment.getExternalStoragePublicDirectory("temi").path + File.separator + "serialized_map.txt")

            val size = file.length().toInt()
            val bytes = ByteArray(size)
            val buf = null
            try {
                val buf = BufferedInputStream(FileInputStream(file))
                buf.read(bytes, 0, bytes.size)

                val obuf = IOUtils.toByteArray(buf)
                var Od: Any? = null
                try {
                    val s: ObjectInputStream
                    val bais = ByteArrayInputStream(obuf)
                    //ReadableByteChannel rbc = Channels.newChannel(bais);
                    s = ObjectInputStream(bais/*Channels.newInputStream(rbc)*/)
                    Od = s.readObject()
                    s.close()
                    bais.close()
                    buf.close()
                    //rbc.close();
                } catch (cnf: IOException) {
                    Log.e(TAG, "Class cannot be deserialized, may have been modified beyond version compatibility")
                } catch (cnf: ClassNotFoundException) {
                    Log.e(TAG, "Class cannot be deserialized, may have been modified beyond version compatibility")
                }

                Log.e(TAG, "Deserialize return:" + Od!!)
            } catch (e: FileNotFoundException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            } catch (e: IOException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            }
        }
    }
}