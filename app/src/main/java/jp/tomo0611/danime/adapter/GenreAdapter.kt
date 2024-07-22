package jp.tomo0611.danime.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import jp.tomo0611.danime.R
import jp.tomo0611.danime.model.Genre

class GenreAdapter(context: Context, list: List<Genre>) :
    ArrayAdapter<Genre>(context, 0, list) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_genre, parent, false)
        }
        val data = getItem(position)

        view?.findViewById<TextView>(R.id.text_genre_title)?.apply { text = data?.genreName }

        return view!!
    }
}