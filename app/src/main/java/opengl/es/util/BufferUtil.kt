package opengl.es.util

import java.nio.*


/**
 * 作者：jingjinggu on 2017/11/3 11:09
 * 邮箱：gujj512@163.com
 */
class BufferUtil {

    companion object {

        @JvmStatic
        fun floatToBuffer(a: FloatArray?): FloatBuffer? {
            //先初始化buffer，数组的长度*4，因为一个float占4个字节
            var mbb = ByteBuffer.allocateDirect((a?.size?:0)* 4)
            //数组排序用nativeOrder
            mbb.order(ByteOrder.nativeOrder())
            var mBuffer = mbb.asFloatBuffer()
            mBuffer.put(a)
            mBuffer.position(0)
            return mBuffer
        }

        @JvmStatic
        fun intToBuffer(a: IntArray): IntBuffer? {

            //先初始化buffer，数组的长度*4，因为一个float占4个字节
            var mbb = ByteBuffer.allocateDirect(a?.size* 4)
            //数组排序用nativeOrder
            mbb.order(ByteOrder.nativeOrder())
            var intBuffer = mbb.asIntBuffer()
            intBuffer.put(a)
            intBuffer.position(0)
            return intBuffer
        }

        @JvmStatic
        fun shortToBuffer(a: ShortArray): ShortBuffer? {

            //先初始化buffer，数组的长度*4，因为一个float占4个字节
            var mbb = ByteBuffer.allocateDirect(a?.size* 4)
            //数组排序用nativeOrder
            mbb.order(ByteOrder.nativeOrder())
            var intBuffer = mbb.asShortBuffer()
            intBuffer.put(a)
            intBuffer.position(0)
            return intBuffer
        }
    }

}