package opengl.es.triangle

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import opengl.es.util.BufferUtil
import opengl.es.util.Utils
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * 作者：jingjinggu on 2017/11/3 14:43
 * 邮箱：gujj512@163.com
 */
class MyRender :GLSurfaceView.Renderer {

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
    private val VERTEX = floatArrayOf(// in counterclockwise order:
            0f, 1f, 0f, // top
            -0.5f, -1f, 0f, // bottom left
            1f, -1f, 0f)// bottom right

    //顶点坐标缓冲
    private var mVertexBuffer:FloatBuffer?=null

    //创建glsl程序
    private var mProgram:Int = 0
    private var mPositionHandle:Int = 0

    constructor(){
        //初始化vertexBuffer-数组转化buffer
        mVertexBuffer=BufferUtil.floatToBuffer(VERTEX)

    }

    override fun onSurfaceChanged(p0: GL10?, width: Int, height: Int) {

        GLES20.glViewport(0,0,width,height)
    }

    override fun onSurfaceCreated(p0: GL10?, p1: EGLConfig?) {

        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f)

        //创建glsl
        mProgram = GLES20.glCreateProgram()
        //加载shader
        var vertexShader = Utils.loadShader(GLES20.GL_VERTEX_SHADER, VERTEX_SHADER)
        var fragmentShader = Utils.loadShader(GLES20.GL_FRAGMENT_SHADER, FRAGMENT_SHADER)
        //attach shader
        GLES20.glAttachShader(mProgram, vertexShader)
        GLES20.glAttachShader(mProgram, fragmentShader)
        //连接glsl
        GLES20.glLinkProgram(mProgram)
        //使用glsl
        GLES20.glUseProgram(mProgram)

        //获取shader代码中的变量索引
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition")

        //启用vertex顶点坐标
        GLES20.glEnableVertexAttribArray(mPositionHandle)
        //绑定vertex顶点坐标
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false,
                12, mVertexBuffer)



    }


    override fun onDrawFrame(p0: GL10?) {

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3)
    }
}