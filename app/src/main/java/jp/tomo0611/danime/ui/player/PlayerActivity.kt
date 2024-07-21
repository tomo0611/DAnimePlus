package jp.tomo0611.danime.ui.player

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.HandlerThread
import android.provider.MediaStore
import android.util.Log
import android.view.PixelCopy
import android.view.SurfaceView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.SeekParameters
import androidx.media3.exoplayer.mediacodec.MediaCodecSelector
import androidx.media3.exoplayer.mediacodec.MediaCodecUtil
import androidx.media3.ui.PlayerView
import com.google.common.collect.ImmutableMap
import jp.tomo0611.danime.R
import kotlinx.coroutines.launch
import java.io.File


class PlayerActivity : AppCompatActivity() {
    @OptIn(UnstableApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_player)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        WindowCompat.setDecorFitsSystemWindows(window, false)
        val windowInsetsCompat = WindowInsetsControllerCompat(window, window.decorView)
        windowInsetsCompat.hide(WindowInsetsCompat.Type.statusBars())
        windowInsetsCompat.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        val episodeId = intent.getStringExtra("episodeId") ?: ""
        if(episodeId.isEmpty()) {
            finish()
            return
        }

        // .secureを除外するDecoderを追加
        val codecSelectorExcludeSecure =
            MediaCodecSelector { mimeType, requiresSecureDecoder, requiresTunnelingDecoder ->
                MediaCodecUtil.getDecoderInfos(
                    mimeType,
                    requiresSecureDecoder,
                    requiresTunnelingDecoder
                ).toMutableList().filter {
                    !it.name.endsWith(".secure")
                }.toMutableList()
            }

        val renderersFactory = DefaultRenderersFactory(this).apply {
            setMediaCodecSelector(codecSelectorExcludeSecure)
        }

        // Initialize ExoPlayer
        val player = ExoPlayer.Builder(this, renderersFactory).build()

        // Bind the player to the view.
        findViewById<PlayerView>(R.id.player_view).player = player
        findViewById<PlayerView>(R.id.player_view).setShowNextButton(false)
        findViewById<PlayerView>(R.id.player_view).setShowPreviousButton(false)
        findViewById<PlayerView>(R.id.player_view).setFullscreenButtonClickListener {
            takeScreenshotAndSave(
                this,
                player.currentPosition,
                findViewById(R.id.player_view),
                findViewById<PlayerView>(R.id.player_view).videoSurfaceView as SurfaceView
            )
        }


        val viewModel: PlayerViewModel by viewModels()
        viewModel.load(episodeId, this)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.result.observe(this@PlayerActivity) {
                    Log.d("PlayerActivity", "result: $it")

                    val token = viewModel.result_token.value

                    val mediaItem: MediaItem = MediaItem.Builder()
                        .setUri(viewModel.result.value?.data?.castContentUri?.replace("http://", "https://"))
                        .setMimeType(MimeTypes.APPLICATION_MPD)
                        .setDrmConfiguration(
                            MediaItem.DrmConfiguration.Builder(C.WIDEVINE_UUID)
                                .setLicenseUri("https://widevine.drmkeyserver.com/widevine")
                                .setMultiSession(true)
                                .setLicenseRequestHeaders(ImmutableMap.of("AcquireLicenseAssertion", token!!))
                                .build()
                        )
                        .build()

                    player.setMediaItem(mediaItem)
                    player.prepare()
                    player.playWhenReady = true
                    player.setSeekParameters(SeekParameters.PREVIOUS_SYNC)
                    player.play()
                }
            }
        }
    }

    @OptIn(UnstableApi::class)
    private fun takeScreenshotAndSave(
        context: Context,
        currentPosition: Long,
        playerView: PlayerView,
        surfaceView: SurfaceView
    ) {

        Log.d("Screenshot", "taken")

        playerView.hideController()

        surfaceView.setSecure(false)

        // Create a bitmap the size of the scene view.
        val textureViewBitmap = Bitmap.createBitmap(
            surfaceView.width, surfaceView.height,
            Bitmap.Config.ARGB_8888
        )
        // Create a handler thread to offload the processing of the image.
        val handlerThread = HandlerThread("PixelCopier")
        handlerThread.start()
        // Make the request to copy.
        PixelCopy.request(surfaceView, textureViewBitmap, { copyResult ->
            if (copyResult === PixelCopy.SUCCESS) {
                Log.e("ExoPlayerTopFragment#Screenshot", textureViewBitmap.toString())

                val result = Bitmap.createBitmap(textureViewBitmap.getWidth(), textureViewBitmap.getHeight(), textureViewBitmap.getConfig())
                val canvas = Canvas(result)
                canvas.drawBitmap(textureViewBitmap, 0f, 0f, null)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    // https://www.raywenderlich.com/10217168-preparing-for-scoped-storage
                    val collection =
                        MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
                    val dirDest = File(Environment.DIRECTORY_PICTURES, "D-Anime")
                    val date = System.currentTimeMillis()
                    val filename =
                        makeFileNameSafe("$date - hello (at=${currentPosition})")
                    val extension = "png"
                    val newImage = ContentValues().apply {
                        put(MediaStore.Images.Media.DISPLAY_NAME, "$filename.$extension")
                        put(MediaStore.MediaColumns.MIME_TYPE, "image/$extension")
                        put(MediaStore.MediaColumns.DATE_ADDED, date)
                        put(MediaStore.MediaColumns.DATE_MODIFIED, date)
                        put(MediaStore.MediaColumns.SIZE, textureViewBitmap.byteCount)
                        put(MediaStore.MediaColumns.WIDTH, textureViewBitmap.width)
                        put(MediaStore.MediaColumns.HEIGHT, textureViewBitmap.height)
                        put(MediaStore.MediaColumns.RELATIVE_PATH, "$dirDest${File.separator}")
                        put(MediaStore.Images.Media.IS_PENDING, 1)
                    }
                    Thread {
                        val newImageUri = context.contentResolver.insert(collection, newImage)
                        context.contentResolver.openOutputStream(newImageUri!!, "w").use {
                            if (it != null) {
                                result.compress(Bitmap.CompressFormat.PNG, 100, it)
                            }
                        }
                        newImage.clear()
                        newImage.put(MediaStore.Images.Media.IS_PENDING, 0)
                        context.contentResolver.update(newImageUri, newImage, null, null)

                        // show toast
                        Handler(context.mainLooper).post {
                            Toast.makeText(
                                context,
                                "Screenshot was saved to /Pictures/D-Anime/$filename.$extension",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }.start()
                }
            } else {
                if(copyResult == PixelCopy.ERROR_SOURCE_INVALID) {
                    val toast = Toast.makeText(
                        context,
                        "It is not possible to copy from the source. " +
                                "This can happen if the source is " +
                                "hardware-protected or destroyed.", Toast.LENGTH_LONG
                    )
                    toast.show()
                } else {
                    val toast = Toast.makeText(
                        context,
                        "Failed to copyPixels: $copyResult", Toast.LENGTH_LONG
                    )
                    toast.show()
                }
            }
            handlerThread.quitSafely()
        }, Handler(handlerThread.looper))

    }

    // https://gist.github.com/daichan4649/7112801
    private fun makeFileNameSafe(filename: String): String {
        return filename
            .replace("<", "＜")
            .replace(">", "")
            .replace(":", "：")
            .replace("*", "＊")
            .replace("?", "？")
            .replace("\"", "”")
            .replace("/", "／")
            .replace("\\", "/")
            .replace("|", "｜")
    }

}