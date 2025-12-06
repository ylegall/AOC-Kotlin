package aoc2015

import aoc2015.Day22.Spell.*
import kotlin.math.min

private object Day22 {

    const val ENEMY_DAMAGE = 9

    enum class Spell(val cost: Int) {
        MISSILES(53),
        DRAIN(73),
        SHIELD(113),
        POISON(173),
        RECHARGE(229)
    }

    data class GameState(
            var hp: Int            = 50,
            var armor: Int         = 0,
            var mana: Int          = 500,
            var enemyHp: Int       = 51,
            var manaSpent: Int     = 0,
            var poisonTimer: Int   = 0,
            var shieldTimer: Int   = 0,
            var rechargeTimer: Int = 0
    )

    sealed class GameResult {
        data class Win(val manaSpent: Int): GameResult()
        object Loss: GameResult()
    }

    fun GameState.applyEffects() {
        if (shieldTimer > 0) {
            armor = 7
            shieldTimer -= 1
        } else {
            armor = 0
        }
        if (rechargeTimer > 0) {
            mana += 101
            rechargeTimer -= 1
        }
        if (poisonTimer > 0) {
            enemyHp -= 3
            poisonTimer -= 1
        }
    }

    private var minManaSoFar = Int.MAX_VALUE

    fun minManaToWin(hardMode: Boolean): Int {
        minManaSoFar = Int.MAX_VALUE
        return (minManaRecursive(hardMode = hardMode) as? GameResult.Win)?.manaSpent ?: -1
    }

    fun minManaRecursive(
        state: GameState = GameState(),
        wizardTurn: Boolean = true,
        hardMode: Boolean = false
    ): GameResult {
        if (hardMode && wizardTurn) state.hp -= 1
        if (state.hp <= 0) {
            return GameResult.Loss
        }

        state.applyEffects()

        if (state.enemyHp <= 0) {
            minManaSoFar = min(state.manaSpent, minManaSoFar)
            return GameResult.Win(state.manaSpent)
        }

        return if (wizardTurn) {
            val availableSpells = Spell.entries.filter {
                it.cost <= state.mana
            }.toMutableSet().apply {
                if (state.poisonTimer > 0)   remove(POISON)
                if (state.rechargeTimer > 0) remove(RECHARGE)
                if (state.shieldTimer > 0)   remove(SHIELD)
            }
            if (availableSpells.isEmpty()) {
                return GameResult.Loss
            }

            if (state.manaSpent > minManaSoFar) return GameResult.Loss

            availableSpells.map { nextSpell ->
                // cast spell
                val nextState = when (nextSpell) {
                    MISSILES -> state.copy(enemyHp = state.enemyHp - 4)
                    DRAIN    -> state.copy(hp = state.hp + 2, enemyHp = state.enemyHp - 2)
                    SHIELD   -> state.copy(shieldTimer = 6)
                    POISON   -> state.copy(poisonTimer = 6)
                    RECHARGE -> state.copy(rechargeTimer = 5)
                }.copy(
                        mana = state.mana - nextSpell.cost,
                        manaSpent = state.manaSpent + nextSpell.cost
                )
                if (nextState.enemyHp <= 0) {
                    minManaSoFar = min(state.manaSpent, minManaSoFar)
                    GameResult.Win(nextState.manaSpent)
                } else {
                    minManaRecursive(nextState, !wizardTurn, hardMode)
                }

            }.filterIsInstance<GameResult.Win>().minByOrNull {
                it.manaSpent
            } ?: GameResult.Loss

        } else {
            state.hp -= (ENEMY_DAMAGE - state.armor)
            if (state.hp <= 0) return GameResult.Loss
            minManaRecursive(state, !wizardTurn, hardMode)
        }
    }
}

fun main() {
    println(Day22.minManaToWin(false))
    println(Day22.minManaToWin(true))
}