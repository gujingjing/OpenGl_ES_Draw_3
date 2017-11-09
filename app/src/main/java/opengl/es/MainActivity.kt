package opengl.es

import android.opengl.GLSurfaceView
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import gjj.retrofit.design.toast
import kotlinx.android.synthetic.main.activity_main.*
import opengl.es.graph.*
import opengl.es.graph.Oval
import opengl.es.util.Utils

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //判断是否支持2.0
        if(!Utils.supportGlEs20(this)){
            toast("GLES 2.0 not supported!")
            finish()
            return
        }

        //设置glsurfaceview的版本
        glsurfaceview.setEGLContextClientVersion(2)

        //三角形
//        glsurfaceview.setRenderer(Triangle())
        //等腰三角形
//        glsurfaceview.setRenderer(TriangleWithCamera())
        //彩色三角形
//        glsurfaceview.setRenderer(TriangleRenderColorFull())
        //正方形
//        glsurfaceview.setRenderer(TriangleSquare())
        //圆形
        glsurfaceview.setRenderer(Oval())











        /**
         * 渲染模式又两种
         * 1. RENDERMODE_WHEN_DIRTY
         * 这是懒惰渲染，需要手动调用    glsurfaceview.requestRender()
         *
         * 2.RENDERMODE_CONTINUOUSLY
         * 这种会不停的自动渲染
         */
        glsurfaceview.renderMode=GLSurfaceView.RENDERMODE_WHEN_DIRTY

    }

    override fun onPause() {
        super.onPause()
        glsurfaceview.onPause()
    }

    override fun onResume() {
        super.onResume()
        glsurfaceview.onResume()
    }
}
