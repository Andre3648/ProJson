package pt.iscte.pa.projson

class JsonObject(
    private val properties: MutableMap<String, JsonValue> = mutableMapOf()
) : JsonValue(){

    fun getProperty(key: String) : JsonValue? = properties[key]

    fun setProperty(key: String, value: Any?) {
        properties[key]= toJsonValue(value)
    }

    fun removeProperty(key: String){
        properties.remove(key)
    }

    fun getProperties(): Map<String, JsonValue> = properties.toMap()

    override fun toString(): String = prettyPrint(0)

    internal fun prettyPrint(indent: Int): String {
        if(properties.isEmpty()) return "{}"
        val childIndent = " ".repeat(indent + 1)
        val outsideIndent = " ".repeat(indent)

        val body = properties.entries.joinToString(",\n") { (key,value) ->
            val outputValue = when(value){
                is JsonObject -> value.prettyPrint(indent + 1)
                //is JsonArray -> value.prettyPrint(indent + 1)
                else -> value.toString()
            }
            "$childIndent\"$key\": $outputValue"
        }
        return "{\n$body\n$outsideIndent}"
    }
}