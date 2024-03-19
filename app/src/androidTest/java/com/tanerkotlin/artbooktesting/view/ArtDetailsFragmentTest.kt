package com.tanerkotlin.artbooktesting.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.google.common.truth.Truth.assertThat
import com.tanerkotlin.artbooktesting.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import javax.inject.Inject
import com.tanerkotlin.artbooktesting.R
import com.tanerkotlin.artbooktesting.getOrAwaitValue
import com.tanerkotlin.artbooktesting.repo.FakeArtRepositoryAndroid
import com.tanerkotlin.artbooktesting.roomdb.Art
import com.tanerkotlin.artbooktesting.viewmodel.ArtViewModel

@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class ArtDetailsFragmentTest {

    @get:Rule
    var hiltRule=HiltAndroidRule(this)

    @get:Rule
    val instantTaskExecutorRule=InstantTaskExecutorRule()

    @Inject
    lateinit var fragmentFactory: ArtFragmentFactory

    @Before
    fun setup(){
        hiltRule.inject()
    }

    @Test
    fun testNavigationFromArtDetailsToImageApi(){
        val navController=Mockito.mock(NavController::class.java)

        launchFragmentInHiltContainer<ArtDetailsFragment>(
            factory=fragmentFactory
        ){
            Navigation.setViewNavController(requireView(),navController)
        }

        Espresso.onView(ViewMatchers.withId(R.id.artimageView)).perform(ViewActions.click())

        Mockito.verify(navController).navigate(
            ArtDetailsFragmentDirections.actionArtDetailsFragmentToImageApiFragment()
        )
    }

    @Test
    fun testOnBackPressed(){
        val navController=Mockito.mock(NavController::class.java)

        launchFragmentInHiltContainer<ArtDetailsFragment>(
            factory=fragmentFactory
        ){
            Navigation.setViewNavController(requireView(),navController)
        }

        pressBack()
        Mockito.verify(navController).popBackStack()
    }

    @Test
    fun testSave() {
        val testViewModel = ArtViewModel(FakeArtRepositoryAndroid())
        launchFragmentInHiltContainer<ArtDetailsFragment>(
            factory = fragmentFactory
        ){
            (this as ArtDetailsFragment).viewModel = testViewModel
        }

        onView(withId(R.id.nameText)).perform(replaceText("Mona Lisa"))
        onView(withId(R.id.artistText)).perform(replaceText("Da Vinci"))
        onView(withId(R.id.yearText)).perform(replaceText("1700"))
        onView(withId(R.id.saveButton)).perform(click())

        assertThat(testViewModel.artList.getOrAwaitValue()).contains(
            Art(
                "Mona Lisa",
                "Da Vinci",
                1700,"")
        )
    }

}