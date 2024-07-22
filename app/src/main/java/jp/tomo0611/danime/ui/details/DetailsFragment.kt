package jp.tomo0611.danime.ui.details

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import jp.tomo0611.danime.R
import jp.tomo0611.danime.adapter.EpisodesAdapter
import jp.tomo0611.danime.adapter.RecentlyAiredEpisodesAdapter
import jp.tomo0611.danime.databinding.FragmentDetailsBinding
import jp.tomo0611.danime.model.AnimeEpisode
import jp.tomo0611.danime.model.Work
import jp.tomo0611.danime.ui.player.PlayerActivity


class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null

    private val binding get() = _binding!!

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
            (requireActivity() as AppCompatActivity).supportActionBar?.title = it.result.titleContents.title.titleName

            Glide.with(this)
                .load(it.result.titleContents.title.indexImageUrl)
                .apply(
                    RequestOptions().transform(RoundedCorners(20)).diskCacheStrategy(
                    DiskCacheStrategy.ALL))
                .into(binding.detailsImageA)
            val regex = Regex("<\\s*font\\s*[^>]*>(.*?)<\\s*/\\s*font\\s*>", RegexOption.DOT_MATCHES_ALL)
            binding.detailsDescription.text = Html.fromHtml(regex.replace(it.result.titleContents.title.titleDetail) { result -> result.groupValues[1] }, Html.FROM_HTML_MODE_COMPACT)
        }

        detailsViewModel.episode_result.observe(viewLifecycleOwner) {
            Log.d("HomeFragment", "result: $it")
            val adapter = this.context?.let { it1 -> EpisodesAdapter(it1, it.result.titleContents.title.episodeContents.episodeList) }
            binding.episodesList.adapter = adapter
            binding.episodesList.setOnItemClickListener { parent, view, position, id ->
                val item = parent.getItemAtPosition(position) as AnimeEpisode
                startActivity(Intent(context, PlayerActivity::class.java).apply {
                    putExtra("episodeId", item.videoEpisodeId)
                })
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}