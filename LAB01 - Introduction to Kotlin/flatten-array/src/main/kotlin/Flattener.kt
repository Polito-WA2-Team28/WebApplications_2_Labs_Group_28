object Flattener {

    /**
     * Accepts an arbitrarily-deep nested list-like structure and returns a flattened
     * structure without any nil/null values.
     *
     * @param source the collection of elements
     * @return a list containing the non-null elements of the original collection.
     */
    fun flatten(source: Collection<Any?>): List<Any> {
        return source.flatMap { when(it) {
            is Collection<*> -> flatten(it)
            else -> listOf(it)
        } }.filterNotNull()
    }
}
