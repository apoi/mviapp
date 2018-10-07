package apoi.mviapp.extensions

fun Any?.ifNull(block: () -> Unit) {
    if (this == null) {
        block()
    }
}
