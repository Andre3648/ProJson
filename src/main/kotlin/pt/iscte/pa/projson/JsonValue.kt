package pt.iscte.pa.projson

sealed class JsonValue {
    abstract override fun toString(): String
}

internal fun toJsonValue(value: Any?): JsonValue = when(value){
    is JsonValue -> value
    null, is String, is Number, is Boolean -> JsonPrimitive(value)
    else -> throw IllegalArgumentException("${value.javaClass.name} is not a simple JsonValue")
}