package jp.tomo0611.danime.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import jp.tomo0611.danime.R
import jp.tomo0611.danime.adapter.RecentlyAiredEpisodesAdapter
import jp.tomo0611.danime.databinding.FragmentHomeBinding
import jp.tomo0611.danime.model.Work

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        homeViewModel.result.observe(viewLifecycleOwner) {
            Log.d("HomeFragment", "result: $it")
            val adapter = this.context?.let { it1 -> RecentlyAiredEpisodesAdapter(it1, it.data.workList) }
            binding.listViewRecentlyAiredEpisodes.adapter = adapter
            binding.listViewRecentlyAiredEpisodes.setOnItemClickListener { parent, view, position, id ->
                val item = parent.getItemAtPosition(position) as Work
                container?.findNavController()?.navigate(R.id.action_open_details, Bundle().apply {
                    putString("workId", item.workId)
                })

                Log.d("HomeFragment", "item: $item")
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}