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

package apoi.mviapp.freesound.arch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import apoi.mviapp.common.Action
import apoi.mviapp.common.Event
import apoi.mviapp.common.Result
import apoi.mviapp.common.State
import apoi.mviapp.freesound.arch.Dispatcher
import apoi.mviapp.freesound.arch.EventMapper
import apoi.mviapp.freesound.arch.LogEvent
import apoi.mviapp.freesound.arch.Logger
import apoi.mviapp.freesound.arch.store.Store
import io.reactivex.disposables.SerialDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

abstract class BaseViewModel<E : Event, A : Action, R : Result, S : State> : ViewModel<E, S>() {

    abstract val initialEvent: E
    abstract val eventMapper: EventMapper<E, A>
    abstract val dispatcher: Dispatcher<A, R>
    abstract val store: Store<R, S>

    private val events: PublishSubject<E> = PublishSubject.create()
    private val state: MutableLiveData<S> = MutableLiveData()
    private val disposable: SerialDisposable = SerialDisposable()

    private val logTag = BaseViewModel::class.java.simpleName
    private val logger = Logger()

    override fun bind() {
        events.startWith(initialEvent)
            .observeOn(Schedulers.computation())
            .doOnNext { logger.log(logTag, LogEvent.Event(it)) }
            .asUiEventFlowable()
            .map { eventMapper(it) }
            .doOnNext { logger.log(logTag, LogEvent.Action(it)) }
            .compose(dispatcher)
            .compose(store.reduceResult())
            .subscribe(
                { state.postValue(it) },
                { logger.log(logTag, LogEvent.Error(it)) })
            .let(disposable::set)
    }

    override fun events(event: E) {
        events.onNext(event)
    }

    override fun state(): LiveData<S> {
        return state
    }

    override fun onCleared() {
        disposable.dispose()
    }
}
