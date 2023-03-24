object WordCount {

    /**
     * Count the number of occurrences in the given input string. It exploits a regex
     * to parse the string and clean each word in the original sentence.
     *
     * @param phrase the input sentence.
     * @return a map containing, for each word, the number of its occurrences.
     */
    fun phrase(phrase: String): Map<String, Int> {
        val result: MutableMap<String, Int> = mutableMapOf<String, Int>()
        val cleanedPhrase: String = phrase.replace(Regex("[^\\w']+|(?<![a-z])'|'(?![a-z])"), " ")

        for (item in cleanedPhrase.trim().split("\\s+".toRegex())) {
            var cleanedItem = item.lowercase()
            result[cleanedItem] = result.getOrDefault(cleanedItem, 0) + 1
        }

        return result
    }
}