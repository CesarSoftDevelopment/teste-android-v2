package com.cesarsoftdevelopment.aikopublictransport.data.repository.datasourceimpl

import com.cesarsoftdevelopment.aikopublictransport.data.model.StopItem
import com.cesarsoftdevelopment.aikopublictransport.data.repository.datasource.StopsRemoteDataSource
import retrofit2.Response
class StopsRemoteDataSourceImpl : StopsRemoteDataSource {
    override suspend fun getStopsByLine(lineCode: Int): Response<List<StopItem>> {
        TODO("Not yet implemented")
    }


}