fun main() {
    val expr = "9x-9"
    val tokens = java.util.StringTokenizer(expr, "+-x/%", true)
    while (tokens.hasMoreTokens()) {
        val token = tokens.nextToken()
        if (token.isEmpty()) continue
        for (t in token){
            println(t)
        }
    }
}