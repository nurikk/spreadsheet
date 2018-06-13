package mathparser

import java.util.HashMap

class Token (val type: String = "", val value: String ="") {
    private val assoc: HashMap<String, String> = HashMap()
    init {
        assoc["*"] = "left"
        assoc["/"] = "left"
        assoc["+"] = "left"
        assoc["-"] = "left"
    }

    private val prec: HashMap<String, Int> = HashMap()
    init {
        prec["*"] = 3
        prec["/"] = 3
        prec["+"] = 2
        prec["-"] = 2
    }

    fun precedence() :Int {
        return prec.getOrDefault(value, 0)
    }

    fun associativity(): String{
        return assoc.getOrDefault(value, "")
    }

    override fun toString(): String {
        return "$type ($value)"
    }


}