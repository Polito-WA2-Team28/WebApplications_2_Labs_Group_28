class Reactor<T>() {

    /**
     * Abstract class of a single cell in the reactor system.
     * Each cell must have at least two properties:
     *  - a value of type <T>
     *  - a list of listeners, i.e. a list of dependent cells of the current one. This is done to
     *      keep track to whom it is necessary to propagate the information changes.
     */
    abstract inner class Cell {
        abstract val value: T
        internal val listeners: MutableList<ComputeCell> = mutableListOf<ComputeCell>()
    }

    /**
     * Inner class defining the input cell. Input cells are not dependent from any other cells.
     * When the value of the input cells changes, it triggers the propagation and the callbacks.
     */
    inner class InputCell(inputValue: T): Cell() {
        override var value: T = inputValue
            set(newValue) {
                field = newValue                            /* updating value       */
                listeners.forEach { it.recompute() }        /* propagating changes  */
                listeners.forEach { it.triggerCallbacks() } /* firing callbacks     */
            }
    }

    /**
     * Inner class defining the output cell. Output cells are dependent from other cells.
     * They receive the list of cells they are dependent from and listen to the value changes,
     * updating their own values and propagating the changes.
     */
    inner class ComputeCell private constructor(val newValue: () -> T): Cell() {

        /**
         * Value of the current cell. It depends on the value of the input
         * cells.
         */
        override var value: T = newValue()
            private set

        /**
         * Callback related properties.
         */
        private var lastCallbackValue: T = value
        private var counterCallback: Int = 0
        private var activeCallbacks: MutableMap<Int, (T) -> Any> = mutableMapOf<Int, (T) -> Any>()

        /**
         * Public constructor of the class. It receives in input:
         *  - a variable number of cells (the ones this cell is dependent from)
         *  - an operation to compute the value of this cell based on the values of its dependents
         *
         * Note that: when the cell is created, it is added to list of dependents for each input cells.
         */
        constructor(vararg inputs: Cell, operation: (List<T>) -> T) : this({ operation(inputs.map { it.value }) }) {
            for (cell in inputs) {
                cell.listeners.add(this)    /* adding this listening ComputeCell to the InputCell */
            }
        }

        /**
         * Chain propagation to all the listener of this ComputeCell.
         * In particular, this function is called for every listener when the value of a cell has changed.
         * It is recomputing the new value and propagating the changes to all the listeners (if the value
         * has, indeed, changed itself).
         */
        internal fun recompute() {
            val recomputed = newValue()
            if (recomputed == value) {
                return  /* In case this cell does not change value, it is useless to fire the propagation */
            } else {
                value = recomputed
                listeners.forEach { it.recompute() }
            }
        }

        /**
         * Back propagation to trigger all the callbacks. A callback is fired only when
         * the value changes.
         */
        internal fun triggerCallbacks() {
            if (value == lastCallbackValue) return      /* if the value does not change, do not trigger the callback */
            lastCallbackValue = value
            for (cb in activeCallbacks.values) {
                cb(value)
            }
            listeners.forEach { it.triggerCallbacks() }
        }

        /**
         * Add a callback to this specific cell. The callback can be then removed by interacting
         * with the Subscription object returned.
         *
         * @param callback lambda function specifying the callback to execute
         * @return the Subscription object used to remove the callback
         */
        fun addCallback(callback: (T) -> Any): Subscription {
            val currentCallback: Int = counterCallback
            counterCallback++
            activeCallbacks[currentCallback] = callback

            return object: Subscription {
                override fun cancel() {
                    activeCallbacks.remove(currentCallback)
                }
            }
        }
    }

    interface Subscription {
        fun cancel()
    }
}
