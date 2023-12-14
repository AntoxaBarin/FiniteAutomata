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
        //println("${new_pair.first}, ${new_pair.second}")

        var Involved : HashMap<Int, MutableList<Int>> = hashMapOf()
        for (q in new_pair.first) {
            //for (r in 0 until Inv[q][new_pair.second]) {
            for (r in 0 until dfa.states_number) {
                if (Inv[q][new_pair.second][r] == 1) {
                    var i = Class[r]
                    if (!Involved.containsKey(i)) {
                        Involved[i] = emptyList<Int>().toMutableList()
                    }
                    Involved[i]!!.add(r) // add??

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
    return P
}

// Возвращает дополнение первого множества до второго (Пример: Q = {1, 2, 3}, F = {1} => Q\F = {2, 3})
fun coSet(firstSet : HashSet<Int>, secondSet: HashSet<Int>) : HashSet<Int> {
    var complement = HashSet<Int>()
    for (i in secondSet) {
        if (!firstSet.contains(i)) {
            complement.add(i)
        }
    }
    //println(complement)
    return complement
}

fun invMaker(numberOfSymbols : Int, numberOfStates : Int, transition : Map<Pair<String, String>, MutableList<String>>) : Array<Array<IntArray>> {
    var Inv = Array(numberOfStates) { Array(numberOfSymbols) { IntArray(numberOfStates) } }
    for (i in transition) {
        //println("${i.key.first} ${i.key.second} ${i.value}")
        for (j in i.value) {
            Inv[j.toInt()][i.key.second.toInt()][i.key.first.toInt()] = 1
        }
    }

    for (i in 0..<numberOfStates) {
        for (j in 0..<numberOfSymbols) {
            for (k in 0..<numberOfStates) {
                if (Inv[i][j][k] == 1) {
                    println("$i $j $k")
                }
            }
        }
    }
    println("===============")

    return Inv
}