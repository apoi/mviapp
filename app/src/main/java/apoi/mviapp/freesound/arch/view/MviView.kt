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

import androidx.lifecycle.LiveData
import apoi.mviapp.common.Event
import apoi.mviapp.common.State

interface MviView<E : Event, in S : State> {
    fun events(): LiveData<E> = EmptyLiveData()
    fun render(state: S)
    fun cancel() {}
}

private class EmptyLiveData<T> : LiveData<T>()
