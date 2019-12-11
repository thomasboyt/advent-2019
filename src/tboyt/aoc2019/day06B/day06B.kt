package tboyt.aoc2019.day06B

import tboyt.aoc2019.util.readDataFile
import tboyt.aoc2019.util.expectResult

fun main() {
    val testInput = readDataFile("day06-test-b")
    expectResult(countTransfers(testInput), 4)
    val input = readDataFile("day06")
    println(countTransfers(input))
}

data class Planet(val name: String) {
    val orbitChildren = arrayListOf<Planet>()
    var parentOrbit: Planet? = null;
}

/**
 * Breadth first strategy. Look 1 node out, 2 nodes out, 3 nodes out, etc
 * until target node is found. Then it's that many nodes.
 */
fun findShortestRoute(start: Planet, targetName: String): Int {
    val seenPlanets = mutableSetOf<Planet>()
    val queue = arrayListOf<Planet>()

    fun checkPlanet(planet: Planet): Boolean {
        if (planet.name == targetName) {
            return true
        }
        var links = planet.orbitChildren
        if (planet.parentOrbit != null) {
            links.add(planet.parentOrbit!!)
        }
        val unseenLinks = links.minus(seenPlanets)
        unseenLinks.forEach { queue.add(it) }
        seenPlanets.addAll(unseenLinks)
        return false
    }

    queue.add(start)
    seenPlanets.add(start)

    var depth = 0
    while (queue.size > 0) {
        depth += 1

        val planetsForDepth = queue.toMutableList()
        queue.clear()

        for (planet in planetsForDepth) {
            val found = checkPlanet(planet)
            if (found) {
                return depth - 3
            }
        }
    }

    throw Exception("No route found")
}

fun countTransfers(input: List<String>): Int {
    val orbits = input.map { it.split(')') }
    val planetMap = mutableMapOf<String, Planet>()

    fun getOrAddPlanet(name: String): Planet {
        if (planetMap.contains(name)) {
            return planetMap[name]!!
        }
        planetMap[name] = Planet(name)
        return planetMap[name]!!
    }

    for (orbit in orbits) {
        val parentPlanet = getOrAddPlanet(orbit[0])
        val childPlanet = getOrAddPlanet(orbit[1])
        parentPlanet.orbitChildren.add(childPlanet)
        childPlanet.parentOrbit = parentPlanet
    }

    val youPlanet = planetMap["YOU"] ?: throw Exception("missing YOU planet")

    val route = findShortestRoute(youPlanet, "SAN")
    return route
}
