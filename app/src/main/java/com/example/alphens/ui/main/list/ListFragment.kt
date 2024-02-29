package com.example.alphens.ui.main.list

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.alphens.R
import com.example.alphens.data.model.Note
import com.example.alphens.databinding.FragmentListBinding
import com.example.alphens.ui.auth.AuthViewModel
import com.example.alphens.ui.main.MainViewModel
import com.example.alphens.util.Resource
import com.example.alphens.util.hide
import com.example.alphens.util.show
import com.example.alphens.util.toast
import dagger.hilt.android.AndroidEntryPoint

private const val LIST = "list"

@AndroidEntryPoint
class ListFragment : Fragment() {

    val TAG: String = "ListFragment"
    private var list: String? = null

    private lateinit var binding: FragmentListBinding
    private val mainViewModel: MainViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()

    private val adapter by lazy {
        ListAdapter(
            onItemClicked = { pos, item ->
                findNavController().navigate(
                    R.id.action_listFragment_to_detailFragment,
                    Bundle().apply {
                        putParcelable("note", item)
                    }
                )
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        if (this::binding.isInitialized){
            return binding.root
        } else {
            binding = FragmentListBinding.inflate(layoutInflater)
            return binding.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observer()
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL)
        binding.rvNote.layoutManager = staggeredGridLayoutManager

        binding.rvNote.adapter = adapter
        binding.fabCreate.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_detailFragment)
        }

        authViewModel.getSession {
            mainViewModel.getNotes(it)
        }
    }

    private fun observer() {
        mainViewModel.notes.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Resource.Loading -> {
                    binding.pbList.show()
                }
                is Resource.Error -> {
                    binding.pbList.hide()
                    state.error?.let { toast(requireContext(), it) }
                }
                is Resource.Success -> {
                    binding.pbList.hide()
                    adapter.updateList(state.data.toMutableList())
                }
            }
        }
    }

    private fun showLogoutConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Logout")
            .setMessage("r u sure?")
            .setPositiveButton("Let me out") { _,_ ->
                authViewModel.logout {
                    findNavController().navigate(R.id.action_listFragment_to_nav_auth)
                }
            }
            .setNegativeButton("Nah, I'd stay", null)
            .show()
    }


    companion object {
        @JvmStatic
        fun newInstance(list: String) = ListFragment().apply {
            arguments = Bundle().apply {
                putString(LIST, list)
            }
        }
    }
}