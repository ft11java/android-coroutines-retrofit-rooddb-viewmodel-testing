package com.tanerkotlin.artbooktesting.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.MediumTest
import com.google.common.truth.Truth.assertThat
import com.tanerkotlin.artbooktesting.launchFragmentInHiltContainer
import com.tanerkotlin.artbooktesting.repo.FakeArtRepositoryAndroid
import com.tanerkotlin.artbooktesting.viewmodel.ArtViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import javax.inject.Inject
import com.tanerkotlin.artbooktesting.R
import com.tanerkotlin.artbooktesting.adapter.ImageRecyclerAdapter
import com.tanerkotlin.artbooktesting.getOrAwaitValue


@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class ImageApiFragmentTest {

    @get:Rule
    var instantiationExceptionRule=InstantTaskExecutorRule()

    @get:Rule
    var hiltRule=HiltAndroidRule(this)

    @Inject
    lateinit var fragmentFactory:ArtFragmentFactory

    @Before
    fun setup(){
        hiltRule.inject()
    }

    @Test
    fun selectImage(){
        val navController=Mockito.mock(NavController::class.java)
        val selectedImageUrl="taner.com"
        val testViewModel= ArtViewModel(FakeArtRepositoryAndroid())

        launchFragmentInHiltContainer<ImageApiFragment>(
            factory=fragmentFactory
        ){
            Navigation.setViewNavController(requireView(),navController)
            (this as ImageApiFragment).viewModel=testViewModel
            imageRecyclerAdapter.images= listOf(selectedImageUrl)
        }
        Espresso.onView(ViewMatchers.withId(R.id.imageRecyclerView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<ImageRecyclerAdapter.ImageViewHolder>(
                0, ViewActions.click()
            )
        )
        Mockito.verify(navController).popBackStack()

        assertThat(testViewModel.selectedImageUrl.getOrAwaitValue()).isEqualTo(selectedImageUrl)


    }

}