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

package apoi.mviapp.freesound.arch.store

import apoi.mviapp.common.Result
import apoi.mviapp.common.State
import apoi.mviapp.freesound.arch.LogEvent
import apoi.mviapp.freesound.arch.Logger
import apoi.mviapp.freesound.arch.Reducer
import apoi.mviapp.freesound.arch.viewmodel.asUiModelFlowable
import io.reactivex.FlowableTransformer

class Store<R : Result, S : State>(
    private val initialState: S,
    private val reducer: Reducer<R, S>
) {

    private val tag = Store::class.java.simpleName
    private val logger = Logger()

    fun reduceResult(): FlowableTransformer<R, S> {
        return FlowableTransformer { it ->
            it.doOnNext { result -> logger.log(tag, LogEvent.Result(result)) }
                .scan(initialState) { model: S, result: R -> reduce(model, result) }
                .doOnNext { model: S -> logger.log(tag, LogEvent.State(model)) }
                .asUiModelFlowable()
        }
    }

    private fun reduce(current: S, result: R): S {
        logger.log(tag, LogEvent.Reduce(result, current))
        return reducer(current, result)
    }
}
