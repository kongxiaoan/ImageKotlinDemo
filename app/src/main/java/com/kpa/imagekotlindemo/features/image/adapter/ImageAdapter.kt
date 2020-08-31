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

package com.kpa.imagekotlindemo.features.image.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kpa.imagekotlindemo.R
import com.kpa.imagekotlindemo.core.extension.inflate
import com.kpa.imagekotlindemo.core.extension.loadFromUrl
import com.kpa.imagekotlindemo.core.navigation.Navigator
import com.kpa.imagekotlindemo.features.image.entry.Image
import com.kpa.imagekotlindemo.features.image.entry.ImageEntry
import kotlinx.android.synthetic.main.ap_item_image.view.*
import javax.inject.Inject
import kotlin.properties.Delegates

/**
 *    author : kpa
 *    e-mail : billkp@yeah.net
 */
class ImageAdapter @Inject constructor() : RecyclerView.Adapter<ImageAdapter.ViewHolder>() {

    internal var collection: List<Image> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }
    internal var clickListener: (Image, Navigator.Extras) -> Unit = { _, _ -> }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(image: Image, clickListener: (Image, Navigator.Extras) -> Unit) {
            itemView.imagePoster.loadFromUrl(image.url)
            itemView.setOnClickListener {
                clickListener(image, Navigator.Extras(itemView.imagePoster))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(parent.inflate(R.layout.ap_item_image))

    override fun getItemCount(): Int = collection.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(collection[position], clickListener)

}