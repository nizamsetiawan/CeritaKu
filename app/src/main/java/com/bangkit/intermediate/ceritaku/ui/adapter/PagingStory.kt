package com.bangkit.intermediate.ceritaku.ui.adapter

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bangkit.intermediate.ceritaku.source.RemoteDataSource
import com.bangkit.intermediate.ceritaku.source.response.ListStoryItem
import retrofit2.HttpException
import java.io.IOException

class PagingStory (
    private val remoteDataSource: RemoteDataSource,
    private val token: String
) : PagingSource<Int, ListStoryItem>() {

    companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        val page = params.key ?: INITIAL_PAGE_INDEX

        return try {
            val response = remoteDataSource.getStory(token, page, params.loadSize)

            if (response.error == false) {
                val stories = response.listStory
                LoadResult.Page(
                    data = stories,
                    prevKey = if (page == INITIAL_PAGE_INDEX) null else page - 1,
                    nextKey = if (stories.isEmpty()) null else page + 1
                )
            } else {
                LoadResult.Error(IOException("Failed to fetch data"))
            }
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition
    }
}
