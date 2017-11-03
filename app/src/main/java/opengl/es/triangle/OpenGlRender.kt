package opengl.es.triangle

import android.opengl.GLSurfaceView
import gjj.retrofit.design.log_e
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import opengl.es.util.BufferUtil


/**
 * 作者：jingjinggu on 2017/11/3 11:11
 * 邮箱：gujj512@163.com
 */
class OpenGlRender :GLSurfaceView.Renderer {

    //三角形的顶点坐标
    private var mTriangleArray = floatArrayOf(0f, 1f, 0f, //上
                                    -1f, -1f, 0f, //左下
                                    1f, -1f, 0f)//右下
    //三角形绘制缓冲
    private var mTriangleBuffer: FloatBuffer? = null

    //绘制的颜色数组
    private var mColorArray = floatArrayOf(1f, 0f, 0f, 1f, //红
            0f, 1f, 0f, 1f, //绿
            0f, 0f, 1f, 1f      //蓝
    )
    //颜色数组缓冲
    private var mColorBuffer: FloatBuffer? = null

    //正方形的四个顶点
    private var quateBuffer: FloatBuffer? = null
    private var mQuateArray = floatArrayOf(-1f, -1f, 0f,//左下
                                            1f, -1f, 0f,//右下
                                            -1f, 1f, 0f,//左上
                                                1f, 1f, 0f)//右上

//    private var mQuateArray = floatArrayOf(1f, 1f, 0f,//右上
//            -1f, 1f, 0f,//左上
//            -1f, -1f, 0f,//左下
//            1f, -1f, 0f)//右下

    //保存，绘制的位置
    //0-3分别表示  右上、左上、左下、右下
    private val VERTEX_INDEX = shortArrayOf(0, 1, 2, 0, 2, 3)


    override fun onDrawFrame(gl: GL10?) {

        gl?.glClear(GL10.GL_COLOR_BUFFER_BIT or GL10.GL_DEPTH_BUFFER_BIT)
        //使用数组作为颜色
        gl?.glColorPointer(4, GL10.GL_FLOAT, 0, mColorBuffer)

        //绘制小三角形
        gl?.glLoadIdentity()
        gl?.glTranslatef(-1.5f, 0.0f, -6.0f)
        gl?.glVertexPointer(3, GL10.GL_FLOAT, 0, mTriangleBuffer)//数组指向三角形顶点buffer
        gl?.glDrawArrays(GL10.GL_TRIANGLES, 0, 3)
//        gl.glDisableClientState(GL10.GL_COLOR_ARRAY)
        gl?.glFinish()

        //绘制正方形
        gl?.glLoadIdentity()
        gl?.glTranslatef(1.5f, 0.0f, -6.0f)
//        gl.glColor4f(1.0f, 0.0f, 0.0f, 1.0f)
        gl?.glVertexPointer(3, GL10.GL_FLOAT, 0, quateBuffer)
        gl?.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4)
        gl?.glFinish()

    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {

        gl?.glViewport(0, 0, width, height)

        var ratio:Float = (width / (height+0.0)).toFloat()
        log_e(ratio.toString())
        gl?.glMatrixMode(GL10.GL_PROJECTION)
        gl?.glLoadIdentity()
        gl?.glFrustumf(-ratio, ratio, -1f, 1f, 1f, 10f)
        gl?.glMatrixMode(GL10.GL_MODELVIEW)
        gl?.glLoadIdentity()


    }

    override fun onSurfaceCreated(gl: GL10?, p1: EGLConfig?) {

        gl?.glShadeModel(GL10.GL_SMOOTH)
        gl?.glClearColor(1.0f, 1.0f, 1.0f, 0f)
        gl?.glClearDepthf(1.0f)
        gl?.glEnable(GL10.GL_DEPTH_TEST)
        gl?.glDepthFunc(GL10.GL_LEQUAL)
        gl?.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST)
        gl?.glEnableClientState(GL10.GL_VERTEX_ARRAY)
        gl?.glEnableClientState(GL10.GL_COLOR_ARRAY)

        mTriangleBuffer = BufferUtil.floatToBuffer(mTriangleArray)
        mColorBuffer = BufferUtil.floatToBuffer(mColorArray)
        quateBuffer = BufferUtil.floatToBuffer(mQuateArray)

    }


}