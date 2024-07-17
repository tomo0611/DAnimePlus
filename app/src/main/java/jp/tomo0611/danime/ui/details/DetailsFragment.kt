package jp.tomo0611.danime.ui.details

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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

        detailsViewModel.text.observe(viewLifecycleOwner) {
            Log.d("DetailsFragment", "result: $it")
            binding.textDetails.text = it
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}