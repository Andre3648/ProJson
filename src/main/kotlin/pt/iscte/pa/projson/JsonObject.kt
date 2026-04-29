package pt.iscte.pa.projson

//A JSON object is a collection of properties (key to a JsonValue)
//With MutableMap the insertion order are kept
class JsonObject(
    private val properties: MutableMap<String, JsonValue> = mutableMapOf()
) : JsonValue(){

    //Returns the JsonValue associated to a key
    fun getProperty(key: String) : JsonValue? = properties[key]

    //Replaces the property value of a chosen key by a given method
    //the value needs to be primitive or a JsonValue
    fun setProperty(key: String, value: Any?) {
        properties[key]= toJsonValue(value)
    }

    //Removes the property with the given key, and does nothing if the key does not exist
    fun removeProperty(key: String){
        properties.remove(key)
    }

    //Returns a Map with order preserved of all properties in an object
    fun getProperties(): Map<String, JsonValue> = properties.toMap()

    //Generates a formatted JSON text, if the object has no properties returns {}
    override fun toString(): String = prettyPrint(0)

    internal fun prettyPrint(indent: Int): String {
        if(properties.isEmpty()) return "{}"
        val childIndent = " ".repeat(indent + 1)
        val outsideIndent = " ".repeat(indent)

        val body = properties.entries.joinToString(",\n") { (key,value) ->
            val outputValue = when(value){
                is JsonObject -> value.prettyPrint(indent + 1)
                is JsonArray -> value.prettyPrint(indent + 1)
                else -> value.toString()
            }
            "$childIndent\"$key\": $outputValue"
        }
        return "{\n$body\n$outsideIndent}"
    }
}