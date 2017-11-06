package opengl.es.graph

import android.opengl.GLSurfaceView
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import android.opengl.GLES20
import android.opengl.Matrix
import opengl.es.util.BufferUtil
import opengl.es.util.Utils





/**
 * 作者：jingjinggu on 2017/11/6 10:47
 * 邮箱：gujj512@163.com
 */
class TriangleWithCamera : GLSurfaceView.Renderer {

    //顶点坐标转换的buffer
    private var vertexBuffer: FloatBuffer? = null
    //顶点坐标shader-修改-变换矩阵和坐标相乘
    private var vertexShaderCode = "attribute vec4 vPosition;" +
            "uniform mat4 vMatrix;" +
            "void main() {" +
            "  gl_Position = vMatrix*vPosition;" +
            "}"
    //fragment-片元着色器shader
    private var fragmentShaderCode = (
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}")


    //应用程序
    private var mProgram: Int = 0
    //顶点坐标
    var COORDS_PER_VERTEX = 3
    //顶点坐标数组
    var triangleCoords = floatArrayOf(0.5f, 0.5f, 0.0f, // top
            -0.5f, -0.5f, 0.0f, // bottom left
            0.5f, -0.5f, 0.0f  // bottom right
    )
    //顶点shader-变量position
    private var mPositionHandle: Int = 0
    //片元shader-colir
    private var mColorHandle: Int = 0
    //相机投影位置
    private var mViewMatrix = FloatArray(16)
    //投射投影位置
    private var mProjectMatrix = FloatArray(16)
    //变换矩阵位置
    private var mMVPMatrix = FloatArray(16)

    //顶点个数
    private var vertexCount = triangleCoords.size / COORDS_PER_VERTEX
    //顶点之间的偏移量
    private var vertexStride = COORDS_PER_VERTEX * 4 // 每个顶点四个字节

    private var mMatrixHandler: Int = 0

    //设置颜色，依次为红绿蓝和透明通道
    var color = floatArrayOf(1.0f, 1.0f, 1.0f, 1.0f)


    //初始化程序
    fun init(){

        vertexBuffer = BufferUtil.floatToBuffer(triangleCoords)
        var vertexShader = Utils.loadShader(GLES20.GL_VERTEX_SHADER,
                vertexShaderCode)
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
        //获取变换矩阵vMatrix成员句柄
        mMatrixHandler= GLES20.glGetUniformLocation(mProgram,"vMatrix")
        //指定vMatrix的值
        GLES20.glUniformMatrix4fv(mMatrixHandler,1,false,mMVPMatrix,0)

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
        //计算宽高比
        var ratio = (width / (height+0.0)).toFloat()
        //设置透视投影
        Matrix.frustumM(mProjectMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 7f)
        //设置相机位置
        Matrix.setLookAtM(mViewMatrix, 0, 0f, 0f, 7.0f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)
        //计算变换矩阵
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectMatrix, 0, mViewMatrix, 0)
    }

    override fun onSurfaceCreated(p0: GL10?, p1: EGLConfig?) {
        init()
    }
}