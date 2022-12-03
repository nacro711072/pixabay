package com.nacro.compose.pixabay.api

import com.nacro.compose.pixabay.api.model.PixabayRemoteData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PixabayApiService {
    @GET("/api")
    suspend fun searchImage(
        @Query("q") q: String, // This value may not exceed 100 characters.
        @Query("lang") lang: String = "en", // cs, da, de, en, es, fr, id, it, hu, nl, no, pl, pt, ro, sk, fi, sv, tr, vi, th, bg, ru, el, ja, ko, zh
        @Query("id") id: String? = null,
        @Query("image_type") imageType: String = "all", // "all", "photo", "illustration", "vector"
        @Query("orientation") orientation: String = "all", // "all", "horizontal", "vertical"
        @Query("category") category: String? = null, // backgrounds, fashion, nature, science, education, feelings, health, people, religion, places, animals, industry, computer, food, sports, transportation, travel, buildings, business, music
        @Query("min_width") minWidth: Int = 0,
        @Query("min_height") minHeight: Int = 0,
        @Query("colors") colors: String? = null, // grayscale", "transparent", "red", "orange", "yellow", "green", "turquoise", "blue", "lilac", "pink", "white", "gray", "black", "brown"
        @Query("editors_choice") editorsChoice: Boolean = false,
        @Query("safesearch") safeSearch: Boolean = false,
        @Query("order") order: String = "popular", // "popular", "latest"
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 20,
        @Query("callback") callback: String? = null,
        @Query("pretty") pretty: Boolean = false, // Indent JSON output. This option should not be used in production.
    ): Response<PixabayRemoteData>

//    @GET("/users")
//    suspend fun getUserList(
//        @Query("since") since: Int
//    ): Response<List<PixabayRemoteData>>

}