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

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kpa.imagekotlindemo.ImageKotlinDemoApplication
import com.kpa.imagekotlindemo.core.di.ApplicationComponent
import javax.inject.Inject

class RouteActivity : AppCompatActivity() {
    private val appComponent: ApplicationComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        (application as ImageKotlinDemoApplication).appComponent
    }
    @Inject // 修改属性 表示该属性需要依赖注入 不能使用private 修饰
    //internal 修饰类 表示只能在当前moudle 使用
    internal lateinit var navigator: Navigator
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        navigator.showMain(this)
    }
}