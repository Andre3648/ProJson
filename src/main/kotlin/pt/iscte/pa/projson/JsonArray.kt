package pt.iscte.pa.projson

class JsonArray(
    private val elements: MutableList<JsonValue> = mutableListOf()
) : JsonValue(){

    fun get(index: Int): JsonValue = elements[index]

    fun add(value: Any?) {
        elements.add(toJsonValue(value))}

    fun remove(index: Int) {
        elements.removeAt(index)}

    fun size(): Int = elements.size

    fun getElements(): List<JsonValue> = elements.toList()

    override fun toString(): String =
        if (elements.isEmpty() || elements.all { it is JsonPrimitive }) //adicionar JsonReference mais tarde
            "[${elements.joinToString(", ")}]"
        else
            prettyPrint(0)

    internal fun prettyPrint(indent: Int): String {
        if(elements.isEmpty()) return "[]"
        val childIndent = " ".repeat(indent + 1)
        val outsideIndent = " ".repeat(indent)

        val things = elements.joinToString(",\n") {value ->
            val outputValue = when(value){
                is JsonObject -> value.prettyPrint(indent + 1)
                is JsonArray -> value.prettyPrint(indent + 1)
                else -> value.toString()
            }
            "$childIndent$outputValue"
        }
        return "[\n$things\n$outsideIndent]"
    }
}