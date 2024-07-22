package jp.tomo0611.danime.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import jp.tomo0611.danime.R
import jp.tomo0611.danime.model.AnimeWork

class AnimeAdapter (context: Context, list: List<AnimeWork>) :
    ArrayAdapter<AnimeWork>(context, 0, list) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_anime, parent, false)
        }
        val data = getItem(position)
        view?.findViewById<ImageView>(R.id.item_anime_thumbnail)?.let {
            Glide.with(context)
                .load(data?.workInfo?.mainKeyVisualPath)
                .centerCrop()
                .apply(
                    RequestOptions().transform(RoundedCorners(20)).diskCacheStrategy(
                        DiskCacheStrategy.ALL
                    )
                )
                .into(it)
        }
        view?.findViewById<TextView>(R.id.item_anime_work_favorite_count)
            ?.apply { text = "Likes: ${data?.workInfo?.favoriteCount}" }
        view?.findViewById<TextView>(R.id.item_anime_work_title)
            ?.apply { text = data?.workInfo?.workTitle }

        view?.findViewById<TextView>(R.id.text_episode_tag)?.visibility = View.GONE

        return view!!
    }
}