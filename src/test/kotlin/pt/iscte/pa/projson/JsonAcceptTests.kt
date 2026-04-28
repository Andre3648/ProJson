package pt.iscte.pa.projson

import kotlin.test.Test
import kotlin.test.assertEquals

class JsonAcceptTests {


    @Test
    fun testVisitPrimitivo() {
        val json = JsonPrimitive("Andre")
        val visited = mutableListOf<JsonValue>()
        json.accept { visited.add(it) }
        assertEquals(1, visited.size)
    }

    @Test
    fun testVisitObject() {
        val json = JsonObject()
        json.setProperty("day", 28)
        json.setProperty("month", 1)
        val visited = mutableListOf<JsonValue>()
        json.accept { visited.add(it) }
        assertEquals(3, visited.size)
    }

    @Test
    fun testVisitArray() {
        val json = JsonArray()
        json.add("Andre")
        val obj = JsonObject()
        obj.setProperty("day", 28)
        json.add(obj)
        val visited = mutableListOf<JsonValue>()
        json.accept { visited.add(it) }
        assertEquals(4, visited.size)
    }

    @Test
    fun testFindAllPrimitives() {
        val json = JsonObject()
        json.setProperty("name", "Andre")
        json.setProperty("age", 20)
        val primitives = mutableListOf<JsonPrimitive>()
        json.accept { if (it is JsonPrimitive) primitives.add(it) }
        assertEquals(2, primitives.size)
    }

    @Test
    fun testFindNull() {
        val json = JsonArray()
        json.add("a")
        json.add(null)
        json.add("b")
        val nulls = mutableListOf<JsonPrimitive>()
        json.accept { if (it is JsonPrimitive && it.value == null) nulls.add(it) }
        assertEquals(1, nulls.size)
    }

    @Test
    fun testVisitNested() {
        val obj = JsonObject()
        val inner = JsonObject()
        inner.setProperty("day", 31)
        obj.setProperty("date", inner)
        val visited = mutableListOf<JsonValue>()
        obj.accept { visited.add(it) }
        assertEquals(3, visited.size)
    }
}