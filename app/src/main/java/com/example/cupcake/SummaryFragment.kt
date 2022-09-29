package com.example.cupcake

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.cupcake.databinding.FragmentSummaryBinding
import com.example.cupcake.model.OrderViewModel

/**
 * [SummaryFragment] berisi ringkasan detail pesanan dengan tombol untuk membagikan pesanan
 * melalui aplikasi lain.
 */
class SummaryFragment : Fragment() {
    private var binding: FragmentSummaryBinding? = null

    private val sharedViewModel: OrderViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentSummaryBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = sharedViewModel
            summaryFragment = this@SummaryFragment
        }
    }

    /**
     * Kirim pesanan dengan membagikan detail pesanan ke aplikasi
     * lain melalui maksud implisit.
     */
    fun sendOrder() {
// Buat teks ringkasan pesanan dengan informasi dari model tampilan
        val numberOfCupcakes = sharedViewModel.quantity.value ?: 0
        val orderSummary = getString(
            R.string.order_details,
            resources.getQuantityString(R.plurals.cupcakes, numberOfCupcakes, numberOfCupcakes),
            sharedViewModel.flavor.value.toString(),
            sharedViewModel.date.value.toString(),
            sharedViewModel.price.value.toString()
        )

        //membuat action_send untuk detail pesanan
        val intent = Intent(Intent.ACTION_SEND)
            .setType("text/plain")
            .putExtra(Intent.EXTRA_SUBJECT, getString(R.string.new_cupcake_order))
            .putExtra(Intent.EXTRA_TEXT, orderSummary)

        if (activity?.packageManager?.resolveActivity(intent, 0) != null) {
            startActivity(intent)
        }
    }

    /**
     * Cancel the order and start over.
     */
    fun cancelOrder() {
        // mengulang pesanan dalam viewmodel
        sharedViewModel.resetOrder()

        // kembali ke start fragment untuk memulai kembali
        findNavController().navigate(R.id.action_summaryFragment_to_startFragment)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}