package opengl.es.util

import android.content.Context.ACTIVITY_SERVICE
import android.app.ActivityManager
import android.app.Activity
import android.content.Context
import android.opengl.GLES20


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
    }
}
