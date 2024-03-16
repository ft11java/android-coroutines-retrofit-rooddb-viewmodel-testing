package com.tanerkotlin.artbooktesting.view

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.bumptech.glide.RequestManager
import com.tanerkotlin.artbooktesting.adapter.ArtRecyclerAdapter
import com.tanerkotlin.artbooktesting.adapter.ImageRecyclerAdapter
import javax.inject.Inject

class ArtFragmentFactory @Inject constructor(
    private val artRecyclerAdapter: ArtRecyclerAdapter,
    private val glide:RequestManager,
    private val imageRecyclerAdepter:ImageRecyclerAdapter
):FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (className) {
            ArtFragment::class.java.name->ArtFragment(artRecyclerAdapter)
            ArtDetailsFragment::class.java.name -> ArtDetailsFragment(glide)
            ImageApiFragment::class.java.name->ImageApiFragment(imageRecyclerAdepter)
            else-> super.instantiate(classLoader, className)
        }
    }
}