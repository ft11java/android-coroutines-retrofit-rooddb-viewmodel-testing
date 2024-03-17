package com.tanerkotlin.artbooktesting.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.tanerkotlin.artbooktesting.MainCoroutineRule
import com.tanerkotlin.artbooktesting.getOrAwaitValueTest
import com.tanerkotlin.artbooktesting.repo.FakeArtRepository
import com.tanerkotlin.artbooktesting.util.Status
import com.tanerkotlin.artbooktesting.viewmodel.ArtViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ArtViewModelTest {

    @get:Rule
    var instantTaskExecutorRule=InstantTaskExecutorRule()
    @get:Rule
    var mainCoroutineRule=MainCoroutineRule()

    private lateinit var viewModel:ArtViewModel

    @Before
    fun setup(){
        //Test Double
        viewModel= ArtViewModel(FakeArtRepository())
    }

    @Test
    fun `insert art without year returns error`(){
      viewModel.makeArt("Mona Lisa","Da Vinci","")
      val value=viewModel.insertArtMessage.getOrAwaitValueTest()
      assertThat(value.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert art without name returns error`(){
        viewModel.makeArt("","Da Vinci","2024")
        val value=viewModel.insertArtMessage.getOrAwaitValueTest()
        assertThat(value.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert art without artistName returns error`(){
        viewModel.makeArt("Mona Lisa","","2024")
        val value=viewModel.insertArtMessage.getOrAwaitValueTest()
        assertThat(value.status).isEqualTo(Status.ERROR)
    }
}