package com.example.cameraxmachinelearningkitandroidgit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.camera.core.CameraSelector
import androidx.camera.core.CameraSelector.LENS_FACING_BACK
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.core.UseCaseGroup
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.concurrent.futures.await
import com.google.mlkit.nl.entityextraction.EntityExtractor
import com.google.mlkit.vision.text.TextRecognizer
import java.util.concurrent.Executor

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

        }
    }
}
    @Composable
    fun CameraView(
        imageAnalysis: ImageAnalysis,
        preview: Preview,
        executor: Executor,
        textRecognizer: TextRecognizer,
        entityExtractor: EntityExtractor,
        callable: () -> Unit

    ) {
        val context = LocalContext.current
        val lifecycle = LocalLifecycleOwner.current

        val previewView = remember {
            PreviewView(context)
        }

        LaunchedEffect(key1 = Unit) {

            val cameraProvider = ProcessCameraProvider.getInstance(context).await()
            val cameraSelector = CameraSelector.Builder().requireLensFacing(LENS_FACING_BACK)
                .build()   // import from androidx.camera.core...

            val useCaseGroup = UseCaseGroup.Builder()
                .addUseCase(imageAnalysis)
                .addUseCase(preview)
                .apply {
                    previewView.viewPort?.let { setViewPort(it) }
                }
                .build()

            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(lifecycle, cameraSelector, useCaseGroup)

            preview.setSurfaceProvider(previewView.surfaceProvider)
    }

    AndroidView(modifier = Modifier.fillMaxSize(), factory = { previewView })
}
