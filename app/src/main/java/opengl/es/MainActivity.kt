package opengl.es

import android.opengl.GLSurfaceView
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import opengl.es.triangle.OpenGlRender

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)


        //创建一个GlsurfaceView
        var glsurfaceView= GLSurfaceView(this)
        glsurfaceView.setRenderer(OpenGlRender())

        setContentView(glsurfaceView)
    }
}
