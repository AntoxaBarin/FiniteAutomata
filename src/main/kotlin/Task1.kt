import java.io.File

class NDFA (filename: String) {
    var alphabet_size : Int = 0
    var states_number : Int = 0
    var dfa_start_state : String = "-1"
    var states : Map<Pair<String, String>, MutableList<String>> = emptyMap()
    lateinit var accepting_states : List<String>
    lateinit var start_states : List<String>

    init {
        changeMachineConfiguration(filename)
    }

    fun changeMachineConfiguration(filename : String) : Unit {
        val file_lines = readFileLines(filename)
        var line_buffer : String
        var parse_buffer : List<String>

        states_number = file_lines[0].toInt()
        alphabet_size = file_lines[1].toInt()

        start_states = file_lines[2].split(" ")
        if (start_states.size == 1) {
            dfa_start_state = start_states[0]
        }

        accepting_states = file_lines[3].split(" ")

        for (i in 4..file_lines.size - 1) {
            line_buffer = file_lines[i]
            parse_buffer = line_buffer.split(" ")

            if (states.containsKey(Pair(parse_buffer[0], parse_buffer[1]))) {
                states[Pair(parse_buffer[0], parse_buffer[1])]!!.add(parse_buffer[2])
            }
            else {
                states += Pair(Pair(parse_buffer[0], parse_buffer[1]), mutableListOf(parse_buffer[2]))
            }
        }


    }

    /*
    На каждой итерации будем поддерживать список текущих состояний
    На первой итерации список состоит из одного состояния - начального,
    Далее на каждой следующей итерации мы берем каждое состояние, переходим по нему в следующие
    Таким образом, после прочтения всех символов у нас будет список из состояний, в которых остановился nfa в
    конце каждого из своих вычислений. Если найдется хотя бы одно принимающее - строка принимается, иначе - отвергается.
     */

    fun calculation(string: String) : Boolean {     // Вычисление DFA/NFA на строке

        for (i in start_states) {
            var current_states : MutableList<String> = mutableListOf(i)

            for (i in string) {
                var temp_list : MutableList<String> = mutableListOf()

                for (j in 0..current_states.size - 1) {
                    if (states.containsKey(Pair(current_states[j], i.toString()))) {
                        states[Pair(current_states[j], i.toString())]!!.forEach{ temp_list.add(it) }
                    }
                }
                current_states = temp_list
            }

            current_states.forEach {
                if (it in accepting_states) {
                    return true
                }
            }
        }

        return false
    }
}

fun readFileLines(filename : String) : List<String> = File(filename).useLines { it.toList() }