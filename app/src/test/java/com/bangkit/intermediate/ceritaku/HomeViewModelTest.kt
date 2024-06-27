package com.bangkit.intermediate.ceritaku

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.bangkit.intermediate.ceritaku.repository.DataRepository
import com.bangkit.intermediate.ceritaku.source.response.ListStoryItem
import com.bangkit.intermediate.ceritaku.ui.adapter.StoryAdapter
import com.bangkit.intermediate.ceritaku.ui.viewModels.HomeViewModel
import com.bangkit.intermediate.ceritaku.utils.ApiResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class TestingMyViewModel {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var dataRepository: DataRepository

    @Test
    fun `when Get Story Should Not Null and Return Data`() = runTest {
        val dummyStory = DummyData.generateDummyStoryResponse()
        val data: PagingData<ListStoryItem> = StoryPagingSource.snapshot(dummyStory)
        val expectedStory = MutableLiveData<ApiResult<PagingData<ListStoryItem>>>()
        expectedStory.value = ApiResult.Success(data)
        Mockito.`when`(dataRepository.getStory("token")).thenReturn(flowOf(ApiResult.Success(data)))

        val homeViewModel = HomeViewModel(dataRepository)
        homeViewModel.fetchAllStories("token")
        val actualStory = homeViewModel.getAllStory.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.StoryDiffCallback,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        if (actualStory is ApiResult.Success) {
            differ.submitData(actualStory.data)
        }

        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyStory.size, differ.snapshot().size)
        Assert.assertEquals(dummyStory[0], differ.snapshot()[0])
    }

    @Test
    fun `when Get Story Empty Should Return No Data`() = runTest {
        val data: PagingData<ListStoryItem> = PagingData.from(emptyList())
        val expectedStory = MutableLiveData<ApiResult<PagingData<ListStoryItem>>>()
        expectedStory.value = ApiResult.Success(data)
        Mockito.`when`(dataRepository.getStory("token")).thenReturn(flowOf(ApiResult.Success(data)))

        val homeViewModel = HomeViewModel(dataRepository)
        homeViewModel.fetchAllStories("token")
        val actualStory = homeViewModel.getAllStory.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.StoryDiffCallback,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        if (actualStory is ApiResult.Success) {
            differ.submitData(actualStory.data)
        }

        Assert.assertEquals(0, differ.snapshot().size)
    }
}

class StoryPagingSource : PagingSource<Int, ListStoryItem>() {
    companion object {
        fun snapshot(items: List<ListStoryItem>): PagingData<ListStoryItem> {
            return PagingData.from(items)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}