package com.noureldin.note.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import com.noureldin.note.main.MainActivity
import com.noureldin.note.R
import com.noureldin.note.databinding.FragmentNewNoteBinding
import com.noureldin.note.helper.toast
import com.noureldin.note.model.Note
import com.noureldin.note.mvvm.NoteViewModel

class NewNoteFragment : Fragment(R.layout.fragment_new_note) {
    private var _binding: FragmentNewNoteBinding? = null
    private val binding get() = _binding!!
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var mView: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewNoteBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        noteViewModel = (activity as MainActivity).noteViewModel
        mView = view
    }
    private fun saveNote(view: View){
        val noteTitle = binding.etNoteTitle.text.toString().trim()
        val noteBody = binding.etNoteBody.text.toString().trim()

        if (noteTitle.isNotEmpty()) {
            val note = Note(0, noteTitle, noteBody)

            noteViewModel.addNote(note)
            Snackbar.make(
                view, "Note saved successfully",
                Snackbar.LENGTH_SHORT
            ).show()
            view.findNavController().navigate(R.id.action_newNoteFragment_to_homeFragment)

        } else {
            activity?.toast("Please enter note title")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_new_note, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_save -> {
                saveNote(mView)
            }
        }

        return super.onOptionsItemSelected(item)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    override fun onResume() {
        super.onResume()
        enableBackArrowButton()
    }


    override fun onPause() {
        super.onPause()
        disableBackArrowButton()
    }


    private fun enableBackArrowButton() {
        val activity = requireActivity() as MainActivity
        val toolbar = activity.findViewById<Toolbar>(R.id.toolbar)

        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        activity.supportActionBar?.setDisplayShowHomeEnabled(true)

        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun disableBackArrowButton() {
        val activity = requireActivity() as MainActivity
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        activity.supportActionBar?.setDisplayShowHomeEnabled(false)
    }
}
