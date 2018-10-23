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

import apoi.mviapp.common.Event
import apoi.mviapp.common.State
import apoi.mviapp.core.BaseFragment
import apoi.mviapp.freesound.arch.viewmodel.ViewModel

/**
 * A base Fragment which provides the binding mechanism hooks to a MviView ViewModel.
 *
 * @param <C> The DI component class.
 */
abstract class MviBaseFragment<M : State, E : Event, VM : ViewModel<E, M>>
    : BaseFragment(), MviView<E, M> {

    internal lateinit var flow: Flow<E, M, VM>
}
