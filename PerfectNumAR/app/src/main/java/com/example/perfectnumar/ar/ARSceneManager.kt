package com.example.perfectnumar.ar

import com.google.ar.core.Anchor
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.Renderable
import com.google.ar.sceneform.ux.ArFragment
import com.example.perfectnumar.utils.Constants

/**
 * AR sahnesini yöneten ve sayıların yerleştirilmesini, ölçeklendirilmesini sağlayan sınıf
 */
class ARSceneManager(private val arFragment: ArFragment) {
    private val numberNodes = mutableListOf<Node>()
    private var onNumberTapListener: ((Int) -> Unit)? = null

    fun initialize() {
        arFragment.arSceneView.scene.addOnUpdateListener { frameTime ->
            if (numberNodes.isEmpty()) {
                placeNumbersInCircle()
            }
        }
    }

    fun setOnNumberTapListener(listener: (Int) -> Unit) {
        onNumberTapListener = listener
    }

    private fun placeNumbersInCircle() {
        val camera = arFragment.arSceneView.scene.camera
        val cameraPosition = camera.worldPosition
        val angleStep = 360f / Constants.NUMBERS_IN_CIRCLE

        for (i in 0 until Constants.NUMBERS_IN_CIRCLE) {
            val angle = Math.toRadians((angleStep * i).toDouble())
            val x = cameraPosition.x + Constants.AR_PLACEMENT_RADIUS * Math.cos(angle).toFloat()
            val z = cameraPosition.z + Constants.AR_PLACEMENT_RADIUS * Math.sin(angle).toFloat()

            createNumberNode(i, Vector3(x, cameraPosition.y, z))
        }
    }

    private fun createNumberNode(number: Int, position: Vector3) {
        ModelHelper.getNumberRenderable(number)?.let { renderable ->
            val node = Node().apply {
                setParent(arFragment.arSceneView.scene)
                localPosition = position
                this.renderable = renderable
                localScale = Vector3(
                    Constants.MODEL_SCALE_NORMAL,
                    Constants.MODEL_SCALE_NORMAL,
                    Constants.MODEL_SCALE_NORMAL
                )
                setOnTapListener { _, _ -> onNumberTapListener?.invoke(number) }
            }
            numberNodes.add(node)
        }
    }

    fun highlightNode(index: Int) {
        numberNodes.getOrNull(index)?.apply {
            localScale = Vector3(
                Constants.MODEL_SCALE_HIGHLIGHTED,
                Constants.MODEL_SCALE_HIGHLIGHTED,
                Constants.MODEL_SCALE_HIGHLIGHTED
            )
        }
    }

    fun resetNodeScale(index: Int) {
        numberNodes.getOrNull(index)?.apply {
            localScale = Vector3(
                Constants.MODEL_SCALE_NORMAL,
                Constants.MODEL_SCALE_NORMAL,
                Constants.MODEL_SCALE_NORMAL
            )
        }
    }

    fun showSuccessAnimation() {
        numberNodes.forEach { node ->
            node.localScale = Vector3(
                Constants.MODEL_SCALE_SUCCESS,
                Constants.MODEL_SCALE_SUCCESS,
                Constants.MODEL_SCALE_SUCCESS
            )
        }
    }

    fun resetAllNodes() {
        numberNodes.forEach { node ->
            node.localScale = Vector3(
                Constants.MODEL_SCALE_NORMAL,
                Constants.MODEL_SCALE_NORMAL,
                Constants.MODEL_SCALE_NORMAL
            )
        }
    }
}