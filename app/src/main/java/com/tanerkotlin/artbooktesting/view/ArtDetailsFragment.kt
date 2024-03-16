package com.tanerkotlin.artbooktesting.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.RequestManager
import com.tanerkotlin.artbooktesting.R
import com.tanerkotlin.artbooktesting.databinding.FragmentArtDetailsBinding
import com.tanerkotlin.artbooktesting.util.Status
import com.tanerkotlin.artbooktesting.viewmodel.ArtViewModel
import javax.inject.Inject

class ArtDetailsFragment @Inject constructor(
    val glide: RequestManager
) : Fragment(R.layout.fragment_art_details){

    private var fragmentBinding : FragmentArtDetailsBinding?=null
    lateinit var viewModel:ArtViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel=ViewModelProvider(requireActivity()).get(ArtViewModel::class.java)

        val binding =FragmentArtDetailsBinding.bind(view)
        fragmentBinding=binding

        subscribeToObservers()

        binding.artimageView.setOnClickListener {
            findNavController().navigate(ArtDetailsFragmentDirections.actionArtDetailsFragmentToImageApiFragment())
        }

        val callback=object :OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(callback)
        binding.saveButton.setOnClickListener {
            viewModel.makeArt(binding.nameText.text.toString(),
                binding.artistText.text.toString(),
                binding.yearText.text.toString())
        }

    }

    private fun subscribeToObservers(){
        viewModel.selectedImageUrl.observe(viewLifecycleOwner, Observer { url->
            fragmentBinding?.let {
                glide.load(url).into(it.artimageView)
            }
        })

        viewModel.insertArtMessage.observe(viewLifecycleOwner, Observer {
            when(it.status){
                Status.SUCCESS->{
                    Toast.makeText(requireContext(),"Success",Toast.LENGTH_LONG).show()
                    findNavController().popBackStack()
                    viewModel.resetInsertArtMsg()
                }

                Status.ERROR->{
                    Toast.makeText(requireContext(),it.message?:"Error",Toast.LENGTH_LONG).show()
                }

                Status.LOADING->{

                }

            }
        })
    }

    override fun onDestroyView() {
        fragmentBinding=null
        super.onDestroyView()
    }
}