package com.example.toko.UI

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.toko.Application.ShopApplication
import com.example.toko.R
import com.example.toko.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var applicationContext: Context
    private val shopViewModel: ShopViewModel by viewModels {
        ShopViewModelFactory((applicationContext as ShopApplication).repository)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        applicationContext=requireContext().applicationContext
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ShopListAdapter{ shop ->
            val action = FirstFragmentDirections.actionFirstFragmentToSecondFragment(shop)
            findNavController().navigate(action)

        }

        binding.recyclerView.adapter= adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        shopViewModel.allShop.observe(viewLifecycleOwner) { shops ->
            shops.let {
                if (shops.isEmpty()){
                    binding.emptytext.visibility= View.VISIBLE
                    binding.imageView.visibility= View.VISIBLE
                }else {
                    binding.emptytext.visibility = View.GONE
                    binding.imageView.visibility = View.GONE
                }
                adapter.submitList(shops)

            }
        }
       binding.addFAB.setOnClickListener {
           val action = FirstFragmentDirections.actionFirstFragmentToSecondFragment(null)
           findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}