import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class HopcroftTest {

    @Test
    fun minimizeTest1() {
        val dfa = NDFA("data/dfa1.txt")

        var correctClasses = emptyList<HashSet<Int>>().toMutableList()
        correctClasses.add(hashSetOf(2, 3, 4))
        correctClasses.add(hashSetOf(5))
        correctClasses.add(hashSetOf(0, 1))

        val HopcroftResult = Hopcroft(dfa)
        assert(HopcroftResult.size == 3)
        assert(HopcroftResult.contains(correctClasses[0]))
        assert(HopcroftResult.contains(correctClasses[1]))
        assert(HopcroftResult.contains(correctClasses[2]))
    }


    @Test
    fun minimizeTest2() {
        val dfa = NDFA("data/dfa2.txt")
        // В этом примере куча недостижимых состояний

        var correctClasses = emptyList<HashSet<Int>>().toMutableList()
        correctClasses.add(hashSetOf(2))
        correctClasses.add(hashSetOf(1))
        correctClasses.add(hashSetOf(0))

        val HopcroftResult = Hopcroft(dfa)
        assert(HopcroftResult.size == 3)
        assert(HopcroftResult.contains(correctClasses[0]))
        assert(HopcroftResult.contains(correctClasses[1]))
        assert(HopcroftResult.contains(correctClasses[2]))
    }

    @Test
    fun minimizeTest3() {
        val dfa = NDFA("data/dfa3.txt")
        // Пример из лекции

        var correctClasses = emptyList<HashSet<Int>>().toMutableList()
        correctClasses.add(hashSetOf(0, 1))
        correctClasses.add(hashSetOf(2, 3))
        correctClasses.add(hashSetOf(4))

        val HopcroftResult = Hopcroft(dfa)
        assert(HopcroftResult.size == 3)
        assert(HopcroftResult.contains(correctClasses[0]))
        assert(HopcroftResult.contains(correctClasses[1]))
        assert(HopcroftResult.contains(correctClasses[2]))
    }

}