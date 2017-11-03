package gjj.retrofit.design

import android.app.Activity
import android.util.Log
import android.widget.Toast

/**
 * 作者：jingjinggu on 2017/10/31 23:18
 * 邮箱：gujj512@163.com
 */
fun Activity.toast(message:CharSequence, duration: Int = Toast.LENGTH_SHORT){

    Toast.makeText(this,message,duration).show()

}

fun Any.log_e(message: String="",tag:String="retrofit-design-"){

    Log.e(tag,message)
}

fun Any.log_w(message: String="",tag:String="retrofit-design-"){

    Log.w(tag,message)
}

fun Any.log_d(message: String="",tag:String="retrofit-design-"){

    Log.d(tag,message)
}
