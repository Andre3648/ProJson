package pt.iscte.pa.projson

// Abstract class that represents any JSON value (All JSON types extend this class)
sealed class JsonValue {
    abstract override fun toString(): String
}

//Converts any compatible value to JsonValue
//The value can be a JsonValue directly or any primitive type (String, Number, Boolean or null), converts this to JsonPrimitive
//If the given value is not supported throws an exception
internal fun toJsonValue(value: Any?): JsonValue = when(value){
    is JsonValue -> value
    null, is String, is Number, is Boolean -> JsonPrimitive(value)
    else -> throw IllegalArgumentException("${value.javaClass.name} is not a simple JsonValue")
}

// Traverses the entire JSON tree
//is depth-first, the current node is visited before its children
//JsonPrimitive e JsonReference are nodes with no children
fun JsonValue.accept(visitor: (JsonValue) -> Unit) {
    visitor(this)
    when (this) {
        is JsonObject -> this.getProperties().values.forEach { it.accept(visitor) }
        is JsonArray  -> this.getElements().forEach { it.accept(visitor) }
        else -> {}
    }
}