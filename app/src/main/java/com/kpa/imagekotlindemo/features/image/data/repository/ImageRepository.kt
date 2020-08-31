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

package com.kpa.imagekotlindemo.features.image.data.repository

import com.kpa.imagekotlindemo.core.base.NetworkHandler
import com.kpa.imagekotlindemo.core.exception.Failure
import com.kpa.imagekotlindemo.core.functional.Either
import com.kpa.imagekotlindemo.features.image.data.api.ImageService
import com.kpa.imagekotlindemo.features.image.entry.Image
import com.kpa.imagekotlindemo.features.image.entry.ImageEntry
import retrofit2.Call
import javax.inject.Inject

/**
 *    author : kpa
 *    e-mail : billkp@yeah.net
 */
interface ImageRepository {
    fun images(page: Int, size: Int): Either<Failure, ImageEntry>

    class NetWork @Inject constructor(
        private val networkHandler: NetworkHandler,
        private val imageService: ImageService
    ) : ImageRepository {
        override fun images(page: Int, size: Int): Either<Failure, ImageEntry> {
            return when (networkHandler.isConnected) {
                true -> request(
                    imageService.images(page, size),
                    {
                        it
                    },
                    ImageEntry.empty()
                )
                false, null -> Either.Error(Failure.NetworkConnection)
            }
        }

        private fun <T, R> request(
            call: Call<T>,
            transform: (T) -> R,
            default: T
        ): Either<Failure, R> {
            return try {
                val response = call.execute()
                when (response.isSuccessful) {
                    true -> Either.Success(transform((response.body() ?: default)))
                    false -> Either.Error(Failure.ServerError)
                }
            } catch (e: Throwable) {
                Either.Error(Failure.ServerError)
            }
        }
    }
}