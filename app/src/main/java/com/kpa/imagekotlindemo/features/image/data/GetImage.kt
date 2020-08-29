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

package com.kpa.imagekotlindemo.features.image.data

import com.kpa.imagekotlindemo.core.exception.Failure
import com.kpa.imagekotlindemo.core.functional.Either
import com.kpa.imagekotlindemo.core.interactor.UseCase
import com.kpa.imagekotlindemo.features.image.data.repository.ImageRepository
import com.kpa.imagekotlindemo.features.image.entry.ImageEntry
import javax.inject.Inject

/**
 *    author : kpa
 *    e-mail : billkp@yeah.net
 */
class GetImage @Inject constructor(private val imageRepository: ImageRepository): UseCase<List<ImageEntry>, UseCase.None>() {
    override suspend fun run(params: None): Either<Failure, List<ImageEntry>> = imageRepository.images()
}