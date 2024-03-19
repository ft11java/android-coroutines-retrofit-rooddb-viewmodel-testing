package com.tanerkotlin.artbooktesting.roomdb

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.tanerkotlin.artbooktesting.getOrAwaitValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@SmallTest
@ExperimentalCoroutinesApi
@HiltAndroidTest
class ArtDaoTest {

    @get :Rule
    var instantTaskExecutorRule=InstantTaskExecutorRule()

    @get:Rule
    var hiltRule=HiltAndroidRule(this)

    @Inject
    @Named("testDatabase")
    lateinit var database: ArtDatabase

    private lateinit var dao:ArtDao

    @Before
    fun setup(){
        /* ->>hilt test sürecine katılmadan önce datatbase bu şekilde başlatıldı.
        database=Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),ArtDatabase::class.java
        ).allowMainThreadQueries().build()
         */

        hiltRule.inject()
        dao=database.artDao()
    }

    @After
    fun teardown(){
        database.close()
    }

    @Test
    fun insertArtTesting()= runTest{
        val  exampleArt=Art("Mona Lisa","Da Vinci",1700,"test.com",1)
        dao.insertArt(exampleArt)
        val list=dao.observeArts().getOrAwaitValue()
        assertThat(list).contains(exampleArt)

    }
    @Test
    fun deleteArtTesting()= runTest{
        val  exampleArt=Art("Mona Lisa","Da Vinci",1700,"test.com",1)
        dao.insertArt(exampleArt)
        dao.deleteArt(exampleArt)
        val list=dao.observeArts().getOrAwaitValue()
        assertThat(list).doesNotContain(exampleArt)
    }


}