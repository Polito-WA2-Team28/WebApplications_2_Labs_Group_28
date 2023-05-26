class Matrix(private val matrixAsString: String) {

    /**
     * Constructing the rows by splitting the string into lines and
     * mapping each line in a list of integers.
     */
    private val rows: List<List<Int>> = matrixAsString
        .lines()
        .map { row -> row.trim().split(" ").map { it.toInt() } }

    /**
     * Constructing the columns by folding each row into a new list
     * of values.
     */
    private val columns: List<List<Int>> = rows
        .fold(List(rows[0].size) { mutableListOf<Int>() }) {
                acc, row ->
            row.forEachIndexed { index, value -> acc[index].add(value) }
            acc
        }

    /**
     * Return the specified column.
     * @param colNr the number of the column
     * @return a list containing the integers in the specified column.
     */
    fun column(colNr: Int): List<Int> {
        return columns[colNr - 1]
    }

    /**
     * Return the specified row.
     * @param rowNr the number of the row
     * @return a list containing the integers in the specified row.
     */
    fun row(rowNr: Int): List<Int> {
        return rows[rowNr - 1]
    }
}
