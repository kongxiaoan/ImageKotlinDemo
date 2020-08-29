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

package com.kpa.imagekotlindemo.features.image

import com.kpa.imagekotlindemo.R
import com.kpa.imagekotlindemo.core.base.BaseFragment
import com.kpa.imagekotlindemo.core.navigation.Navigator
import com.kpa.imagekotlindemo.features.image.adapter.ImageAdapter
import com.kpa.imagekotlindemo.features.image.viewmodel.ImageViewModel
import javax.inject.Inject

/**
 *    author : kpa
 *    e-mail : billkp@yeah.net
 */
class ImageFragment : BaseFragment() {
    @Inject lateinit var navigator: Navigator
    @Inject lateinit var mAdapter: ImageAdapter

    private lateinit var imageViewModel: ImageViewModel
    override fun layoutId(): Int = R.layout.activity_image
}