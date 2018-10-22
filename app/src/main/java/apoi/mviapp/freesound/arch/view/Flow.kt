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

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.OnLifecycleEvent
import apoi.mviapp.freesound.arch.Event
import apoi.mviapp.freesound.arch.State
import apoi.mviapp.freesound.arch.viewmodel.ViewModel

/**
 * The MviView holds this instance.
 */
class Flow<E : Event, M : State, VM : ViewModel<E, M>>(
    private val mviView: MviView<E, M>,
    private val viewModel: VM,
    private val lifecycleOwner: LifecycleOwner
) {

    init {
        lifecycleOwner.lifecycle.observeOnCreate { connect() }
    }

    private fun connect() {
        // Send UiEvents to the ViewModel
        mviView.events()
            .observe(lifecycleOwner, Observer { viewModel.uiEvents(it!!) })

        // Send UiModels to the View
        viewModel.uiModels()
            .observe(lifecycleOwner, Observer { mviView.render(it!!) })

        // Cancels asynchronous actions the view is handling
        lifecycleOwner.lifecycle.observeOnDestroy { mviView.cancel() }
    }

    private fun Lifecycle.observeOnDestroy(action: () -> Unit) {
        this.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroyed() = action()
        })
    }

    private fun Lifecycle.observeOnCreate(action: () -> Unit) {
        this.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
            fun onCreate() = action()
        })
    }
}
