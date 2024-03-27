package com.noureldin.note.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.noureldin.note.database.NoteDatabase
import com.noureldin.note.databinding.ActivityMainBinding
import com.noureldin.note.mvvm.NoteViewModel
import com.noureldin.note.repository.NoteRepository
import com.noureldin.note.mvvm.NoteViewModelProviderFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var noteViewModel: NoteViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        setUpViewModel()
    }
    private fun setUpViewModel() {
        val noteRepository = NoteRepository(
            NoteDatabase(this)
        )

        val viewModelProviderFactory =
            NoteViewModelProviderFactory(
                application, noteRepository
            )

        noteViewModel = ViewModelProvider(
            this,
            viewModelProviderFactory
        ).get(NoteViewModel::class.java)
    }
}