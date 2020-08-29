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

package com.kpa.imagekotlindemo.features.image.viewmodel

import android.media.Image
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kpa.imagekotlindemo.core.base.BaseViewModel
import com.kpa.imagekotlindemo.core.interactor.UseCase
import com.kpa.imagekotlindemo.features.image.data.GetImage
import com.kpa.imagekotlindemo.features.image.entry.ImageEntry
import javax.inject.Inject

/**
 *    author : kpa
 *    e-mail : billkp@yeah.net
 */
class ImageViewModel @Inject constructor(private val getImage: GetImage) : BaseViewModel() {
    private val _image: MutableLiveData<List<ImageEntry>> = MutableLiveData()
    val image: LiveData<List<ImageEntry>> = _image

    fun loadImage() = getImage(UseCase.None()) {
        it.fold(::handleFailure, ::handleImageList)
    }

    private fun handleImageList(imageEntry: List<ImageEntry>) {
        _image.value = imageEntry.map {
            ImageEntry()
        }
    }
}