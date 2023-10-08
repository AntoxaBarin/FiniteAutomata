import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class Task1_Test {

    val DFA1 = NDFA("data/Test_1_dfa_1.txt")    // L = {0^n 1^m | n, m > 0}
    val DFA2 = NDFA("data/Test_1_dfa_2.txt")    // L = {2^n | n > 0}

    @Test
    fun DFA1() {
        assertEquals(true, DFA1.calculation("00111"))
        assertEquals(true, DFA1.calculation("01111"))
        assertEquals(true, DFA1.calculation("00000000000001"))

        assertEquals(false, DFA1.calculation("010"))
        assertEquals(false, DFA1.calculation("00101"))
        assertEquals(false, DFA1.calculation("1"))
    }

    @Test
    fun DFA2() {
        assertEquals(true, DFA2.calculation("22222222222222222"))
        assertEquals(true, DFA2.calculation("22222"))

        assertEquals(false, DFA2.calculation("010"))
        assertEquals(false, DFA2.calculation("222201"))
        assertEquals(false, DFA2.calculation("012"))
    }

    val NFA = NDFA("data/Test_1_nfa.txt")      // Автомат из условия задания 1

    @Test
    fun NFA() {
        assertEquals(true, NFA.calculation("0000"))
        assertEquals(true, NFA.calculation("11100"))
        assertEquals(true, NFA.calculation("0000000000000"))

        assertEquals(false, NFA.calculation("010"))
        assertEquals(false, NFA.calculation("1111111"))
        assertEquals(false, NFA.calculation("1"))
    }
}