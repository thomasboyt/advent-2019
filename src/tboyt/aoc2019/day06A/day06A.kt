package tboyt.aoc2019.day06A

import tboyt.aoc2019.util.readDataFile
import tboyt.aoc2019.util.expectResult

fun main() {
    val testInput = readDataFile("day06-test")
    expectResult(countOrbits(testInput), 42)
    val input = readDataFile("day06")
    println(countOrbits(input))
}

data class Planet(val name: String) {
    val orbitChildren = arrayListOf<Planet>()
}

fun countPlanetOrbitChildren(planet: Planet, depthCount: Int): Int {
    return planet.orbitChildren.fold(planet.orbitChildren.size) {
        sum, planet -> sum + countPlanetOrbitChildren(planet, depthCount + 1) + depthCount
    }
}

fun countOrbits(input: List<String>): Int {
    val orbits = input.map { it.split(')') }
    val planetMap = mutableMapOf<String, Planet>()

    // strategy:
    // starting at COM, recurse down linked list

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
    }

    val comPlanet = planetMap["COM"] ?: throw Exception("missing COM planet")

    return countPlanetOrbitChildren(comPlanet, 0)
}
