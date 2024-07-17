package jp.tomo0611.danime.ui.details

import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import jp.tomo0611.danime.databinding.FragmentDetailsBinding


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
            binding.detailsDescription.text = Html.fromHtml(it.result.titleContents.title.titleDetail, Html.FROM_HTML_MODE_COMPACT)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}