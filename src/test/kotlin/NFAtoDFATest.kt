import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File

class NFAtoDFATest {

    val firstFile = File("data/Test_2_nfa_output.txt")
    val secondFile = File("data/Test_2_dfa_prediction.txt")

    @Test       // Проверяем, что файл с предполагаемым DFA такой же как полученный файл С DFA, построенным из NFA
    fun compareOutputDFAandPredictedDFA() {
        NFAtoDFA("data/Test_2_nfa_input.txt", "data/Test_2_nfa_output.txt") // Строим DFA по NFA

        assertEquals(true, isEqualFiles(firstFile.toPath(), secondFile.toPath()))
    }

}