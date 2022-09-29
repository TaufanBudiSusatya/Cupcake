package com.example.cupcake

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.cupcake.databinding.FragmentStartBinding
import com.example.cupcake.model.OrderViewModel

/**
 * Ini adalah layar pertama dari aplikasi Cupcake. Pengguna dapat memilih berapa banyak cupcakes yang akan dipesan.
 */
class StartFragment : Fragment() {

    private var binding: FragmentStartBinding? = null

    private val sharedViewModel: OrderViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentStartBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.startFragment = this
    }

    /**
     * Mulai pesanan dengan jumlah kue  yang diinginkan dan
     * navigasikan ke layar berikutnya.
     */
    fun orderCupcake(quantity: Int) {
        sharedViewModel.setQuantity(quantity)

// Jika belum ada rasa yang diatur dalam model tampilan, pilih vanilla sebagai rasa default
        if (sharedViewModel.hasNoFlavorSet()) {
            sharedViewModel.setFlavor(getString(R.string.vanilla))
        }

        // Navigate to the next destination to select the flavor of the cupcakes
        findNavController().navigate(R.id.action_startFragment_to_flavorFragment)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}