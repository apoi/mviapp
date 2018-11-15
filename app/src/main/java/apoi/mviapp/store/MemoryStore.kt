package apoi.mviapp.store

class MemoryStore<T, U> {

    private val cache: MutableMap<T, U> = mutableMapOf()

    fun put(id: T, item: U) {
        synchronized(cache) {
            cache.put(id, item)
        }
    }

    fun get(id: T): U? {
        synchronized(cache) {
            return cache[id]?.also { cache.remove(id) }
        }
    }
}
