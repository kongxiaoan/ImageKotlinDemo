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

package com.kpa.imagekotlindemo.core.navigation

import android.content.Context
import android.view.View
import com.kpa.imagekotlindemo.features.image.ImageActivity
import com.kpa.imagekotlindemo.features.logo.Authenticator
import com.kpa.imagekotlindemo.features.logo.LoginActivity
import javax.inject.Inject
import javax.inject.Singleton

/**
 *    author : kpa
 *    e-mail : billkp@yeah.net
 */
@Singleton
class Navigator @Inject constructor(private val authenticator: Authenticator) {
    private fun showLogin(context: Context) =
        context.startActivity(LoginActivity.callingIntent(context))

    fun showMain(context: Context) {
        when (authenticator.userLoggedIn()) {
            true -> showImage(context)
            false -> showLogin(context)
        }
    }

    private fun showImage(context: Context) =
        context.startActivity(ImageActivity.callingIntent(context))

    class Extras(val transitionSharedElement: View)
}