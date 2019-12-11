package tboyt.aoc2019.day06B

import tboyt.aoc2019.util.readDataFile
import tboyt.aoc2019.util.expectResult

fun main() {
    val testInput = readDataFile("day06-test-b")
    expectResult(countTransfers(testInput), 4)
}

data class Planet(val name: String) {
    val orbitChildren = arrayListOf<Planet>()
    var parentOrbit: Planet? = null;
}

fun findShortestRoute(start: Planet, targetName: String): ArrayList<String> {
    val searchTargets = if (start.parentOrbit != null) {
        listOf(start.parentOrbit).plus(start.orbitChildren)
    } else {
        start.orbitChildren
    }
    // TODO: ?!?!?!?!?
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
    println(route)
    return route.size
}
