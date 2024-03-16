package com.tanerkotlin.artbooktesting.repo

import android.provider.ContactsContract.CommonDataKinds.Im
import androidx.lifecycle.LiveData
import com.tanerkotlin.artbooktesting.model.ImageResponse
import com.tanerkotlin.artbooktesting.roomdb.Art
import com.tanerkotlin.artbooktesting.util.Resource

interface ArtRepositoryInterface {

    suspend fun insertArt(art: Art)

    suspend fun deleteArt(art:Art)

    fun getArt(): LiveData<List<Art>>

    suspend fun searchImage(imageString: String) : Resource<ImageResponse>

}