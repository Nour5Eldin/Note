package com.noureldin.note.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.noureldin.note.main.MainActivity
import com.noureldin.note.R
import com.noureldin.note.databinding.FragmentUpdateNoteBinding
import com.noureldin.note.helper.toast
import com.noureldin.note.model.Note
import com.noureldin.note.mvvm.NoteViewModel

class UpdateNoteFragment : Fragment(R.layout.fragment_update_note) {
    private var _binding: FragmentUpdateNoteBinding? = null
    private val binding get() = _binding!!

    private val args: UpdateNoteFragmentArgs by navArgs()
    private lateinit var currentNote: Note
    private lateinit var noteViewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpdateNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        noteViewModel = (activity as MainActivity).noteViewModel
        currentNote = args.note!!
        binding.etNoteBodyUpdate.setText(currentNote.noteBody)
        binding.etNoteTitleUpdate.setText(currentNote.noteTitle)

        binding.fabDone.setOnClickListener {
            val title = binding.etNoteTitleUpdate.text.toString().trim()
            val body = binding.etNoteBodyUpdate.text.toString().trim()

            if (title.isNotEmpty()) {
                val note = Note(currentNote.id, title, body)
                noteViewModel.updateNote(note)

                view.findNavController().navigate(R.id.action_updateNoteFragment_to_homeFragment)

            } else {
                activity?.toast("Enter a note title please")
            }
        }
    }

    private fun deleteNote() {
        val dialogView = LayoutInflater.from(activity).inflate(R.layout.dialog_delete, null)

        val bottomSheetDialog = activity?.let { BottomSheetDialog(it) }
        bottomSheetDialog?.setContentView(dialogView)
        val cancelButton = dialogView.findViewById<AppCompatButton>(R.id.no_btn)
        val deleteButton = dialogView.findViewById<AppCompatButton>(R.id.yes_btn)

        cancelButton.setOnClickListener {
            bottomSheetDialog?.dismiss()
        }

        deleteButton.setOnClickListener {
            noteViewModel.deleteNote(currentNote)
            view?.findNavController()?.navigate(
                R.id.action_updateNoteFragment_to_homeFragment
            )
            bottomSheetDialog?.dismiss()
        }

        bottomSheetDialog?.show()
    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_update_note, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_delete -> {
                deleteNote()
            }
        }

        return super.onOptionsItemSelected(item)
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


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}