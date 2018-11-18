package com.secretbiology.stickertest

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.util.Pair
import android.view.View
import android.widget.ProgressBar
import com.facebook.drawee.backends.pipeline.Fresco
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.ref.WeakReference
import java.util.*

class MainActivity : BaseActivity() {

    private lateinit var progressBar: ProgressBar
    private lateinit var loadListAsyncTask: LoadListAsyncTask

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fresco.initialize(this)
        setContentView(R.layout.activity_main)
        progressBar = findViewById(R.id.entry_activity_progress)
        loadListAsyncTask = LoadListAsyncTask(this)
        loadListAsyncTask.execute()
    }

    private fun showErrorMessage(errorMessage: String) {
        progressBar.visibility = View.GONE
        Log.e("MainActivity", "error fetching sticker packs, $errorMessage")
        error_message.text = getString(R.string.error_message, errorMessage)
    }


    override fun onDestroy() {
        super.onDestroy()
        if (!loadListAsyncTask.isCancelled) {
            loadListAsyncTask.cancel(true)
        }
    }

    private fun showStickerPack(stickerPackList: ArrayList<StickerPack>) {
        progressBar.visibility = View.GONE
        if (stickerPackList.size > 1) {
            val intent = Intent(this, StickerPackListActivity::class.java)
            intent.putParcelableArrayListExtra(StickerPackListActivity.EXTRA_STICKER_PACK_LIST_DATA, stickerPackList)
            startActivity(intent)
            finish()
            overridePendingTransition(0, 0)
        } else {
            val intent = Intent(this, StickerPackDetailsActivity::class.java)
            intent.putExtra(StickerPackDetailsActivity.EXTRA_SHOW_UP_BUTTON, false)
            intent.putExtra(StickerPackDetailsActivity.EXTRA_STICKER_PACK_DATA, stickerPackList[0])
            startActivity(intent)
            finish()
            overridePendingTransition(0, 0)
        }
    }

    internal class LoadListAsyncTask(activity: MainActivity) :
        AsyncTask<Void, Void, Pair<String, ArrayList<StickerPack>>>() {
        private val contextWeakReference: WeakReference<MainActivity> = WeakReference<MainActivity>(activity)

        override fun doInBackground(vararg voids: Void): Pair<String, ArrayList<StickerPack>> {
            val stickerPackList: ArrayList<StickerPack>
            try {
                val context = contextWeakReference.get()
                if (context != null) {
                    stickerPackList = StickerPackLoader.fetchStickerPacks(context)
                    if (stickerPackList.size == 0) {
                        return Pair<String, ArrayList<StickerPack>>("could not find any packs", null)
                    }
                    return Pair<String, ArrayList<StickerPack>>(null, stickerPackList)
                } else {
                    return Pair<String, ArrayList<StickerPack>>("could not fetch sticker packs", null)
                }
            } catch (e: Exception) {
                Log.e("EntryActivity", "error fetching sticker packs", e)
                return Pair<String, ArrayList<StickerPack>>(e.message, null)
            }

        }

        override fun onPostExecute(stringListPair: Pair<String, ArrayList<StickerPack>>) {

            val entryActivity = contextWeakReference.get()
            if (entryActivity != null) {
                if (stringListPair.first != null) {
                    entryActivity.showErrorMessage(stringListPair.first)
                } else {
                    entryActivity.showStickerPack(stringListPair.second)
                }
            }
        }
    }
}
