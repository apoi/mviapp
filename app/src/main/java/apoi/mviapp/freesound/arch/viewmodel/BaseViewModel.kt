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
import apoi.mviapp.freesound.arch.Action
import apoi.mviapp.freesound.arch.Dispatcher
import apoi.mviapp.freesound.arch.Event
import apoi.mviapp.freesound.arch.EventMapper
import apoi.mviapp.freesound.arch.LogEvent
import apoi.mviapp.freesound.arch.Logger
import apoi.mviapp.freesound.arch.Result
import apoi.mviapp.freesound.arch.State
import apoi.mviapp.freesound.arch.store.Store
import io.reactivex.disposables.SerialDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class BaseViewModel<in E : Event, A : Action, R : Result, S : State>(
    initialEvent: E,
    eventMapper: EventMapper<E, A>,
    dispatcher: Dispatcher<A, R>,
    store: Store<R, S>,
    private val logTag: String,
    private val logger: Logger
) : ViewModel<E, S>() {

    private val uiEvents: PublishSubject<E> = PublishSubject.create()
    private val uiModel: MutableLiveData<S> = MutableLiveData()
    private val disposable: SerialDisposable = SerialDisposable()

    init {
        disposable.set(
            uiEvents.startWith(initialEvent)
                .observeOn(Schedulers.computation())
                .doOnNext { logger.log(logTag, LogEvent.Event(it)) }
                .asUiEventFlowable()
                .map { eventMapper(it) }
                .doOnNext { logger.log(logTag, LogEvent.Action(it)) }
                .compose(dispatcher)
                .compose(store.reduceResult())
                .subscribe(
                    { uiModel.postValue(it) },
                    { logger.log(logTag, LogEvent.Error(it)) })
        )
    }

    override fun uiEvents(uiEvent: E) {
        uiEvents.onNext(uiEvent)
    }

    override fun uiModels(): LiveData<S> {
        return uiModel
    }

    override fun onCleared() {
        disposable.dispose()
    }
}