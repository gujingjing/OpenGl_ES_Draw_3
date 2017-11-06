package opengl.es.triangle

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import opengl.es.util.BufferUtil
import opengl.es.util.Utils
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * 作者：jingjinggu on 2017/11/3 16:25
 * 邮箱：gujj512@163.com
 */
class MyThreeRender :GLSurfaceView.Renderer {


    private val VERTEX_SHADER = (
            "attribute vec4 vPosition;\n"
                    + "void main() {\n"
                    + " gl_Position = vPosition;\n"
                    + "}")


    private val FRAGMENT_SHADER = (
            "precision mediump float;\n"
                    + "void main() {\n"
                    + " gl_FragColor = vec4(0.5, 0, 0, 1);\n"
                    + "}")

    //三角形vertex顶点的坐标
//    private val VERTEX = floatArrayOf(// in counterclockwise order:
//            0f, 1f, 0f, // top
//            -0.5f, -1f, 0f, // bottom left
//            1f, -1f, 0f)// bottom right

    private val VERTEX = floatArrayOf(0.5f,  0.5f, 0.0f, // top
            -0.5f, -0.5f, 0.0f, // bottom left
            0.5f, -0.5f, 0.0f ) // bottom right



    private var mVertexBuffer: FloatBuffer?=null

    //创建glsl程序
    private var mProgram:Int = 0
    private var mPositionHandle:Int = 0
    private var mColorHandle:Int = 0

    var color = floatArrayOf(1.0f, 1.0f, 1.0f, 1.0f) //白色


    /**
     * serfaceView创建
     * 可做初始化过程
     */
    override fun onSurfaceCreated(p0: GL10?, p1: EGLConfig?) {

        //创建gled背景
        GLES20.glClearColor(0.5f,0.5f,0.5f,1.0f)
        //将vertex顶点坐标转化为buffer
        mVertexBuffer=BufferUtil.floatToBuffer(VERTEX)

        //加载顶点着色器shader
        var vertexShader = Utils.loadShader(GLES20.GL_VERTEX_SHADER, VERTEX_SHADER)
        //加载片元着色器shader
        var fragmentShader = Utils.loadShader(GLES20.GL_FRAGMENT_SHADER, FRAGMENT_SHADER)

        //创建一个空的opengl程序
        mProgram=GLES20.glCreateProgram()
        //将顶点着色器加入到程序中
        GLES20.glAttachShader(mProgram,vertexShader)
        //将片元着色器加入到程序中
        GLES20.glAttachShader(mProgram,fragmentShader)

        //连接以及加入shader的着色器程序
        GLES20.glLinkProgram(mProgram)


    }

    /**
     * SurfaceView大小尺寸发生变化调用
     */
    override fun onSurfaceChanged(p0: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0,0,width,height)
    }

    /**
     * sufaceview绘制
     */
    override fun onDrawFrame(p0: GL10?) {
        //开始使用,将程序加入到opengles中
        GLES20.glUseProgram(mProgram)

        //获取顶点着色器的句柄(获取shader代码中的变量索引)
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition")
        //启用三角形顶点句柄
        GLES20.glEnableVertexAttribArray(mPositionHandle)

        //绑定三角形顶点坐标
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false,
                12, mVertexBuffer)

        //获取片元着色器的句柄
        mColorHandle=GLES20.glGetUniformLocation(mProgram,"vColor")
        //绘制三角形的颜色
        GLES20.glUniform4fv(mColorHandle, 1, color, 0)

        //绘制三角形
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, VERTEX.size)
        //禁止顶点数组的句柄
        GLES20.glDisableVertexAttribArray(mPositionHandle)

    }

}