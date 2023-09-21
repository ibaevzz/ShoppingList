package com.ibaevzz.shoppinglist.fragments

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ibaevzz.shoppinglist.adapters.ItemsAdapter
import com.ibaevzz.shoppinglist.R
import com.ibaevzz.shoppinglist.activity.MainActivity
import com.ibaevzz.shoppinglist.constants.Constants
import com.ibaevzz.shoppinglist.databinding.AddItemBinding
import com.ibaevzz.shoppinglist.databinding.BaseFragmentBinding
import com.ibaevzz.shoppinglist.di.ShoppingComponentProvider
import com.ibaevzz.shoppinglist.network.models.Item
import com.ibaevzz.shoppinglist.view_models.ItemsViewModel
import com.ibaevzz.shoppinglist.view_models.ViewModelFactory
import kotlinx.coroutines.*
import javax.inject.Inject

class ItemsFragment: Fragment() {

    private var isFirst = true
    private lateinit var job: Job
    private lateinit var binding: BaseFragmentBinding
    private val listId by lazy {
        requireArguments().getLong(Constants.LIST_ID)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: ItemsViewModel by viewModels {
        viewModelFactory
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ShoppingComponentProvider.appComponent().inject(this)
    }

    override fun onDetach() {
        stop()
        super.onDetach()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View {

        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            setHomeAsUpIndicator(R.drawable.arrow_back)
        }

        binding = BaseFragmentBinding.inflate(inflater)

        if((requireActivity() as MainActivity).isNetworkAvailable()) {
            makeUI()
            start()
        }else{
            Toast.makeText(requireContext(), "Не удалось подключиться", Toast.LENGTH_SHORT).show()
            CoroutineScope(Dispatchers.IO).launch {
                while(true){
                    if((requireActivity() as MainActivity).isNetworkAvailable()){
                        withContext(Dispatchers.Main){
                            makeUI()
                            start()
                        }
                        break
                    }
                }
            }
        }

        return binding.root
    }

    private fun start(){
        job = CoroutineScope(Dispatchers.IO).launch {
            while(true){
                try {
                    if((requireActivity() as MainActivity).isNetworkAvailable()){
                        withContext(Dispatchers.Main){
                            viewModel.getAllItemsOfList(listId)
                        }
                    }
                }catch (_: IllegalStateException){

                }

                delay(1000)
            }
        }
    }

    private fun stop(){
        job.cancel()
    }

    private fun makeUI(){
        if((requireActivity() as MainActivity).isNetworkAvailable()){
            viewModel.getAllItemsOfList(listId).observe(this){allItems->
                if(isFirst) {
                    binding.recycler.visibility = View.VISIBLE
                    binding.add.visibility = View.VISIBLE
                    binding.progress.visibility = View.INVISIBLE

                    binding.recycler.layoutManager = LinearLayoutManager(requireContext())
                    if (allItems?.success == true) {
                        binding.recycler.adapter =
                            ItemsAdapter(allItems, ::delete, ::cross, ::onClick)
                    }

                    binding.add.setOnClickListener {
                        val dialogBinding = AddItemBinding.inflate(layoutInflater)
                        val dialog = AlertDialog.Builder(requireContext())
                            .setTitle("Введите название и количество")
                            .setView(dialogBinding.root)
                            .setPositiveButton("Добавить") { d, _ ->
                                val name = dialogBinding.name.text.toString()
                                var n: Int = 0
                                if (dialogBinding.n.text.toString() != "") {
                                    n = dialogBinding.n.text.toString().toInt()
                                }
                                if (name != "" && n != 0) {
                                    if ((requireActivity() as MainActivity).isNetworkAvailable()) {
                                        viewModel.addItem(listId, name, n)
                                        viewModel.getAllItemsOfList(listId)
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
                    if (allItems?.success == true) {
                        (binding.recycler.adapter as ItemsAdapter).changeItems(allItems)
                    }
                }
                isFirst = false
            }
        }else{
            Toast.makeText(requireContext(), "Отсутствует интернет соединение", Toast.LENGTH_SHORT).show()
        }
    }

    private fun delete(itemId: Long){
        if((requireActivity() as MainActivity).isNetworkAvailable()) {
            viewModel.deleteItem(listId, itemId)
            viewModel.getAllItemsOfList(listId)
            binding.progress.visibility = View.VISIBLE
        }else{
            Toast.makeText(requireContext(), "Отсутствует интернет соединение", Toast.LENGTH_SHORT).show()
        }
    }

    private fun cross(itemId: Long){
        if((requireActivity() as MainActivity).isNetworkAvailable()) {
            viewModel.crossItem(listId, itemId)
            viewModel.getAllItemsOfList(listId)
            binding.progress.visibility = View.VISIBLE
        }else{
            Toast.makeText(requireContext(), "Отсутствует интернет соединение", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onClick(item: Item){
        if(item.isCrossed)
            return
        val dialogBinding = AddItemBinding.inflate(layoutInflater)
        dialogBinding.name.setText(item.name)
        dialogBinding.n.setText(item.created)
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Введите название и количество")
            .setView(dialogBinding.root)
            .setPositiveButton("Изменить"){d, _->
                val name = dialogBinding.name.text.toString()
                val n = dialogBinding.n.text.toString().toInt()
                if(name!=""){
                    if((requireActivity() as MainActivity).isNetworkAvailable()) {
                        viewModel.addItem(listId, name, n)
                        viewModel.deleteItem(listId, item.id)
                        viewModel.getAllItemsOfList(listId)
                        binding.progress.visibility = View.VISIBLE
                    }else{
                        Toast.makeText(requireContext(), "Отсутствует интернет соединение", Toast.LENGTH_SHORT).show()
                    }
                    d.cancel()
                }
            }
            .setNegativeButton("Назад"){d, _->
                d.cancel()
            }
        dialog.create().show()
    }
}