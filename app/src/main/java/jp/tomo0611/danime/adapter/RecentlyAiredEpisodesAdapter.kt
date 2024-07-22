package jp.tomo0611.danime.adapter

import android.content.Context
import android.text.format.DateUtils
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
import jp.tomo0611.danime.model.Work
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.TimeZone


class RecentlyAiredEpisodesAdapter(context: Context, list: List<Work>) : ArrayAdapter<Work>(context, 0, list) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view  = convertView
        if (view == null) {
            view  = LayoutInflater.from(context).inflate(R.layout.item_episode, parent, false)
        }
        val data = getItem(position)

        // view?.findViewById<ImageView>(R.id.user_icon).apply { data?.icon }
        if(data?.workInfo?.scenes?.size!! > 0) {
            view?.findViewById<ImageView>(R.id.image_episode_thumbnail)?.let {
                Glide.with(context)
                    .load(data.workInfo.scenes[0].mainScenePath.replace("_3.png", "_1.png"))
                    .centerCrop()
                    .apply(RequestOptions().transform(RoundedCorners(20)).diskCacheStrategy(
                        DiskCacheStrategy.ALL))
                    .into(it)
            }
        }

        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"))
        try {
            val time: Long = sdf.parse(data?.workInfo?.latestPartPublicDate)?.getTime() ?: 0
            val now = System.currentTimeMillis()
            val ago =
                DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS)
            view?.findViewById<TextView>(R.id.text_published_at)?.apply { text = ago }
        } catch (e: ParseException) {
            view?.findViewById<TextView>(R.id.text_published_at)?.apply { text = "ERROR" }
            e.printStackTrace()
        }
        view?.findViewById<TextView>(R.id.text_work_title)?.apply { text = data?.workInfo?.workTitle }
        view?.findViewById<TextView>(R.id.text_episode_name)?.apply {
            text = if (data?.workInfo?.startPartDispNumber == data?.workInfo?.endPartDispNumber) {
                data?.workInfo?.startPartDispNumber
            } else {
                data?.workInfo?.endPartDispNumber
            }
        }

        // workIconsにrentalがあればレンタル作品
        if (data.workInfo.workIcons.contains("rental")) {
            view?.findViewById<TextView>(R.id.text_episode_tag)?.visibility = View.VISIBLE
        } else {
            view?.findViewById<TextView>(R.id.text_episode_tag)?.visibility = View.GONE
        }

        return view!!
    }
}