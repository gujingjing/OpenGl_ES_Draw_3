package opengl.es.util

import android.content.Context.ACTIVITY_SERVICE
import android.app.ActivityManager
import android.app.Activity
import android.content.Context
import android.opengl.GLES20
import java.nio.FloatBuffer


/**
 *  * 作者：jingjinggu on 2017/11/3 14:38
 *  * 邮箱：gujj512@163.com
 *  
 */
class Utils {

    companion object {

        /**
         * 判断是否支持opel 2.0
         */
        fun supportGlEs20(activity: Activity): Boolean {

            var activityManager = activity.getSystemService(
                    Context.ACTIVITY_SERVICE) as ActivityManager

            return activityManager.deviceConfigurationInfo.reqGlEsVersion >= 0x20000
        }
        /**
         * 加载shader着色器
         */
        @JvmStatic fun loadShader(type: Int, shaderCode: String): Int {
            val shader = GLES20.glCreateShader(type)
            GLES20.glShaderSource(shader, shaderCode)
            GLES20.glCompileShader(shader)
            return shader
        }
        /**
         * 初始化程序的加载
         */
//        @JvmStatic fun init(vertexBuffer:FloatBuffer?,coords:FloatArray,vertexShaderCode:String,
//                            fragmentShaderCode:String,mProgram:Int){
//            vertexBuffer = BufferUtil.floatToBuffer(coords)
//            val vertexShader = Utils.loadShader(GLES20.GL_VERTEX_SHADER,
//                    vertexShaderCode)
//            val fragmentShader = Utils.loadShader(GLES20.GL_FRAGMENT_SHADER,
//                    fragmentShaderCode)
//
//            //创建一个空的OpenGLES程序
//            mProgram = GLES20.glCreateProgram()
//            //将顶点着色器加入到程序
//            GLES20.glAttachShader(mProgram, vertexShader)
//            //将片元着色器加入到程序中
//            GLES20.glAttachShader(mProgram, fragmentShader)
//            //连接到着色器程序
//            GLES20.glLinkProgram(mProgram)
//        }
    }
}
