package aoc2015

object Day21
{
    private data class Item(val cost: Int, val damage: Int, val armor: Int)

    private val weapons = listOf(
            Item(8, 4, 0),
            Item(10, 5, 0),
            Item(25, 6, 0),
            Item(40, 7, 0),
            Item(74, 8, 0)
    )

    private val armors = listOf(
            Item(13, 0, 1),
            Item(31, 0, 2),
            Item(53, 0, 3),
            Item(75, 0, 4),
            Item(102, 0, 5)
    )

    private val rings = listOf(
            Item(25, 1, 0),
            Item(50, 2, 0),
            Item(100, 3, 0),
            Item(20, 0, 1),
            Item(40, 0, 2),
            Item(80, 0, 3)
    )

    private data class Entity(var hp: Int, val damage: Int, val armor: Int)

    private data class Inventory(
            val weapon: Item,
            val armor: Item? = null,
            val rings: List<Item> = emptyList()
    ) {
        fun cost(): Int {
            return weapon.cost + rings.map { it.cost }.sum() + (armor?.cost ?: 0)
        }
    }

    private val boss = Entity(hp = 104, damage = 8, armor = 1)
    private val baseStats = Entity(hp = 100, damage = 0, armor = 0)

    private fun Entity.withInventory(inventory: Inventory) = Entity(
            hp,
            damage + inventory.weapon.damage + inventory.rings.map { it.damage }.sum(),
            armor + (inventory.armor?.armor ?: 0) + inventory.rings.map { it.armor }.sum()
    )

    private fun Inventory.isWinningInventory(): Boolean {
        val boostedStats = baseStats.withInventory(this)
        val bossDamage = Math.max(1, boss.damage - boostedStats.armor)
        val playerDamage = Math.max(1, boostedStats.damage - boss.armor)
        val playerTurns = Math.ceil(boss.hp.toDouble() / playerDamage).toInt()
        val bossTurns = Math.ceil(boostedStats.hp.toDouble() / bossDamage).toInt()
        return playerTurns <= bossTurns
    }

    private fun allLoadouts(): List<Inventory> {
        return weapons.flatMap { weapon ->
            armors.flatMap { listOf(it, null) }.flatMap { armor ->
                rings.flatMap { listOf(it, null) }.flatMap { ring1 ->
                    rings.filter { it !== ring1 }.flatMap { listOf(it, null) }.map { ring2 ->
                        Inventory(weapon, armor, listOfNotNull(ring1, ring2))
                    }
                }
            }
        }
    }

    fun run() {
        val allLoadouts = allLoadouts()
        allLoadouts.filter {
            it.isWinningInventory()
        }.minByOrNull {
            it.cost()
        }?.cost()?.also { println(it) }

        allLoadouts.filterNot {
            it.isWinningInventory()
        }.maxByOrNull {
            it.cost()
        }?.cost()?.also { println(it) }
    }
}

fun main() {
    Day21.run()
}