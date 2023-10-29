import java.io.File

// Функция построения DFA из NFA методом "построения подмножеств"
fun NFAtoDFA(filename_input : String, filename_output : String) : Map< Pair<MutableList<String>?, String>, MutableList<String>? > {

    // Обычный счетчик, который будет нумеровать подмножества состояний
    var counter_set_states = 0

    // Будем хранить здесь короткие названия для множеств состояний нового DFA.
    var short_names_for_sets_of_states : Map< MutableList<String>?, Int > = emptyMap()

    // Строим NFA по конфигурации из файла
    var NFA = NDFA(filename_input)

    // Словарь будет хранить функцию перехода DFA. Ключ в формате множества состояний и символа, значение - множество состояний
    var DFA_transition_function : Map< Pair<MutableList<String>?, String>, MutableList<String>? > = emptyMap()
    var new_state : MutableList<String>? = mutableListOf()

    // Начинаем со стартового состояния NFA
    // Строим подмножества, в которые NFA переходит из стартового состояния
    for (i in 0..NFA.alphabet_size - 1) {
        val temp = Pair(mutableListOf(NFA.dfa_start_state), i.toString())
        if (NFA.states.containsKey(Pair(NFA.dfa_start_state, i.toString()))) {
            DFA_transition_function += Pair(temp, NFA.states[Pair(NFA.dfa_start_state, i.toString())])

            // Замечаем новое подмножество состояний
            if (!DFA_transition_function.containsKey(Pair(NFA.states[Pair(NFA.dfa_start_state, i.toString())], i.toString()))) {
                new_state = NFA.states[Pair(NFA.dfa_start_state, i.toString())]
            }
        }
    }

    // Список, в котором хранятся подмножества состояний, если в нём на очередной итерации не будет нового подмножества
    // это значит, что мы закончили построения подмножеств
    var last_generated_states : MutableList<MutableList<String>> = mutableListOf()
    var new_state_generated = true

    // Пока получаем новые подмножества состояний
    while (new_state_generated) {

        for (i in 0..NFA.alphabet_size - 1) {

            // Просчитываем значения функции перехода для новых подмножеств состояний
            var new_state_subset = calculateStatesSubset(NFA.states, i.toString(), new_state)
            DFA_transition_function += Pair(Pair(new_state, i.toString()), new_state_subset)

            last_generated_states.add(new_state_subset)
        }

        var temp_counter = 0
        for (i in last_generated_states) {
            if (!DFA_transition_function.containsKey(Pair(i, "0"))) {   // Нашли новое подмножество

                new_state_generated = true
                new_state = i
                break
            }
            else {
                temp_counter += 1
            }
        }

        // Если нет нового подмножества, то значит, что мы закончили построение
        if (temp_counter == last_generated_states.size) {
            new_state_generated = false
        }

    }

    // Задаем подмножествам короткие имена
    DFA_transition_function.forEach { elem ->
        if (!short_names_for_sets_of_states.containsKey(elem.key.first)) {
            short_names_for_sets_of_states += Pair(elem.key.first, counter_set_states++)
        }
    }

    // Записываем полученный DFA в файлик
    writeDFAtoFile(NFA.alphabet_size, NFA.states_number, NFA.dfa_start_state,
        DFA_transition_function, NFA.accepting_states, filename_output, short_names_for_sets_of_states)


    // Формат записи переходов в файле
    // a1, a2, ..., an s b1, b2, ..., bn
    // ai, bi - элементы подмножеств, s - символ

    //println(short_names_for_sets_of_states.map { "${it.key}: ${it.value}" }.joinToString(", "))


    println(short_names_for_sets_of_states.map { "${it.key}: ${it.value}" }.joinToString(", "))
    return DFA_transition_function

}

fun calculateStatesSubset(NFA_transition_function : Map<Pair<String, String>, MutableList<String>>,
                          symbol : String,
                          new_state : MutableList<String>?) : MutableList<String>
{

    // Вычисляеми все состояния, в которые можем добраться по всем символам из текущего подмножества
    var result : MutableList<String> = mutableListOf()
    for (i in 0..new_state!!.size.minus(1)) {
        if (NFA_transition_function.containsKey(Pair(new_state[i], symbol))) {
            NFA_transition_function[Pair(new_state[i], symbol)]!!.forEach {
                result.add(it)
            }
        }
    }

    return result
}

fun formatList(list : MutableList<String>? ) : String {
    val formattedString1: String = list.toString()
        .replace(",", "")
        .replace("[", "")
        .replace("]", "")
        .trim()

    return formattedString1
}

fun writeDFAtoFile(alp_size: Int,
                   states_num: Int,
                   dfa_start: String,
                   states_ : Map< Pair<MutableList<String>?, String>, MutableList<String>? >,
                   acc_states : List<String>,
                   filename: String,
                   short_names : Map< MutableList<String>?, Int >)
{


    val file = File(filename)
    val acc_states_string = acc_states.toString()
        .replace(",", "")
        .replace("[", "")
        .replace("]", "")
        .trim()

    var text = "${alp_size}\n${states_num}\n${dfa_start}\n${acc_states_string}\n"
    file.writeText(text, Charsets.UTF_8)

    for (i in states_) {
        file.appendText("${short_names[i.key.first]} ${i.key.second} ${short_names[i.value]}\n", Charsets.UTF_8)
    }
}
