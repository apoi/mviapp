/*
 * Copyright 2017 Futurice GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package apoi.mviapp.freesound.arch

import io.reactivex.Flowable
import io.reactivex.FlowableTransformer

typealias EventMapper<E, A> = (E) -> A

typealias Reducer<R, S> = (S, R) -> S

typealias Dispatcher<A, R> = FlowableTransformer<in A, out R>

fun <A, R> combine(vararg transformers: Dispatcher<A, R>): Dispatcher<A, R> {
    return Dispatcher {
        it.publish { actions: Flowable<A> ->
            Flowable.merge(transformers.map { it -> actions.compose(it) })
        }
    }
}
