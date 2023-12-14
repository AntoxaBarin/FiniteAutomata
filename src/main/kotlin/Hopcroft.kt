fun Hopcroft(dfa : NDFA?) : List<HashSet<Int>> {
    var P = emptyList<HashSet<Int>>().toMutableList()
    var Queue : Queue<Pair<HashSet<Int>, Int>> = LinkedList<Pair<HashSet<Int>, Int>>()
    var Class = Array(dfa!!.states_number) { 0 }
    var Inv : Array<Array<IntArray>>


    Inv = invMaker(dfa!!.alphabet_size, dfa.states_number, dfa.states)

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


    for (i in 0 until dfa.states_number) {
        if (i in F) {
            Class[i] = 0
        }
        else if (i in coSet(F, Q)) {
            Class[i] = 1
        }
    }

    for (c in 0 until dfa!!.alphabet_size) {
        Queue.add(Pair(F, c))           // insert <F, c> into queue
        Queue.add(Pair(coSet(F, Q), c)) // insert <Q \ F, c> into queue
    }

    while (Queue.size != 0) {

        var new_pair = Queue.remove()

        var Involved : HashMap<Int, MutableList<Int>> = hashMapOf()
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
        for (i in Involved.keys) {
            if (Involved[i]!!.size < P[i].size) {
                P.add(emptySet<Int>().toHashSet())
                var j = P.size - 1
                for (r in Involved[i]!!) {
                    P[i].remove(r)
                    P[j].add(r)
                }
                if (P[j].size > P[i].size) {
                    var tmp = P[j]
                    P[j] = P[i]
                    P[i] = tmp
                }
                for (r in P[j]) {
                    Class[r] = j
                }
                for (c in 0 until dfa!!.alphabet_size) {

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

fun invMaker(numberOfSymbols : Int, numberOfStates : Int, transition : Map<Pair<String, String>, MutableList<String>>) : Array<Array<IntArray>> {
    var Inv = Array(numberOfStates) { Array(numberOfSymbols) { IntArray(numberOfStates) } }
    for (i in transition) {
        for (j in i.value) {
            Inv[j.toInt()][i.key.second.toInt()][i.key.first.toInt()] = 1
        }
    }
    return Inv
}


fun removeUnreachableStates(dfa : NDFA, classes : MutableList<HashSet<Int>>) : MutableList<HashSet<Int>> {
    var outOfJ = false
    var outOfK = false
    var result = emptyList<HashSet<Int>>().toMutableList()

    for (i in classes) {
        println("i: $i")
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
