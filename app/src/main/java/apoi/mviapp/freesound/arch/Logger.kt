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

import timber.log.Timber

class Logger {

    fun log(tag: String, logEvent: LogEvent) {
        when (logEvent) {
            is LogEvent.Event -> Timber.d("MVI|$tag| Event => $logEvent")
            is LogEvent.Action -> Timber.d("MVI|$tag| Action => $logEvent")
            is LogEvent.Result -> Timber.d("MVI|$tag| Result => $logEvent")
            is LogEvent.Reduce -> Timber.d("MVI|$tag| Reduce => $logEvent")
            is LogEvent.State -> Timber.d("MVI|$tag| ViewState => $logEvent")
            is LogEvent.Error -> Timber.e(logEvent.throwable, "MVI|$tag| Fatal Error => $logEvent")
        }
    }
}

sealed class LogEvent {
    data class Event(val event: apoi.mviapp.common.Event) : LogEvent()
    data class Action(val action: apoi.mviapp.common.Action) : LogEvent()
    data class Result(val result: apoi.mviapp.common.Result) : LogEvent()
    data class Reduce(val result: apoi.mviapp.common.Result, val prevState: apoi.mviapp.common.State) : LogEvent()
    data class State(val state: apoi.mviapp.common.State) : LogEvent()
    data class Error(val throwable: Throwable) : LogEvent()
}
