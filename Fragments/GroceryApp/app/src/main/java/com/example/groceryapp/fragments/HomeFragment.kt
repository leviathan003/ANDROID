package com.example.groceryapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.groceryapp.MainActivity
import com.example.groceryapp.R
import com.example.groceryapp.adapters.NotesRVAdapter
import com.example.groceryapp.databinding.FragmentHomeBinding
import com.example.groceryapp.models.Notes
import com.example.groceryapp.viewmodels.NoteViewModel

class HomeFragment : Fragment(R.layout.fragment_home), SearchView.OnQueryTextListener,MenuProvider {

    private var homeBinding : FragmentHomeBinding? = null
    private val binding get() = homeBinding!!

    private lateinit var noteViewModel:NoteViewModel
    private lateinit var noteAdapter:NotesRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeBinding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost:MenuHost = requireActivity()
        menuHost.addMenuProvider(this,viewLifecycleOwner,Lifecycle.State.RESUMED)

        noteViewModel = (activity as MainActivity).viewModel
        setupHomeRV()

        binding.addNoteFab.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_addNoteFragment)
        }
    }

    private fun updteUI(note:List<Notes>?){
        if(note!=null){
            if(note.isNotEmpty()){
                binding.homeRecyclerView.visibility = View.VISIBLE
            }
            else{
                binding.homeRecyclerView.visibility = View.GONE
            }
        }
    }

    private fun setupHomeRV(){
        noteAdapter = NotesRVAdapter()
        binding.homeRecyclerView.apply {
            layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
            setHasFixedSize(true)
            adapter = noteAdapter
        }
        activity?.let{
            noteViewModel.getAllNotes().observe(viewLifecycleOwner){
                note->
                noteAdapter.differ.submitList(note)
                updteUI(note)
            }
        }
    }

    private fun searchNote(query:String?){
        val searchQuery = "%$query"
        noteViewModel.searchNote(searchQuery).observe(this){
            list->
            noteAdapter.differ.submitList(list)
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if(newText!=null){
            searchNote(newText)
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        homeBinding=null
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.home_menu,menu)

        val search = menu.findItem(R.id.searchMenu).actionView as SearchView
        search.isSubmitButtonEnabled = false
        search.setOnQueryTextListener(this)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return false
    }


}