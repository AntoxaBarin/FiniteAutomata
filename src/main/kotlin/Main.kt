import java.nio.file.Files
import java.nio.file.Path

fun isEqualFiles(firstFile: Path, secondFile: Path): Boolean {    // Сравнивает два файла
    if (Files.size(firstFile) != Files.size(secondFile)) {
        return false
    }
    Files.newBufferedReader(firstFile).use { bf1 ->
        Files.newBufferedReader(secondFile).use { bf2 ->
            var ch: Int
            while (bf1.read().also { ch = it } != -1) {
                if (ch != bf2.read()) {
                    return false
                }
            }
        }
    }
    return true
}

fun main() {

}


