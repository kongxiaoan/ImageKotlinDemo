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

package com.kpa.imagekotlindemo.features.image.entry

import com.google.gson.annotations.SerializedName
import com.kpa.imagekotlindemo.core.extension.empty

data class Image(
    @SerializedName("_id")
    val id: String,
    @SerializedName("author")
    val author: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("desc")
    val desc: String,
    @SerializedName("images")
    val images: List<String>,
    @SerializedName("likeCounts")
    val likeCounts: Int,
    @SerializedName("publishedAt")
    val publishedAt: String,
    @SerializedName("stars")
    val stars: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("views")
    val views: Int
) {
    companion object {
        fun empty() = Image(
            String.empty(),
            String.empty(),
            String.empty(),
            String.empty(),
            emptyList(),
            0,
            String.empty(),
            0,
            String.empty(),
            String.empty(),
            String.empty(),
            0
        )
    }
}