package opengl.es.graph

import android.opengl.GLSurfaceView
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import android.opengl.GLES20
import opengl.es.util.BufferUtil
import opengl.es.util.Utils



/**
 * 作者：jingjinggu on 2017/11/6 10:24
 * 邮箱：gujj512@163.com
 */
class Triangle : GLSurfaceView.Renderer {

    //顶点坐标缓冲
    private var vertexBuffer: FloatBuffer? = null

    //顶点着色器shader
    private var vertexShaderCode = "attribute vec4 vPosition;" +
            "void main() {" +
            "  gl_Position = vPosition;" +
            "}"

    //fragment片元着色器shader
    private var fragmentShaderCode = (
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}")

    //应用程序program
    private var mProgram: Int = 0

    //顶点坐标个数
    var COORDS_PER_VERTEX = 3
    //三角形坐标-逆时针
    var triangleCoords = floatArrayOf(0.5f, 0.5f, 0.0f, // top
            -0.5f, -0.5f, 0.0f, // bottom left
            0.5f, -0.5f, 0.0f  // bottom right
    )

    //顶点shader-positiv变量获取
    private var mPositionHandle: Int = 0
    //片元shader-vColor变量获取
    private var mColorHandle: Int = 0

    //顶点个数
    private var vertexCount = triangleCoords.size / COORDS_PER_VERTEX
    //顶点之间的偏移量
    private var vertexStride = COORDS_PER_VERTEX * 4 // 每个顶点四个字节

    private var mMatrixHandler: Int = 0

    //设置颜色，依次为红绿蓝和透明通道
    var color = floatArrayOf(1.0f, 1.0f, 1.0f, 1.0f)


    //初始化设置参数值
    fun init (){

        //将顶点坐标转化为buffer
        vertexBuffer = BufferUtil.floatToBuffer(triangleCoords)

        //加载顶点着色器
        var vertexShader = Utils.loadShader(GLES20.GL_VERTEX_SHADER,
                vertexShaderCode)
        //加载片元着色器
        var fragmentShader = Utils.loadShader(GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode)

        //创建一个空的OpenGLES程序
        mProgram = GLES20.glCreateProgram()
        //将顶点着色器加入到程序
        GLES20.glAttachShader(mProgram, vertexShader)
        //将片元着色器加入到程序中
        GLES20.glAttachShader(mProgram, fragmentShader)
        //连接到着色器程序
        GLES20.glLinkProgram(mProgram)

    }
    override fun onDrawFrame(p0: GL10?) {

        //将程序加入到OpenGLES2.0环境
        GLES20.glUseProgram(mProgram)

        //获取顶点着色器的vPosition成员句柄
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition")
        //启用三角形顶点的句柄
        GLES20.glEnableVertexAttribArray(mPositionHandle)
        //准备三角形的坐标数据
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer)
        //获取片元着色器的vColor成员的句柄
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor")
        //设置绘制三角形的颜色
        GLES20.glUniform4fv(mColorHandle, 1, color, 0)
        //绘制三角形
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount)
        //禁止顶点数组的句柄
        GLES20.glDisableVertexAttribArray(mPositionHandle)
        
    }

    override fun onSurfaceChanged(p0: GL10?, width: Int, height: Int) {

    }

    override fun onSurfaceCreated(p0: GL10?, p1: EGLConfig?) {
        init()
    }

}