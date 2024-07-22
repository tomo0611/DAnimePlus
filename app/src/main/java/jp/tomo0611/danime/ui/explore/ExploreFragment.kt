package jp.tomo0611.danime.ui.explore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import jp.tomo0611.danime.R
import jp.tomo0611.danime.adapter.GenreAdapter
import jp.tomo0611.danime.databinding.FragmentExploreBinding
import jp.tomo0611.danime.model.Genre
import jp.tomo0611.danime.model.Work

class ExploreFragment : Fragment() {

    private var _binding: FragmentExploreBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    // Genre List
    private val genreList = listOf<Genre>(
        Genre("11", "SF/ファンタジー"),
        Genre("12", "ロボット/メカ"),
        Genre("13", "アクション/バトル"),
        Genre("14", "コメディ/ギャグ"),
        Genre("15", "恋愛/ラブコメ"),
        Genre("23", "日常/ほのぼの"),
        Genre("16", "スポーツ/競技"),
        Genre("17", "ホラー/サスペンス/推理"),
        Genre("18", "歴史/戦記"),
        Genre("19", "戦争/ミリタリー"),
        Genre("20", "ドラマ/青春"),
        Genre("21", "キッズ/ファミリー"),
        Genre("22", "ショート"),
        Genre("25", "2.5次元舞台"),
        Genre("24", "ライブ/ラジオ/etc."),
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //val exploreViewModel =
        //    ViewModelProvider(this).get(ExploreViewModel::class.java)

        _binding = FragmentExploreBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val adapter = GenreAdapter(requireActivity(), genreList)
        binding.exploreGenreGridview.adapter = adapter
        binding.exploreGenreGridview.setOnItemClickListener { parent, view, position, id ->
            container?.findNavController()?.navigate(R.id.action_search, Bundle().apply {
                putString("genreId", genreList[position].genreId)
            })
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}