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

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.kpa.imagekotlindemo.R
import com.kpa.imagekotlindemo.core.base.BaseFragment
import com.kpa.imagekotlindemo.core.exception.Failure
import com.kpa.imagekotlindemo.core.extension.failure
import com.kpa.imagekotlindemo.core.extension.observe
import com.kpa.imagekotlindemo.core.extension.viewModel
import com.kpa.imagekotlindemo.core.navigation.Navigator
import com.kpa.imagekotlindemo.features.image.adapter.ImageAdapter
import com.kpa.imagekotlindemo.features.image.data.GetImage
import com.kpa.imagekotlindemo.features.image.entry.Image
import com.kpa.imagekotlindemo.features.image.viewmodel.ImageViewModel
import kotlinx.android.synthetic.main.activity_image.*
import javax.inject.Inject

/**
 *    author : kpa
 *    e-mail : billkp@yeah.net
 */
class ImageFragment : BaseFragment() {
    @Inject
    lateinit var navigator: Navigator

    @Inject
    lateinit var mAdapter: ImageAdapter


    private lateinit var imageViewModel: ImageViewModel

    override fun layoutId(): Int = R.layout.activity_image

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)

        imageViewModel = viewModel(viewModelFactory) {
            observe(image, ::renderImageList)
            failure(failure, ::handleFailure)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeView()
        loadImageList()
    }

    private fun loadImageList() {
        imageViewModel.loadImage(1, 30)
    }

    private fun initializeView() {
        imageList.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        imageList.adapter = mAdapter
        mAdapter.clickListener = { image, navigationExtras ->
            // click
        }
    }

    private fun renderImageList(image: List<Image>?) {
        mAdapter.collection = image.orEmpty()
        hideProgress()
    }

    private fun hideProgress() {

    }

    private fun handleFailure(failure: Failure?) {
        when (failure) {
            is Failure.NetworkConnection -> renderFailure("网络异常")
            is Failure.ServerError -> renderFailure("服务异常")
        }
    }

    private fun renderFailure(s: String) {
        Log.e("ImageKotlinDemo", s)
    }

}