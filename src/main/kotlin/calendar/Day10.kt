package calendar

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.chocosolver.solver.Model
import org.chocosolver.solver.search.strategy.Search
import org.chocosolver.solver.variables.IntVar
import kotlin.concurrent.atomics.AtomicInt
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.math.min

import Solver
import org.chocosolver.solver.search.limits.ICounter
import org.chocosolver.solver.search.limits.NodeCounter

class Day10 : Solver {

    private data class Machine(
        val part1TargetState: Int,
        val triggers: List<List<Int>>,
        val triggersAsInts: List<Int>,
        val part2TargetState: List<Int>,
    )

    private fun setBits(bitPositions: List<Int>): Int {
        var bitMask = 0
        for (bitPos in bitPositions) {
            bitMask = (bitMask or (1 shl bitPos))
        }
        return bitMask
    }

    private fun stateToInt(state: String): Int {
        val onPositions = state
            .trim('[')
            .trim(']')
            .mapIndexed { idx, ch ->
                if (ch == '#') {
                    idx
                } else {
                    -1
                }
            }.filterNot { it == -1 }.toList()
        return setBits(onPositions)
    }

    private fun applyBinaryTrigger(state: Int, trigger: Int): Int {
        return state xor trigger
    }

    private fun part1(machines: List<Machine>): String {
        val cache: MutableMap<Int, Int> = mutableMapOf()
        var result = 0

        fun inner(currentState: Int, targetState: Int, triggers: List<Int>, steps: Int) {
            val prevBest = cache.putIfAbsent(currentState, steps) ?: Int.MAX_VALUE
            if (steps >= prevBest) {
                return
            }

            cache[currentState] = steps
            for (trigger in triggers) {
                val newState = applyBinaryTrigger(currentState, trigger)
                inner(newState, targetState, triggers, steps + 1)
            }
        }

        for (machine in machines) {
            cache.clear()
            inner(0, machine.part1TargetState, machine.triggersAsInts, 0)
            result += cache[machine.part1TargetState]!!
        }
        return result.toString()
    }

    /**
     * My own implementation of a Diophantine equation solver. However, it is too slow for the
     * actual input, so I ended up using the Choco Solver library instead.
     */
//    private fun solveDiophantineSystem(machine: Machine): Int {
//        var solution = Int.MAX_VALUE
//
//        val numVariables = machine.triggers.size
//        val numEquations = machine.part2TargetState.size
//
//        val theoreticalBest = machine.part2TargetState.max()
//
//        // lower bounds are always 0
//        val upperBounds = IntArray(numVariables) { Int.MAX_VALUE }
//
//        val equations = Array(numEquations) { IntArray(numVariables) { 0 } }
//        val remainings = IntArray(numEquations) { idx -> machine.part2TargetState[idx] }
//
//        val assignments = IntArray(numVariables) { 0 }
//
//        for (equationIdx in machine.part2TargetState.indices) {
//            for (triggerIdx in machine.triggers.indices) {
//                val trigger = machine.triggers[triggerIdx]
//                for (variablePos in trigger) {
//                    if (variablePos == equationIdx) {
//                        equations[equationIdx][triggerIdx] = 1
//                        upperBounds[triggerIdx] = min(upperBounds[triggerIdx],
//                            machine.part2TargetState[equationIdx])
//                    }
//                }
//            }
//        }
//
//        fun inner(varIdx: Int, sumSoFar: Int) {
//
//            if (solution == theoreticalBest) {
//                // can't do better than this
//                return
//            }
//
//            if (sumSoFar >= solution)  {
//                // already worse than best found
//                return
//            }
//
//            if (varIdx >= numVariables) {
//                // check if all equations are satisfied
//                if (remainings.all { it == 0 }) {
//                    solution = min(solution, sumSoFar)
//                }
//                return
//            }
//
//            // check constraints
//            for (eqIdx in 0 until numEquations) {
//                if (remainings[eqIdx] < 0)
//                    return
//
//                var maxPossible = 0
//                // pre-caching doesn't help much here
//                for (v in varIdx until numVariables) {
//                    maxPossible += equations[eqIdx][v] * upperBounds[v]
//                }
//
//                if (remainings[eqIdx] > maxPossible) {
//                    return
//                }
//            }
//
//            var upperBound = upperBounds[varIdx]
//            for (eq in 0 until numEquations) {
//                if (equations[eq][varIdx] > 0) {
//                    upperBound = minOf(upperBound, remainings[eq])
//                }
//            }
//
//            if (upperBound < 0) return
//
//            for (value in 0..upperBound) {
//                assignments[varIdx] = value
//
//                if (value > 0) {
//                    for (eqIdx in 0 until numEquations) {
//                        remainings[eqIdx] -= equations[eqIdx][varIdx] * value
//                    }
//                }
//
//                inner(varIdx + 1, sumSoFar + value)
//
//                if (value > 0) {
//                    for (eqIdx in 0 until numEquations)
//                        remainings[eqIdx] += equations[eqIdx][varIdx] * value
//                }
//            }
//        }
//
//        inner(0, 0)
//        return solution
//    }
//
//    @OptIn(ExperimentalAtomicApi::class)
//    private fun part2(machines: List<Machine>): String {
//        var total = 0
//        runBlocking {
//            val limited = Dispatchers.Default.limitedParallelism(
//                (Runtime.getRuntime().availableProcessors() - 1).coerceAtLeast(1)
//            )
//            val solvedCount = AtomicInt(0)
//            total = machines.map { machine ->
//                async(limited) {
//                    val res = solveDiophantineSystem(machine)
//                    println("Solved ${solvedCount.fetchAndAdd(1)}/${machines.size} machines")
//                    res
//                }
//            }.awaitAll().sum()
//        }
//        return total.toString()
//    }

    private fun solveDiophantineSystem(machine: Machine): Int {
        val numVariables = machine.triggers.size
        val numEquations = machine.part2TargetState.size

        val theoreticalBest = machine.part2TargetState.max()

        // lower bounds are always 0
        val upperBounds = IntArray(numVariables) { Int.MAX_VALUE }

        val equations = Array(numEquations) { IntArray(numVariables) { 0 } }

        for (equationIdx in machine.part2TargetState.indices) {
            for (triggerIdx in machine.triggers.indices) {
                val trigger = machine.triggers[triggerIdx]
                for (variablePos in trigger) {
                    if (variablePos == equationIdx) {
                        equations[equationIdx][triggerIdx] = 1
                        upperBounds[triggerIdx] = min(
                            upperBounds[triggerIdx],
                            machine.part2TargetState[equationIdx]
                        )
                    }
                }
            }
        }

        val model = Model("DiophantineSolver")
        val variables = mutableListOf<IntVar>()

        for (varIdx in 0 until numVariables) {
            variables.add(model.intVar("x_$varIdx", 0, upperBounds[varIdx]))
        }

        for (eqIdx in 0 until numEquations) {
            model.scalar(
                variables.toTypedArray(),
                equations[eqIdx],
                "=",
                machine.part2TargetState[eqIdx]
            ).post()
        }

        val sumVar = model.intVar("sum", 0, upperBounds.sum())
        model.sum(variables.toTypedArray(), "=", sumVar).post()
        model.setObjective(Model.MINIMIZE, sumVar)

        val solver = model.solver

        // use a search strategy that tries to minimize the sum variable first
        solver.setSearch(
            Search.minDomLBSearch(sumVar),
            Search.lastConflict(
                Search.domOverWDegSearch(*variables.toTypedArray())
            )
        )

        solver.setNoGoodRecordingFromRestarts()
        solver.setLubyRestart(
            128,
            NodeCounter(model, 5000),
        300
        )

        var best = Int.MAX_VALUE
        while (solver.solve()) {
            best = sumVar.value
            if (best == theoreticalBest) break
        }

        return best
    }

    @OptIn(ExperimentalAtomicApi::class)
    private fun part2(machines: List<Machine>): String {
        var total = 0
        runBlocking {
            val limited = Dispatchers.Default.limitedParallelism(
                (Runtime.getRuntime().availableProcessors() - 1).coerceAtLeast(1)
            )
            val solvedCount = AtomicInt(0)
            total = machines.map { machine ->
                async(limited) {
                    val res = solveDiophantineSystem(machine)
                    println("Solved ${solvedCount.fetchAndAdd(1)}/${machines.size-1} machines")
                    res
                }
            }.awaitAll().sum()
        }

        return total.toString()
    }

    override fun run(): List<String> {
        val rawInput = getRawInput(10)
            .trim()
            .split('\n')
        val machines = mutableListOf<Machine>()
        for (line in rawInput) {
            val (state, rest) = line
                .trim()
                .split(' ', ignoreCase = true, limit = 2)
            val (triggers, tail) = Pair(
                rest.substringBeforeLast(' '),
                rest.substringAfterLast(' ')
            )
            val machine = Machine(
                stateToInt(state),
                triggers.split(' ')
                    .map { trigger ->
                        trigger.trim('(')
                            .trim(')')
                            .split(',')
                            .map { it.toInt() }
                    },
                triggers
                    .split(' ')
                    .map { trigger ->
                        setBits(
                            trigger
                                .trim('(')
                                .trim(')')
                                .split(',')
                                .map { it.toInt() }
                        )
                    },
                tail
                    .trim('{')
                    .trim('}')
                    .split(',')
                    .map { it.toInt() },
            )
            machines.add(machine)
        }

        return listOf(
            part1(machines),
            // commenting the second part out as it may take a long time to compute
            "Placeholder for part 2"
            // part2(machines)
        )
    }
}
