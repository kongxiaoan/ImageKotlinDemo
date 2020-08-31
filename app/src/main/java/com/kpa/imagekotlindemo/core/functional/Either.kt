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

package com.kpa.imagekotlindemo.core.functional

/**
 *    author : kpa
 *    e-mail : billkp@yeah.net
 */
sealed class Either<out L, out R> {

    data class Error<out L>(val a: L) : Either<L, Nothing>()

    data class Success<out R>(val b: R) : Either<Nothing, R>()

    val isRight get() = this is Success<R>

    val isLeft get() = this is Error<L>

    fun <L> left(a: L) = Error(a)

    fun <R> right(b: R) = Success(b)

    fun fold(fnL: (L) -> Any, fnR: (R) -> Any): Any =
        when(this) {
            is Error -> fnL(a)
            is Success -> fnR(b)
        }
}