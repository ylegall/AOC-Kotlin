package aoc2021

import java.io.File


fun main() {

    data class Header(val version: Long, val typeId: Int)

    data class Literal(val value: Long, val finalIndex: Int)

    data class Packet(
        val header: Header,
        val literal: Literal?,
        val subPackets: List<Packet> = emptyList()
    ) {
        fun nextPacketIndex(): Int = literal?.finalIndex ?: subPackets.last().nextPacketIndex()
    }

    fun parseHeader(start: Int, bits: String): Header {
        return Header(
            bits.substring(start .. start + 2).toLong(2),
            bits.substring(start + 3 .. start + 5).toInt(2)
        )
    }

    fun parseLiteral(startIndex: Int, bits: String): Literal {
        var i = startIndex
        val sb = StringBuilder(bits.substring(i + 1 .. i + 4))
        while (bits[i] == '1') {
            i += 5
            sb.append(bits.substring(i + 1 .. i + 4))
        }
        return Literal(
            sb.toString().toLong(2),
            i + 5
        )
    }

    fun parsePacket(start: Int, bits: String): Packet {
        val header = parseHeader(start, bits)
        return if (header.typeId == 4) {
            val literal = parseLiteral(start + 6, bits)
            Packet(header, literal)
        } else {
            val lengthTypeId = bits[start + 6]
            val subPackets = mutableListOf<Packet>()
            if (lengthTypeId == '0') {
                val dataLength = bits.substring(start + 7 .. start + 21).toInt(2)
                var currentIndex = start + 22
                val finalIndex = currentIndex + dataLength - 1
                while (currentIndex < finalIndex) {
                    subPackets.add(parsePacket(currentIndex, bits))
                    currentIndex = subPackets.last().nextPacketIndex()
                }
            } else {
                val numPackets = bits.substring(start + 7 .. start + 17).toInt(2)
                var currentIndex = start + 18
                for (i in 0 until numPackets) {
                    subPackets.add(parsePacket(currentIndex, bits))
                    currentIndex = subPackets.last().nextPacketIndex()
                }
            }
            Packet(header, null, subPackets)
        }
    }

    fun versionSum(packet: Packet): Long {
        return packet.header.version + packet.subPackets.sumOf { subPacket ->
            versionSum(subPacket)
        }
    }

    fun operatorSum(packet: Packet): Long {
        return when (packet.header.typeId) {
            0 -> packet.subPackets.sumOf { operatorSum(it) }
            1 -> packet.subPackets.fold(1L) { product, subPacket -> product * operatorSum(subPacket) }
            2 -> packet.subPackets.minOf { operatorSum(it) }
            3 -> packet.subPackets.maxOf { operatorSum(it) }
            4 -> packet.literal?.value ?: 0L
            5 -> if (operatorSum(packet.subPackets[0]) > operatorSum(packet.subPackets[1])) 1L else 0L
            6 -> if (operatorSum(packet.subPackets[0]) < operatorSum(packet.subPackets[1])) 1L else 0L
            7 -> if (operatorSum(packet.subPackets[0]) == operatorSum(packet.subPackets[1])) 1L else 0L
            else -> throw Exception("bad type ID")
        }
    }

    fun part1(bits: String): Long {
        return versionSum(parsePacket(0, bits))
    }

    fun part2(bits: String): Long {
        return operatorSum(parsePacket(0, bits))
    }

    val bits = File("inputs/2021/input.txt").readLines()[0]
        .map { it.digitToInt(16).toString(2).padStart(4, '0') }
        .joinToString("")

    println(part1(bits))
    println(part2(bits))
}