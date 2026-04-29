package pt.iscte.pa.projson

import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.primaryConstructor

//Main entry point of the ProJson library, following the facade pattern
class ProJson{

    //Converts any value supported into its corresponding JsonValue
    //The conversion is recursive for nested objects, collections and maps
    fun toJson(obj: Any?): JsonValue = when (obj) {
        null             -> JsonPrimitive(null)
        is String        -> JsonPrimitive(obj)
        is Number        -> JsonPrimitive(obj)
        is Boolean       -> JsonPrimitive(obj)
        is Collection<*> -> convertCollection(obj)
        //is Array<*>      -> convertCollection(obj.toList())
        is Map<*, *>     -> convertMap(obj)
        else             -> convertObject(obj)
    }

    //Converts a Map to a JsonObject without a $type property
    //key value are converted to Strings and values are recursively converted
    private fun convertMap(map: Map<*, *>): JsonObject {
        val obj = JsonObject()
        map.forEach { (key, value) ->
            obj.setProperty(key.toString(), toJson(value))
        }
        return obj
    }

    //Converts a collection to a JsonArray by recursively converting each element
    private fun convertCollection(collection: Collection<*>): JsonArray {
        val array = JsonArray()
        collection.forEach { element ->
            array.add(toJson(element))
        }
        return array
    }

    //Converts any object to a JsonObject using reflection
    //Adds a $type property with the simple class name
    private fun convertObject(obj: Any): JsonObject {
        val json = JsonObject()
        val c = obj::class

        json.setProperty($$"$type", c.simpleName)

        c.primaryConstructor?.parameters?.forEach { param ->
            val prop = c.declaredMemberProperties.first { it.name == param.name }
            json.setProperty(param.name ?: "", toJson(prop.getter.call(obj)))
        }

        return json
    }
}

fun main() {
    data class Date(val day: Int, val month: Int, val year: Int)
    val result = ProJson().toJson(Date(31, 4, 2026)) as JsonObject
    result.setProperty("year", 2027)
    println(result)
    val list = listOf("Andre", null, 28)
    val json = ProJson().toJson(list) as JsonArray
    json.add("c")
    println(json)
    json.add(result)
    println(json)
}

