package pt.iscte.pa.projson

//JSON Array is an ordered list of JsonValue elements, it can be any JsonValue such as JsonPrimitive, JsonObject or other JsonArray
class JsonArray(
    private val elements: MutableList<JsonValue> = mutableListOf()
) : JsonValue(){

    //Returns the element of the given index
    fun get(index: Int): JsonValue = elements[index]

    //Adds a new element to the end of the array
    fun add(value: Any?) {
        elements.add(toJsonValue(value))}

    //remove the element of the given index
    fun remove(index: Int) {
        elements.removeAt(index)}

    //get the number of elements in the array
    fun size(): Int = elements.size

    //returns a list of all elements, with the insertion order preserved
    fun getElements(): List<JsonValue> = elements.toList()

    //Generates a formatted JSON text, if the array is empty returns []
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