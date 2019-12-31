package aoc2019

import util.RingBuffer
import util.arrayDequeOf

private object Day23  {
    
    private val codes = loadIntCodeInstructions("inputs/2019/23.txt")
    
    private data class Packet(val x: Long, val y: Long)
    
    private enum class InputMode { INIT, X, Y }
    
    private class NetworkNode(
            val code: Int,
            val network: Network
    ) {
        val inputQueue = arrayDequeOf<Packet>()
        private var outputMode = 0
        private var inputMode = InputMode.INIT
        private var packet: Packet? = null
        private var dst: Long = 0
        private var outputX: Long = 0
        private var outputY: Long = 0
        private var waitingOnInput = false
        
        val processor = intCodeProcessor(codes) {
            inputSupplier = { getInput().also { pause() } }
            outputConsumer = { handleOutput(it); pause() }
        }
        
        fun isIdle() = inputQueue.isEmpty() && waitingOnInput
        
        private fun handleOutput(value: Long) {
            when (outputMode) {
                0 -> dst = value
                1 -> outputX = value
                2 -> {
                    outputY = value
                    network.routePacket(dst, outputX, outputY)
                }
                else -> throw Exception("bad output mode: $outputMode")
            }
            outputMode = (outputMode + 1) % 3
            waitingOnInput = false
        }
        
        private fun getInput(): Long {
            val nextInput = when(inputMode) {
                InputMode.INIT -> {
                    inputMode = InputMode.X
                    code.toLong()
                }
                InputMode.X -> {
                    inputMode = InputMode.Y
                    packet = inputQueue.poll()
                    packet?.x
                }
                InputMode.Y -> {
                    inputMode = InputMode.X
                    packet?.y
                }
            }
            return if (nextInput == null) {
                waitingOnInput = true
                -1L
            } else {
                nextInput
            }
        }
    }

    private class Network {
        private val nodes = Array(50) { NetworkNode(it, this) }
        private var running = true
        private var natPacket: Packet? = null
        
        private var natPacketsSent = RingBuffer<Packet>(2)
        
        fun routePacket(dst: Long, x: Long, y: Long) {
            val packet = Packet(x, y)
            //println("sending packet $packet to $dst")
            if (dst == 255L) {
                natPacket = packet
                println("NAT received packet $packet")
            } else {
                nodes[dst.toInt()].inputQueue.add(packet)
            }
        }
        
        fun monitorNodes() {
            if (nodes.all { it.isIdle()} && natPacket != null) {
                println("NAT sending packet $natPacket")
                nodes[0].inputQueue.add(natPacket!!)
                natPacketsSent.add(natPacket!!)
                if (natPacketsSent.size == 2 && natPacketsSent[0].y == natPacketsSent[1].y) {
                    println(natPacketsSent[0])
                    running = false
                }
                natPacket = null
            }
        }
        
        fun findFirstPacketTo255() {
            while (running) {
                nodes.forEach { it.processor.run() }
                monitorNodes()
            }
        }
    }
    
    fun run() {
        val network = Network()
        network.findFirstPacketTo255()
    }
}

fun main() {
    Day23.run()
}