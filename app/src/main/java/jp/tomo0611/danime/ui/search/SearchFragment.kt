package jp.tomo0611.danime.ui.search

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import jp.tomo0611.danime.R
import jp.tomo0611.danime.adapter.AnimeAdapter
import jp.tomo0611.danime.databinding.FragmentSearchBinding
import jp.tomo0611.danime.model.AnimeWork

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (requireActivity().actionBar != null) {
            requireActivity().actionBar?.setDisplayHomeAsUpEnabled(true)
            requireActivity().actionBar?.setHomeButtonEnabled(true)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val searchViewModel =
            ViewModelProvider(this).get(SearchViewModel::class.java)

        Log.d("SearchFragment", "genreId: ${arguments?.getString("genreId")}")

        searchViewModel.setGenreId(arguments?.getString("genreId")!!)

        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root
        searchViewModel.result.observe(viewLifecycleOwner) {
            Log.d("SearchFragment", "result: $it")
            val listview = binding.listSearchResult
            val adapter = AnimeAdapter(requireActivity(), it.data.workList)
            listview.adapter = adapter
            listview.setOnItemClickListener { parent, view, position, id ->
                val item = parent.getItemAtPosition(position) as AnimeWork
                Log.d("SearchFragment#OnClick", "item: $item")
                requireActivity().findNavController(R.id.nav_host_fragment_activity_main).navigate(R.id.action_open_details, Bundle().apply {
                    putString("workId", item.workId)
                })
            }
        }

        return root
    }
}