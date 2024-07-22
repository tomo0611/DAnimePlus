package jp.tomo0611.danime.ui.details

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import jp.tomo0611.danime.R
import jp.tomo0611.danime.adapter.EpisodesAdapter
import jp.tomo0611.danime.adapter.RecentlyAiredEpisodesAdapter
import jp.tomo0611.danime.databinding.FragmentDetailsBinding
import jp.tomo0611.danime.model.AnimeEpisode
import jp.tomo0611.danime.model.AnimeTitle
import jp.tomo0611.danime.model.EpisodeContents
import jp.tomo0611.danime.model.Work
import jp.tomo0611.danime.ui.player.PlayerActivity


class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null

    private val binding get() = _binding!!

    // When requested, this adapter returns a DemoObjectFragment,
    // representing an object in the collection.
    private lateinit var detailsTabAdapter: DetailsTabAdapter
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (requireActivity().actionBar != null) {
            requireActivity().actionBar?.setDisplayHomeAsUpEnabled(true);
            requireActivity().actionBar?.setHomeButtonEnabled(true);
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val detailsViewModel =
            ViewModelProvider(this).get(DetailsViewModel::class.java)

        Log.d("DetailsFragment", "workId: ${arguments?.getString("workId")}")

        detailsViewModel.setWorkId(arguments?.getString("workId")!!)

        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        detailsViewModel.result.observe(viewLifecycleOwner) {
            Log.d("DetailsFragment", "result: $it")
            (requireActivity() as AppCompatActivity).supportActionBar?.title =
                it.result.titleContents.title.titleName

            Glide.with(this)
                .load(it.result.titleContents.title.indexImageUrl)
                .apply(
                    RequestOptions().transform(RoundedCorners(20)).diskCacheStrategy(
                        DiskCacheStrategy.ALL
                    )
                )
                .into(binding.detailsImageA)
        }

        detailsViewModel.episode_result.observe(viewLifecycleOwner) {
            Log.d("HomeFragment", "result: $it")

            detailsTabAdapter = DetailsTabAdapter(
                this,
                detailsViewModel.result.value!!.result.titleContents.title,
                it.result.titleContents.title.episodeContents
            )
            viewPager = requireView().findViewById(R.id.details_pager)
            viewPager.adapter = detailsTabAdapter

            val tabLayout = requireView().findViewById<TabLayout>(R.id.details_tab_layout)
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                if (position == 0) {
                    tab.text = "Episodes"
                } else {
                    tab.text = "Details"
                }
            }.attach()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class DetailsTabAdapter(
    fragment: Fragment,
    val anime_detail: AnimeTitle,
    val anime_episode: EpisodeContents
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        // Return a NEW fragment instance in createFragment(int).
        if (position == 0) {
            val fragment = DetailsEpisodesFragment()
            fragment.arguments = Bundle().apply {
                // The object is just an integer.
                putString("DATA", Gson().toJson(anime_episode))
            }
            return fragment
        } else {
            val fragment = DetailsDetailsFragment()
            fragment.arguments = Bundle().apply {
                // The object is just an integer.
                putString("DATA", Gson().toJson(anime_detail))
            }
            return fragment
        }
    }
}

class DetailsDetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_details_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.takeIf { it.containsKey("DATA") }?.apply {
            val data = Gson().fromJson(getString("DATA"), AnimeTitle::class.java)
            val regex =
                Regex("<\\s*font\\s*[^>]*>(.*?)<\\s*/\\s*font\\s*>", RegexOption.DOT_MATCHES_ALL)
            val textView: TextView = view.findViewById(R.id.details_description)
            textView.text = Html.fromHtml(
                regex.replace(data.titleDetail) { result -> result.groupValues[1] },
                Html.FROM_HTML_MODE_COMPACT
            )
        }
    }
}


class DetailsEpisodesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_details_episodes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.takeIf { it.containsKey("DATA") }?.apply {
            val data = Gson().fromJson(getString("DATA"), EpisodeContents::class.java)
            val adapter = EpisodesAdapter(requireActivity(), data.episodeList)
            val listview = view.findViewById<ListView>(R.id.episodes_list)
            listview.adapter = adapter
            listview.setOnItemClickListener { parent, view, position, id ->
                val item = parent.getItemAtPosition(position) as AnimeEpisode
                startActivity(Intent(context, PlayerActivity::class.java).apply {
                    putExtra("episodeId", item.videoEpisodeId)
                })
            }
        }
    }
}
