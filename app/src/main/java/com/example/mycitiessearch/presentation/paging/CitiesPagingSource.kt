package com.example.mycitiessearch.presentation.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.mycitiessearch.domain.models.CityModel
import com.example.mycitiessearch.domain.usecases.CitiesUseCase
import okio.IOException

private const val DEFAULT_PAGE = 0
private const val DEFAULT_FIRST_PAGE = 1

class CitiesPagingSource(
    private val textQuery: String,
    private val isFavoritesFilter: Boolean,
    private val citiesUseCase: CitiesUseCase
) : PagingSource<Int, CityModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CityModel> {
        return try {
            val page = params.key ?: DEFAULT_PAGE
            val cities = when {
                isFavoritesFilter && textQuery.isNotEmpty() -> {
                    citiesUseCase.getSearchFavoritesCities(
                        query = textQuery,
                        limit = params.loadSize,
                        offset = page * params.loadSize
                    )
                }

                textQuery.isNotEmpty() -> {
                    citiesUseCase.getCitiesSearch(
                        query = textQuery,
                        limit = params.loadSize,
                        offset = page * params.loadSize
                    )
                }

                isFavoritesFilter -> {
                    citiesUseCase.getAllFavoritesCities(
                        limit = params.loadSize,
                        offset = page * params.loadSize
                    )
                }

                else -> {
                    citiesUseCase.getCitiesDB(
                        limit = params.loadSize,
                        offset = page * params.loadSize
                    )
                }
            }
            LoadResult.Page(
                data = cities,
                prevKey = if (page > DEFAULT_PAGE) page.minus(DEFAULT_FIRST_PAGE) else null,
                nextKey = if (cities.isEmpty()) null else page.plus(DEFAULT_FIRST_PAGE)
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, CityModel>): Int? {
        return state.anchorPosition
    }
}