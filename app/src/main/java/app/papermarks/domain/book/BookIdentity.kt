package app.papermarks.domain.book

object BookIdentity {
    fun bookIdFromIsbn13(isbn13: String): String = "isbn:${isbn13.filter(Char::isDigit)}"

    fun isValidEan13(value: String): Boolean {
        val digits = value.filter(Char::isDigit)
        if (digits.length != 13) return false
        val sum = digits.take(12).mapIndexed { index, c ->
            val n = c.digitToInt()
            if (index % 2 == 0) n else n * 3
        }.sum()
        val check = (10 - (sum % 10)) % 10
        return check == digits.last().digitToInt()
    }
}
