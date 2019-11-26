package aoc2018

import aoc2018.Day24.Allegiance.IMMUNE
import aoc2018.Day24.Allegiance.INFECTION
import aoc2018.Day24.AttackType.BLUDGEONING
import aoc2018.Day24.AttackType.COLD
import aoc2018.Day24.AttackType.FIRE
import aoc2018.Day24.AttackType.RADIATION
import aoc2018.Day24.AttackType.SLASHING
import util.enumSetOf

object Day24 {

    private enum class Allegiance { IMMUNE, INFECTION }

    private enum class AttackType {
        FIRE,
        COLD,
        RADIATION,
        BLUDGEONING,
        SLASHING,
    }

    private data class Attack(val damage: Int, val type: AttackType)

    private data class Army(
            val allegiance: Allegiance,
            var units: Int,
            val hitPoints: Int,
            val weaknesses: Set<AttackType>,
            val immunities: Set<AttackType>,
            val attack: Attack,
            val initiative: Int
    ) {

        var id: Int? = null

        fun effectivePower() = units * attack.damage

        fun targetPreference(armies: List<Army>, selected: Set<Army>) =
            armies.filter { it.allegiance != allegiance && attack.type !in it.immunities && it !in selected }
                    .sortedWith(
                            compareByDescending<Army> { damageToTarget(it) }
                                    .thenByDescending { it.effectivePower() }
                                    .thenByDescending { it.initiative }
                    ).firstOrNull()

        fun attack(target: Army) {
            val killedUnits = (damageToTarget(target) / target.hitPoints).coerceAtMost(target.units)
//            if (killedUnits > 0) println("$allegiance group $id attacks defending group ${target.id}, killing $killedUnits units")
            target.units = (target.units - killedUnits).coerceAtLeast(0)
        }

        private fun damageToTarget(target: Army): Int {
            val multiplier = when (attack.type) {
                in target.weaknesses -> 2
                in target.immunities -> 0
                else -> 1
            }
            return effectivePower() * multiplier
        }
    }

    private val targetSelectionComparator = compareByDescending<Army> {
        it.effectivePower()
    }.thenByDescending {
        it.initiative
    }

    private val attackOrderComparator = compareByDescending<Map.Entry<Army, Army?>> { it.key.initiative }

    private class Battle(val armies: List<Army>) {

        fun run(): Allegiance {
            while (true) {
                val sizeBefore = armies.sumBy { it.units }
                step()
                if (isDefeated(IMMUNE) || isDefeated(INFECTION)) {
                    break
                }
                if (sizeBefore == armies.sumBy { it.units }) {
                    return INFECTION
                }
            }
            println("immune units: " + totalUnits(IMMUNE))
            println("infection units: " + totalUnits(INFECTION))
//            println("total units: " + totalUnits())
            return if (isDefeated(IMMUNE)) INFECTION else IMMUNE
        }

        private fun step() {
            val chosenUnits = HashSet<Army>()
            val aliveUnits = armies.filter { it.units > 0 }
            val targetMapping = aliveUnits.sortedWith(targetSelectionComparator).map { army ->
                val target = army.targetPreference(aliveUnits, chosenUnits)?.also { chosenUnits.add(it) }
                army to target
            }.toMap()

//            println()
            targetMapping.entries.sortedWith(attackOrderComparator).forEach { (attacker, defender) ->
                if (attacker.units > 0 && defender != null) {
                    attacker.attack(defender)
                }
            }
        }

        private fun totalUnits(allegiance: Allegiance) = armies.filter { it.allegiance == allegiance }.sumBy { it.units }
//        private fun totalUnits() = armies.filter { it.units > 0}

        private fun isDefeated(allegiance: Allegiance) = armies.filter {
            it.allegiance == allegiance
        }.all {
            it.units <= 0
        }
    }

    private fun configureBattle(boost: Int = 0): Battle {
        val battle = Battle(listOf(
             Army(IMMUNE,    916,  3041,  enumSetOf(COLD, FIRE), enumSetOf(),                       Attack(29 + boost, FIRE),       13),
             Army(IMMUNE,    1959, 7875,  enumSetOf(COLD), enumSetOf(SLASHING, BLUDGEONING),        Attack(38 + boost, RADIATION),  20),
             Army(IMMUNE,    8933, 5687,  enumSetOf(), enumSetOf(),                                 Attack(6 + boost, SLASHING),    15),
             Army(IMMUNE,    938,  8548,  enumSetOf(), enumSetOf(),                                 Attack(89 + boost, RADIATION),  4),
             Army(IMMUNE,    1945, 3360,  enumSetOf(RADIATION), enumSetOf(COLD),                    Attack(16 + boost, COLD),       1),
             Army(IMMUNE,    2211, 7794,  enumSetOf(SLASHING), enumSetOf(),                         Attack(30 + boost, FIRE),       12),
             Army(IMMUNE,    24,   3693,  enumSetOf(), enumSetOf(),                                 Attack(1502 + boost, FIRE),     5),
             Army(IMMUNE,    2004, 4141,  enumSetOf(), enumSetOf(RADIATION),                        Attack(18 + boost, SLASHING),   19),
             Army(IMMUNE,    3862, 3735,  enumSetOf(), enumSetOf(BLUDGEONING, FIRE),                Attack(9 + boost, FIRE),        10),
             Army(IMMUNE,    8831, 3762,  enumSetOf(RADIATION), enumSetOf(),                        Attack(3 + boost, FIRE),        7),
             Army(INFECTION, 578,  55836, enumSetOf(), enumSetOf(),                                 Attack(154, RADIATION), 9),
             Army(INFECTION, 476,  55907, enumSetOf(FIRE), enumSetOf(),                             Attack(208, COLD),      18),
             Army(INFECTION, 496,  33203, enumSetOf(FIRE, RADIATION), enumSetOf(COLD, BLUDGEONING), Attack(116, SLASHING),  14),
             Army(INFECTION, 683,  12889, enumSetOf(FIRE), enumSetOf(),                             Attack(35, BLUDGEONING),11),
             Army(INFECTION, 1093, 29789, enumSetOf(), enumSetOf(COLD, FIRE),                       Attack(51, RADIATION),  17),
             Army(INFECTION, 2448, 40566, enumSetOf(COLD), enumSetOf(BLUDGEONING, FIRE),            Attack(25, SLASHING),   16),
             Army(INFECTION, 1229, 6831,  enumSetOf(FIRE, COLD), enumSetOf(SLASHING),               Attack(8, BLUDGEONING), 8),
             Army(INFECTION, 3680, 34240, enumSetOf(FIRE, COLD), enumSetOf(BLUDGEONING),            Attack(17, RADIATION),  3),
             Army(INFECTION, 4523, 9788,  enumSetOf(), enumSetOf(BLUDGEONING, FIRE, SLASHING),      Attack(3, BLUDGEONING), 6),
             Army(INFECTION, 587,  49714, enumSetOf(BLUDGEONING), enumSetOf(),                      Attack(161, FIRE),      2)
        ))

//        val battle = Battle(listOf(
//                Army(IMMUNE, 17, 5390, enumSetOf(RADIATION, BLUDGEONING), enumSetOf(), Attack(4507, FIRE), 2),
//                Army(IMMUNE, 989, 1274, enumSetOf(BLUDGEONING, SLASHING), enumSetOf(FIRE), Attack(25, SLASHING), 3),
//                Army(INFECTION, 801, 4706, enumSetOf(RADIATION), enumSetOf(), Attack(116, BLUDGEONING), 1),
//                Army(INFECTION, 4485, 2961, enumSetOf(FIRE, COLD), enumSetOf(RADIATION), Attack(12, SLASHING), 4)
//        ))

        battle.armies.filter { it.allegiance == IMMUNE }.forEachIndexed { idx, army -> army.id = idx + 1 }
        battle.armies.filter { it.allegiance == INFECTION }.forEachIndexed { idx, army -> army.id = idx + 1 }

        return battle
    }

    fun findWinningTeam() {
        val battle = configureBattle()
        battle.run()
    }

    fun findUnitsForSmallestBoost() {
        var low = 1
        var high = 10_000
        while (low < high) {
            val mid = (low + high) / 2
            val battle = configureBattle(mid)
            val winner = battle.run()
            println("Computed battle for boost $mid: $winner")
            if (winner == IMMUNE) {
                high = mid
            } else {
                low = mid + 1
            }
        }
    }
}

// 5321 infection units is too low
fun main() {
    Day24.findWinningTeam()
    Day24.findUnitsForSmallestBoost()
}