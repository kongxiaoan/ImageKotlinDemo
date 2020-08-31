/*
 * Copyright [2020] [kongpingan]
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 */

package com.kpa.imagekotlindemo.features.image.data.api

import com.kpa.imagekotlindemo.features.image.entry.ImageEntry
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 *    author : kpa
 *    e-mail : billkp@yeah.net
 */
interface ImageApi {
    @GET("page/{page}/count/{size}")
    fun images(@Path("page") page: Int, @Path("size") size: Int): Call<ImageEntry>
}