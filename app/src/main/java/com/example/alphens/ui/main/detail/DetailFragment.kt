package com.example.alphens.ui.main.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.alphens.data.model.Note
import com.example.alphens.databinding.FragmentDetailBinding
import com.example.alphens.ui.auth.AuthViewModel
import com.example.alphens.ui.main.MainViewModel
import com.example.alphens.util.Resource
import com.example.alphens.util.hide
import com.example.alphens.util.show
import com.example.alphens.util.toast
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date

@AndroidEntryPoint
class DetailFragment : Fragment() {

    val TAG: String = "DetailFragment"
    private lateinit var binding: FragmentDetailBinding
    private val mainViewModel: MainViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()
    private var isEdit = false
    private var note: Note? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        if (this::binding.isInitialized) {
            return binding.root
        } else {
            binding = FragmentDetailBinding.inflate(layoutInflater)
            return binding.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateUi()
        observer()
    }

    private fun observer() {
        mainViewModel.create.observe(viewLifecycleOwner) {state ->
            when(state) {
                is Resource.Loading -> {
                    binding.pbDetail.show()
                }
                is Resource.Error -> {
                    binding.pbDetail.hide()
                    state.error?.let { toast(requireContext(), it) }
                }
                is Resource.Success -> {
                    binding.pbDetail.hide()
                    toast(requireContext(), state.data.second)
                    note = state.data.first
                    isMakeEnabledUi(false)
                    binding.done.hide()
                    binding.delete.hide()
                    binding.edit.show()
                }
                else -> {
                    toast(requireContext(), "nuts!!")
                }
            }
        }
        mainViewModel.update.observe(viewLifecycleOwner) {state ->
            when(state) {
                is Resource.Loading -> {
                    binding.pbDetail.show()
                }
                is Resource.Error -> {
                    binding.pbDetail.hide()
                    state.error?.let { toast(requireContext(), it) }
                }
                is Resource.Success -> {
                    binding.pbDetail.hide()
                    //toast(requireContext(), state.data)
                    binding.done.hide()
                    binding.edit.show()
                    isMakeEnabledUi(false)
                }
                else -> {
                    toast(requireContext(), "nuts!!")
                }
            }
        }
        mainViewModel.delete.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Resource.Loading -> {
                    binding.pbDetail.show()
                }
                is Resource.Error -> {
                    binding.pbDetail.hide()
                    state.error?.let { toast(requireContext(), it) }
                }
                is Resource.Success -> {
                    binding.pbDetail.hide()
                    toast(requireContext(), state.data)
                    findNavController().navigateUp()
                }
                else -> {
                    toast(requireContext(), "nuts!!")
                }
            }
        }
    }

    private fun isMakeEnabledUi(isDisable: Boolean = false) {
        binding.title.isEnabled = isDisable
        binding.date.isEnabled = isDisable
        binding.description.isEnabled = isDisable
    }

    private fun updateUi() {
        val sdf = SimpleDateFormat("dd MMM yyyy . hh:mm a")
        note = arguments?.getParcelable("note")
        note?.let { note ->
            binding.title.setText(note.title)
            binding.date.setText(sdf.format(note.date))
            binding.description.setText(note.description)
            binding.done.hide()
            binding.edit.show()
            binding.delete.show()
            isMakeEnabledUi(false)
        } ?: run {
            binding.title.setText("")
            binding.date.setText(sdf.format(Date()))
            binding.description.setText("")
            binding.done.hide()
            binding.edit.hide()
            binding.delete.hide()
            isMakeEnabledUi(true)
        }
        binding.back.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.title.setOnClickListener {
            isMakeEnabledUi(true)
        }
        binding.description.setOnClickListener {
            isMakeEnabledUi(true)
        }
        binding.delete.setOnClickListener {
            note?.let { mainViewModel.deleteNote(it) }
        }
        binding.edit.setOnClickListener {
            isMakeEnabledUi(true)
            binding.done.show()
            binding.edit.hide()
            binding.title.requestFocus()
        }
        binding.done.setOnClickListener {
            if (validation()) {
                onDonePressed()
            }
        }
        binding.title.doAfterTextChanged {
            binding.done.show()
            binding.edit.hide()
        }
        binding.description.doAfterTextChanged {
            binding.done.show()
            binding.edit.hide()
        }
    }


    private fun validation(): Boolean {
        var isValid = true
        if (binding.title.text.toString().isNullOrEmpty()) {
            isValid = true
        }
        if (binding.description.text.toString().isNullOrEmpty()) {
            isValid = false
            toast(requireContext(), "missing")
        }
        return isValid
    }

    private fun getNote(): Note {
        return Note(
            id = note?.id ?: "",
            title = binding.title.text.toString(),
            description = binding.description.text.toString(),
            date = Date()
        ).apply {
            authViewModel.getSession {
                this.user_id = it?.id ?: ""
            }
        }
    }

    private fun onDonePressed() {
        if (note == null) {
            mainViewModel.createNote(getNote())
        } else {
            mainViewModel.updateNote(getNote())
        }
    }

}