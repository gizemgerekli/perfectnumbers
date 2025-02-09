package com.example.perfectnumar.ar

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.ar.sceneform.rendering.ModelRenderable
import com.example.perfectnumar.utils.Constants

object ModelHelper {
    private val renderables = mutableMapOf<Int, ModelRenderable>()

    fun loadModels(context: Context, onComplete: () -> Unit) {
        var loadedCount = 0

        for (i in 0..9) {
            ModelRenderable.builder()
                .setSource(context, Uri.parse("models/number_${i}.glb"))
                .build()
                .thenAccept { renderable ->
                    renderables[i] = renderable
                    loadedCount++
                    if (loadedCount == Constants.NUMBERS_IN_CIRCLE) onComplete()
                }
                .exceptionally {
                    Log.e("ModelHelper", "Model yükleme hatası: $it")
                    null
                }
        }
    }

    fun getNumberRenderable(number: Int): ModelRenderable? = renderables[number]
}