fun Hopcroft(dfa : NDFA?) : List<HashSet<Int>> {
    // Список всех классов эквивалентности
    var P = emptyList<HashSet<Int>>().toMutableList()
    // Очередь, будем хранить в ней пары <класс эквивалентости, символ из алфавита>
    var Queue : Queue<Pair<HashSet<Int>, Int>> = LinkedList<Pair<HashSet<Int>, Int>>()
    // Class[q] хранит номер класса эквивалентности, в котором находится состояние q
    var Class = Array(dfa!!.states_number) { 0 }
    // Inv[r][a][q] = 1, если существует переход из r в q по символу а
    var Inv : Array<Array<IntArray>>

    // Заполняем массив Inv
    Inv = invMaker(dfa!!.alphabet_size, dfa.states_number, dfa.states)

    // Заполняем первые классы эквивалентности
    var Q = HashSet<Int>()       // все состояния автомата
    for (i in 0 until dfa.states_number) {
        Q.add(i)
    }

    var F = HashSet<Int>()     // принимающие состояния автомата
    for (i in dfa.accepting_states) {
        F.add(i.toInt())
    }

    // изначально у нас два класса
    P.add(F)
    P.add(coSet(F, Q))

    // Заполняем массив Class
    for (i in 0 until dfa.states_number) {
        if (i in F) {
            Class[i] = 0
        }
        else if (i in coSet(F, Q)) {
            Class[i] = 1
        }
    }

    // Добавляем первые пары в очередь
    for (c in 0 until dfa!!.alphabet_size) {
        Queue.add(Pair(F, c))           // insert <F, c> into queue
        Queue.add(Pair(coSet(F, Q), c)) // insert <Q \ F, c> into queue
    }

    // Тот самый цикл из леммы  2
    while (Queue.size != 0) {

        // Достаем пару из очереди
        var new_pair = Queue.remove()

        // Involved - ассоциативный массив из номеров классов в массивы из номеров вершин
        var Involved : HashMap<Int, MutableList<Int>> = hashMapOf()
        // перебираем состояния из класса эквивалентности из пары
        for (q in new_pair.first) {
            for (r in 0 until dfa.states_number) {
                if (Inv[q][new_pair.second][r] == 1) {
                    var i = Class[r]
                    if (!Involved.containsKey(i)) {
                        Involved[i] = emptyList<Int>().toMutableList()
                    }
                    Involved[i]!!.add(r)

                }
            }
        }
        //Перебираем ключи
        for (i in Involved.keys) {
            if (Involved[i]!!.size < P[i].size) {
                //Создаем пустой класс в разбиении P
                P.add(emptySet<Int>().toHashSet())
                //j - индекс нового класса
                var j = P.size - 1
                for (r in Involved[i]!!) {
                    P[i].remove(r)
                    P[j].add(r)
                }
                // P[i] должен быть меньше P[j]
                if (P[j].size > P[i].size) {
                    var tmp = P[j]
                    P[j] = P[i]
                    P[i] = tmp
                }
                //Обновляем номера классов для вершин, у которых они изменились
                for (r in P[j]) {
                    Class[r] = j
                }
                for (c in 0 until dfa!!.alphabet_size) {
                    // Добавляем новую пару в очередь
                    Queue.add(Pair(P[j], c))
                }
            }
        }

    }

    println("Minimization finished. Equality classes:")
    var P_result = removeUnreachableStates(dfa, P)
    for (i in P_result) {
        print("$i ")
    }
    println()

    return P_result
}

// Возвращает дополнение первого множества до второго (Пример: Q = {1, 2, 3}, F = {1} => Q\F = {2, 3})
fun coSet(firstSet : HashSet<Int>, secondSet: HashSet<Int>) : HashSet<Int> {
    var complement = HashSet<Int>()
    for (i in secondSet) {
        if (!firstSet.contains(i)) {
            complement.add(i)
        }
    }
    return complement
}

// Эта функция заполняет массив Inv. По сути, Inv[r][a][q] = 1, если существует переход из r в q по символу а.
fun invMaker(numberOfSymbols : Int, numberOfStates : Int, transition : Map<Pair<String, String>, MutableList<String>>) : Array<Array<IntArray>> {
    var Inv = Array(numberOfStates) { Array(numberOfSymbols) { IntArray(numberOfStates) } }
    for (i in transition) {
        for (j in i.value) {
            Inv[j.toInt()][i.key.second.toInt()][i.key.first.toInt()] = 1
        }
    }
    return Inv
}

// Убираем классы эквивалентности с недостижимыми состояниями.
// Может показаться, что это довольно долгая операция, но тут мы будем часто выходить из циклов с помощью флагов, пропуская существенную часть итераций.
fun removeUnreachableStates(dfa : NDFA, classes : MutableList<HashSet<Int>>) : MutableList<HashSet<Int>> {
    var outOfJ = false
    var outOfK = false
    var result = emptyList<HashSet<Int>>().toMutableList()

    // Смотрим на состояние на класса эквивалентности, если есть переходы в него из других состояний, то этот класс не содержит недостижимых состояний, добавляем его в результирующий набор классов
    for (i in classes) {
        for (j in i) {
            for (k in dfa.states) {
                for (l in k.value) {
                    if (l == j.toString() && k.key.first != j.toString()) {
                        result.add(i)
                        outOfJ = true
                        outOfK = true
                        break
                    }
                }
                if (outOfK) {
                    outOfK = false
                    break
                }
            }
            if (outOfJ) {
                outOfJ = false
                break
            }
        }
    }
    return result
}
