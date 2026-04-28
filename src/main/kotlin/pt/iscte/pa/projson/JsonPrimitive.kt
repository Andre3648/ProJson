package pt.iscte.pa.projson

class JsonPrimitive(val value: Any?) : JsonValue(){
    init{
        require(value == null || value is String || value is Number || value is Boolean){
            "JsonPrimitive supports only null, string, number ou boolean. result: ${value?.javaClass?.name}"
        }
    }

    override fun toString(): String = when (value){
        null -> "null"
        is String -> "\"${value
            .replace("\\","\\\\")
            .replace("\"","\\\"")
            .replace("\n","\\n")
            .replace("\r","\\r")
            .replace("\t","\\t")
        }\""
        is Number -> value.toString()
        is Boolean -> value.toString()
        else -> throw IllegalStateException("Given value:${value.javaClass.name}")
    }
}