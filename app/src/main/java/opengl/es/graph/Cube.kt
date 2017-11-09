package opengl.es.graph

import android.opengl.GLSurfaceView
import java.nio.FloatBuffer
import java.nio.ShortBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import android.opengl.GLES20
import android.opengl.Matrix
import opengl.es.util.BufferUtil
import opengl.es.util.Utils



/**
 * 作者：jingjinggu on 2017/11/9 14:48
 * 邮箱：gujj512@163.com
 */
class Cube : GLSurfaceView.Renderer {

    //顶点缓冲
    private var vertexBuffer: FloatBuffer? = null
    //颜色缓冲
    private var colorBuffer: FloatBuffer? = null
    //绘制顺序缓冲
    private var indexBuffer: ShortBuffer? = null

    //顶点shader
    private var vertexShaderCode = "attribute vec4 vPosition;" +
            "uniform mat4 vMatrix;" +
            "varying  vec4 vColor;" +
            "attribute vec4 aColor;" +
            "void main() {" +
            "  gl_Position = vMatrix*vPosition;" +
            "  vColor=aColor;" +
            "}"

    //片元shader
    private var fragmentShaderCode = (
            "precision mediump float;" +
                    "varying vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}")

    //gl程序
    private var mProgram: Int = 0
    //没三个值代表一个顶点坐标
    var COORDS_PER_VERTEX = 3
    //正方体坐标-先正面再后面-都是从左上开始0-1-2-3  4-5-6-7
    var cubePositions = floatArrayOf(-1.0f, 1.0f, 1.0f, //正面左上0
            -1.0f, -1.0f, 1.0f, //正面左下1
            1.0f, -1.0f, 1.0f, //正面右下2
            1.0f, 1.0f, 1.0f, //正面右上3
            -1.0f, 1.0f, -1.0f, //反面左上4
            -1.0f, -1.0f, -1.0f, //反面左下5
            1.0f, -1.0f, -1.0f, //反面右下6
            1.0f, 1.0f, -1.0f)//反面右上7
    //绘制的顺序-
    var index = shortArrayOf(6, 7, 4, 6, 4, 5, //后面
            6, 3, 7, 6, 2, 3, //右面
            6, 5, 1, 6, 1, 2, //下面
            0, 3, 2, 0, 2, 1, //正面
            0, 1, 5, 0, 5, 4, //左面
            0, 7, 3, 0, 4, 7)//上面

    //颜色
    var color = floatArrayOf(0f, 1f, 0f, 1f,
            0f, 1f, 0f, 1f,
            0f, 1f, 0f, 1f,
            0f, 1f, 0f, 1f,
            1f, 0f, 0f, 1f,
            1f, 0f, 0f, 1f,
            1f, 0f, 0f, 1f,
            1f, 0f, 0f, 1f)

    //position变量
    private var mPositionHandle: Int = 0
    //color眼变量
    private var mColorHandle: Int = 0

    //相机投影
    private var mViewMatrix = FloatArray(16)
    //透射投影
    private var mProjectMatrix = FloatArray(16)
    //变换矩阵
    private var mMVPMatrix = FloatArray(16)
    //变换矩阵变量
    private var mMatrixHandler: Int = 0

    //顶点个数-总数除以每个坐标几个元素
    private var vertexCount = cubePositions.size / COORDS_PER_VERTEX
    //顶点之间的偏移量
    private var vertexStride = COORDS_PER_VERTEX * 4 // 每个顶点四个字节



    override fun onDrawFrame(p0: GL10?) {
        //清除深度缓存和颜色缓存
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
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
        GLES20.glVertexAttribPointer(mPositionHandle, 3,
                GLES20.GL_FLOAT, false,
                0, vertexBuffer)
        //获取片元着色器的vColor成员的句柄
        mColorHandle = GLES20.glGetAttribLocation(mProgram, "aColor")
        //设置绘制三角形的颜色
//        GLES20.glUniform4fv(mColorHandle, 2, color, 0);
        GLES20.glEnableVertexAttribArray(mColorHandle)
        GLES20.glVertexAttribPointer(mColorHandle, 4,
                GLES20.GL_FLOAT, false,
                0, colorBuffer)
        //索引法绘制正方体
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, index.size, GLES20.GL_UNSIGNED_SHORT, indexBuffer)
        //禁止顶点数组的句柄
        GLES20.glDisableVertexAttribArray(mPositionHandle)

    }

    override fun onSurfaceChanged(p0: GL10?, width: Int, height: Int) {

        //计算宽高比
        var ratio = (width / (height+0.0)).toFloat()
        //设置透视投影
        Matrix.frustumM(mProjectMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 20f)
        //设置相机位置
        Matrix.setLookAtM(mViewMatrix, 0, 5.0f, 5.0f, 10.0f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)
        //计算变换矩阵
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectMatrix, 0, mViewMatrix, 0)
    }

    override fun onSurfaceCreated(p0: GL10?, p1: EGLConfig?) {
        init()
    }

    fun init(){

        //获取顶点坐标缓冲
        vertexBuffer = BufferUtil.floatToBuffer(cubePositions)
        //获取颜色缓冲
        colorBuffer = BufferUtil.floatToBuffer(color)
        //获取绘制顺序缓冲
        indexBuffer = BufferUtil.shortToBuffer(index)

        //顶点shader
        var vertexShader = Utils.loadShader(GLES20.GL_VERTEX_SHADER,
                vertexShaderCode)
        //片元shader
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

        //开启深度测试
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
    }









}