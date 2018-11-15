/*
 * Copyright 2018 Futurice GmbH
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

package apoi.mviapp.freesound.arch.view

import android.os.Bundle
import apoi.mviapp.common.Event
import apoi.mviapp.common.State
import apoi.mviapp.core.BaseFragment
import apoi.mviapp.extensions.observeOnResume
import apoi.mviapp.freesound.arch.viewmodel.ViewModel
import java.util.Random

private const val STATE_KEY = "mvi_state"

/**
 * A base Fragment which provides the binding mechanism hooks to a MviView ViewModel.
 */
abstract class MviBaseFragment<S : State, E : Event, VM : ViewModel<E, S>>
    : BaseFragment() {

    internal lateinit var flow: Flow<E, S, VM>

    internal abstract val viewModel: VM

    protected var initialState: S? = null
        private set

    init {
        lifecycle.observeOnResume { viewModel.bind() }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        savedInstanceState?.getInt(STATE_KEY, 0)?.let {
            initialState = fragmentStateStore.get(it) as? S
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        viewModel.state().value?.let {
            val bundleId = Random().nextInt()
            outState.putInt(STATE_KEY, bundleId)
            fragmentStateStore.put(bundleId, it)
        }
    }
}
