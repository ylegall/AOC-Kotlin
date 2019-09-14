package aoc2016

import java.security.MessageDigest
import javax.xml.bind.DatatypeConverter


object Day14 {

    private const val SALT = "qzyelonm"

    private val md = MessageDigest.getInstance("MD5")

    private val tripleRegex = Regex("""(.)\1{2}""")
    private val quintRegex = Regex("""(.)\1{4}""")

    private data class Hash(
            val char: Char,
            val hash: String,
            val index: Int
    )

    private data class HashMatch(
            val char: Char,
            val tripleIndex: Int,
            val quintIndex: Int
    )

    private val tripleMap = HashMap<Char, List<Hash>>()

    private fun md5Sum(index: Int, cycles: Int = 0): String {
        md.reset()
        md.update("$SALT$index".toByteArray())
        repeat(cycles) {
            val lastDigest = md.digest().toHexString()
            md.reset()
            md.update(lastDigest.toByteArray())
        }
        val hash = md.digest()
        return hash.toHexString()
    }

    private fun ByteArray.toHexString() = DatatypeConverter.printHexBinary(this).toLowerCase()

    private fun findMatchingTriple(match: MatchResult, index: Int): Set<Hash> {
        val listOfTripples = getTripleFromMatch(match)
        val matchedTriples = listOfTripples.filter {
            index - it.index <= 1000
        }
        tripleMap[match.value[0]] = emptyList()
        return matchedTriples.toSet()
    }

    private fun storeTriple(match: MatchResult, fullHash: String, index: Int) {
        val listOfTripples = getTripleFromMatch(match)
        val newTripleList = listOfTripples.filter {
            index - it.index <= 1000
        } + Hash(match.value[0], fullHash, index)
        tripleMap[match.value[0]] = newTripleList
    }

    private fun getTripleFromMatch(match: MatchResult) : List<Hash> {
         return tripleMap[match.value[0]] ?: emptyList()
    }

    fun findKeys(cycles: Int = 0) {
        tripleMap.clear()
        var index = 0
        val keys = mutableListOf<HashMatch>()
        var stepsBeyond = 0
        while (stepsBeyond < 1000) {
            val hash = md5Sum(index, cycles)

            val quintuples = quintRegex.findAll(hash)
            for (match in quintuples) {
                val tripleMatches = findMatchingTriple(match, index).map { HashMatch(it.char, it.index, index) }
                keys.addAll(tripleMatches)
            }

            tripleRegex.find(hash)?.let { match ->
                storeTriple(match, hash, index)
            }

            if (keys.size >= 64) {
                stepsBeyond++
            }
            index++
        }
        keys.toList().sortedBy { it.tripleIndex }.take(64).forEachIndexed { i, it -> println("$i: $it") }
    }
}

fun main() {
    Day14.findKeys()
    Day14.findKeys(2016)
}