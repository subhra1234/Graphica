package com.example.graphica.GraphView

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel

class GraphView{
    var maxX=10f
        private set
    var minX=-10f
        private set
    var maxY=10f
        private set
    var minY=-10f
        private set
    private var width=maxX-minX
    private var height=maxY-minY

    init {
        this.maxX=10f
        this.minX=-10f
        this.width=this.maxX-this.minX
        this.height=this.maxY-this.minY
    }
    fun setXs(xmax:Float,xmin:Float){
        this.maxX=xmax
        this.minX=xmin
        this.width=maxX-minX
    }

    @Composable
    fun GraphViewUI(modifier: Modifier = Modifier) {
        val viewmodel = viewModel<GraphV_ViewModel>()
        val textMeasurer= rememberTextMeasurer()
        Box(modifier = Modifier.fillMaxSize()){
            Canvas(modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTransformGestures { centroid, pan, zoom, rotation ->
                        var zum=viewmodel.zoom
                        zum += zoom / 100f
                        val factor=getXStepSize(zum)/getXStepSize(0f)
                        viewmodel.zoom=zum
                        if(factor==2f){
                            setXs(maxX*factor, minX*factor)
                        }
                    }
                }) {
                for(i in 0..19){
                    val x=minX+i*getXStepSize(viewmodel.zoom)
                    drawLine(Color.Black, mapToCanvas(Offset(x,minY), this.size)
                        , mapToCanvas(Offset(x, maxY),this.size), strokeWidth = 3f)
                    if(mapToCanvas(offset = Offset(x,2f),this.size).x<this.size.width){
                    drawText(textMeasurer,text = x.toString(), topLeft = mapToCanvas(offset = Offset(x,2f), size = this.size))}
                }
            }
        }
    }

    private fun mapToCanvas(offset: Offset, size: Size): Offset{
        return Offset((offset.x+this.width/2)*size.width/this.width,((this.height/2-offset.y)*size.height/this.height) )
    }

    private fun getXStepSize(zoom: Float):Float{
        val A=(this.maxX-this.minX)/20f
        return A*(1+sawTooth(zoom))
    }


    private fun sawTooth(x:Float): Float{
        if (x>1){
            return sawTooth(x-1)
        }
        else if (x<0){
            return sawTooth(x+1)
        }
        else return x
    }

}

@Preview(showBackground = true)
@Composable
private fun preview() {
    val gv=GraphView()
    gv.GraphViewUI()

}