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

package apoi.mviapp.freesound.arch.view

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import apoi.mviapp.common.Event
import apoi.mviapp.common.State
import apoi.mviapp.extensions.observeOnCreate
import apoi.mviapp.extensions.observeOnDestroy
import apoi.mviapp.freesound.arch.viewmodel.ViewModel

/**
 * The MviView holds this instance.
 */
class Flow<E : Event, S : State, VM : ViewModel<E, S>>(
    private val mviView: MviView<E, S>,
    private val viewModel: VM,
    private val lifecycleOwner: LifecycleOwner
) {

    init {
        lifecycleOwner.lifecycle.observeOnCreate { connect() }
    }

    private fun connect() {
        // Start processing Events
        viewModel.bind()

        // Send Events to the ViewModel
        mviView.events()
            .observe(lifecycleOwner, Observer { viewModel.events(it) })

        // Send States to the View
        viewModel.state()
            .observe(lifecycleOwner, Observer { mviView.render(it) })

        // Cancels asynchronous actions the view is handling
        lifecycleOwner.lifecycle.observeOnDestroy { mviView.cancel() }
    }
}
