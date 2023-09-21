package com.ibaevzz.shoppinglist.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ibaevzz.shoppinglist.adapters.ListsAdapter
import com.ibaevzz.shoppinglist.R
import com.ibaevzz.shoppinglist.activity.MainActivity
import com.ibaevzz.shoppinglist.constants.Constants
import com.ibaevzz.shoppinglist.databinding.AddListBinding
import com.ibaevzz.shoppinglist.databinding.BaseFragmentBinding
import com.ibaevzz.shoppinglist.di.ShoppingComponentProvider
import com.ibaevzz.shoppinglist.view_models.ListsViewModel
import com.ibaevzz.shoppinglist.view_models.ViewModelFactory
import kotlinx.coroutines.*
import javax.inject.Inject

class ListsFragment: Fragment() {

    private var isFirst = true
    private lateinit var job: Job
    private lateinit var binding: BaseFragmentBinding
    private lateinit var pref: SharedPreferences
    private var key: String = ""

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: ListsViewModel by viewModels {
        viewModelFactory
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ShoppingComponentProvider.appComponent().inject(this)
    }

    override fun onDetach() {
        job.cancel()
        super.onDetach()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        binding = BaseFragmentBinding.inflate(inflater)

        pref = (requireActivity() as MainActivity).getSharedPreferences(Constants.AUTH, Context.MODE_PRIVATE)
        key = pref.getString(Constants.AUTH, "")?:""

        if((requireActivity() as MainActivity).isNetworkAvailable()) {
            auth()
        }else{
            Toast.makeText(requireContext(), "Не удалось подключиться", Toast.LENGTH_SHORT).show()
            CoroutineScope(Dispatchers.IO).launch {
                while(true){
                    if((requireActivity() as MainActivity).isNetworkAvailable()){
                        withContext(Dispatchers.Main){
                            auth()
                        }
                        break
                    }
                }
            }
        }
        return binding.root
    }

    private fun getKey(){
        viewModel.getAuthKey().observe(this){ _key ->
            if(_key!=null){
                key = _key
                pref.edit().putString(Constants.AUTH, key).apply()
                makeUI()
                start()
            }else{
                Toast.makeText(requireContext(), "Не удалось получить ключ", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun auth(){
        if(key==""){
            getKey()
        }else{
            viewModel.auth(key).observe(this){ success ->
                if(success==false){
                    getKey()
                }else if(success==null){
                    Toast.makeText(requireContext(), "Не удалось авторизоваться", Toast.LENGTH_SHORT).show()
                }else{
                    start()
                    makeUI()
                }
            }
        }
    }

    private fun start(){
        job = CoroutineScope(Dispatchers.IO).launch {
            while(true){
                if((requireActivity() as MainActivity).isNetworkAvailable()){
                    withContext(Dispatchers.Main){
                        viewModel.getAllLists(key)
                    }
                }
                delay(1000)
            }
        }
    }

    private fun makeUI(){
        if((requireActivity() as MainActivity).isNetworkAvailable()){
            viewModel.getAllLists(key).observe(this){allLists->
                if(isFirst) {
                    binding.recycler.visibility = View.VISIBLE
                    binding.add.visibility = View.VISIBLE
                    binding.progress.visibility = View.INVISIBLE

                    binding.recycler.layoutManager = LinearLayoutManager(requireContext())
                    if (allLists?.success == true) {
                        binding.recycler.adapter = ListsAdapter(allLists, ::delete, ::onClick)
                    }

                    binding.add.setOnClickListener {
                        val dialogBinding = AddListBinding.inflate(layoutInflater)
                        val dialog = AlertDialog.Builder(requireContext())
                            .setTitle("Введите название")
                            .setView(dialogBinding.root)
                            .setPositiveButton("Добавить") { d, _ ->
                                val name = dialogBinding.name.text.toString()
                                if (name != "") {
                                    if ((requireActivity() as MainActivity).isNetworkAvailable()) {
                                        viewModel.createList(key, name)
                                        viewModel.getAllLists(key)
                                        binding.progress.visibility = View.VISIBLE
                                    } else {
                                        Toast.makeText(
                                            requireContext(),
                                            "Отсутствует интернет соединение",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    d.cancel()
                                }
                            }
                            .setNegativeButton("Назад") { d, _ ->
                                d.cancel()
                            }
                        dialog.create().show()
                    }
                }else{
                    binding.progress.visibility = View.INVISIBLE
                    if (allLists?.success == true) {
                        (binding.recycler.adapter as ListsAdapter).changeLists(allLists)
                    }
                }
                isFirst = false
            }
        }else{
            Toast.makeText(requireContext(), "Отсутствует интернет соединение", Toast.LENGTH_SHORT).show()
        }
    }

    private fun delete(id: Long){
        if((requireActivity() as MainActivity).isNetworkAvailable()) {
            viewModel.removeList(id)
            viewModel.getAllLists(key)
            binding.progress.visibility = View.VISIBLE
        }else{
            Toast.makeText(requireContext(), "Отсутствует интернет соединение", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onClick(listId: Long){
        val args = Bundle()
        args.putLong(Constants.LIST_ID, listId)
        isFirst = true
        findNavController().navigate(R.id.action_listsFragment_to_itemsFragment, args)
    }
}