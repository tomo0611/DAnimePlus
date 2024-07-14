package jp.tomo0611.danime.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import jp.tomo0611.danime.R
import jp.tomo0611.danime.model.Work

class RecentlyAiredEpisodesAdapter(context: Context, list: List<Work>) : ArrayAdapter<Work>(context, 0, list) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view  = convertView
        if (view == null) {
            view  = LayoutInflater.from(context).inflate(R.layout.item_episode, parent, false)
        }
        val data = getItem(position)

        // view?.findViewById<ImageView>(R.id.user_icon).apply { data?.icon }
        view?.findViewById<TextView>(R.id.text_work_title)?.apply { text = data?.workInfo?.workTitle }
        view?.findViewById<TextView>(R.id.text_published_at)?.apply { text = data?.workInfo?.latestPartPublicDate }
        view?.findViewById<TextView>(R.id.text_episode_name)?.apply {
            text = if (data?.workInfo?.startPartDispNumber == data?.workInfo?.endPartDispNumber) {
                data?.workInfo?.startPartDispNumber
            } else {
                data?.workInfo?.endPartDispNumber
            }
        }
        return view!!
    }
}