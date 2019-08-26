import io.kotlintest.matchers.file.shouldHaveExtension
import io.kotlintest.specs.StringSpec
import it.unibo.alchemist.core.implementations.Engine
import it.unibo.alchemist.core.interfaces.Status
import it.unibo.alchemist.loader.YamlLoader
import it.unibo.alchemist.model.implementations.positions.Euclidean2DPosition
import java.io.File
import java.util.concurrent.TimeUnit

class RunSimulationTest<T : Any?> : StringSpec() {

    companion object {
        const val directorySimulation = "src/main/yaml"
        const val simulationExtension = ".yml"
        const val runForSimulation = 5
    }

    init {
        val listOfSimulationPaths = File(directorySimulation).walk().filter { it.isFile }.toList()

        "extension of configuration files should be $simulationExtension" {
            listOfSimulationPaths.forEach{ it.shouldHaveExtension(simulationExtension) }
        }

        "run simulations not should throw exception" {
            listOfSimulationPaths
                .map { Pair(it, YamlLoader(it.inputStream()).getDefault<T, Euclidean2DPosition>()) }
                .map { Pair(it.first, Engine(it.second, runForSimulation.toLong()).also {
                    it.play()
                    it.run()
                }) }
                .forEach {
                    it.second.waitFor(Status.TERMINATED, 0, TimeUnit.MILLISECONDS)
                    assert(it.second.error.isEmpty) {
                        "Error in simulation of: ${it.second.error.get().message.orEmpty()}"
                    }
                }
        }
    }
}
