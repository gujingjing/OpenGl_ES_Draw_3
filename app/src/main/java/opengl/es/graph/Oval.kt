package opengl.es.graph

import android.opengl.GLSurfaceView
import android.opengl.Matrix
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import android.opengl.GLES20
import opengl.es.util.BufferUtil
import opengl.es.util.Utils.Companion.loadShader


/**
 *  * 作者：jingjinggu on 2017/11/8 21:26
 *  * 邮箱：gujj512@163.com
 *  
 */
class Oval : GLSurfaceView.Renderer {

    //顶点坐标缓冲
    var vertexBuffer: FloatBuffer? = null
    //顶点shader
    var vertexShaderCode = "attribute vec4 vPosition;" +
            "uniform mat4 vMatrix;" +
            "void main() {" +
            "  gl_Position = vMatrix*vPosition;" +
            "}"
    //片元shader
    var fragmentShaderCode = (
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}")
    //opengl应用程序
    var mProgram: Int = 0
    var COORDS_PER_VERTEX = 3
    //顶点位置shader变量
    var mPositionHandle: Int = 0
    //片元颜色shader-color变量
    var mColorHandle: Int = 0

    //相机投影
    var mViewMatrix = FloatArray(16)
    //透射投影
    var mProjectMatrix = FloatArray(16)
    //变换矩阵
    var mMVPMatrix = FloatArray(16)

    //顶点之间的偏移量
    var vertexStride = 0 // 每个顶点四个字节
    //顶点shader-变量
    var mMatrixHandler: Int = 0

    //半径
    var radius = 1.0f
    var n = 360  //切割份数

    //圆形的坐标点
    var shapePos: FloatArray? = null
    //z坐标的位置距离-后面做立体图形的时候比较好参考
    var height = 0.0f


    //设置颜色，依次为红绿蓝和透明通道
    var color = floatArrayOf(1.0f, 1.0f, 1.0f, 1.0f)


    override fun onDrawFrame(p0: GL10?) {

        //将程序加入到OpenGLES2.0环境
        GLES20.glUseProgram(mProgram)
        //获取变换矩阵vMatrix成员句柄
        mMatrixHandler = GLES20.glGetUniformLocation(mProgram, "vMatrix")
        //指定vMatrix的值
        GLES20.glUniformMatrix4fv(mMatrixHandler, 1, false, mMVPMatrix, 0)
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
        //绘制三角形-传入的顶点作为扇面绘制
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, (shapePos?.size?:0) / 3)
        //禁止顶点数组的句柄
        GLES20.glDisableVertexAttribArray(mPositionHandle)


    }

    override fun onSurfaceChanged(p0: GL10?, width: Int, height: Int) {

        //计算宽高比
        var ratio = (width / (height + 0.0)).toFloat()
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


    fun init() {
        //计算出所有的三角形坐标
        shapePos = createPositions()
        //将三角形坐标转化为buffer
        vertexBuffer = BufferUtil.floatToBuffer(shapePos)

        //获取顶点shader
        var vertexShader = loadShader(GLES20.GL_VERTEX_SHADER,
                vertexShaderCode)
        //获取片元shader
        var fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER,
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

    //创建圆形的坐标
    fun createPositions(): FloatArray {
        var data = ArrayList<Float>()
        //设置圆心坐标-0.0中心位置
        data.add(0.0f)
        data.add(0.0f)
        data.add(height)
        //每份占的比例
        var angDegSpan = 360f / n
        run {
            var i = 0f
            while (i < 360 + angDegSpan) {
                //根据角度计算三角形顶点的坐标
                data.add((radius * Math.sin(i * Math.PI / 180f)).toFloat())
                data.add((radius * Math.cos(i * Math.PI / 180f)).toFloat())
                data.add(height)
                i += angDegSpan
            }
        }
        var f = FloatArray(data.size)
        for (i in f.indices) {
            f[i] = data[i]
        }
        return f
    }


}
