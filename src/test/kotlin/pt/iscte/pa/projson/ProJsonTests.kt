package pt.iscte.pa.projson

import kotlin.test.Test
import kotlin.test.assertEquals

class ProJsonTests {

    @Test
    fun testNull() {
        val json = ProJson().toJson(null)
        assertEquals(null, (json as JsonPrimitive).value)
    }

    @Test
    fun testString() {
        val json = ProJson().toJson("Andre")
        assertEquals("Andre", (json as JsonPrimitive).value)
    }

    @Test
    fun testInteger() {
        val json = ProJson().toJson(28)
        assertEquals(28, (json as JsonPrimitive).value)
    }

    @Test
    fun testBoolean() {
        val json = ProJson().toJson(false)
        assertEquals(false, (json as JsonPrimitive).value)
    }

    @Test
    fun testEmptyCollection() {
        val json = ProJson().toJson(emptyList<Any>())
        assertEquals(0, (json as JsonArray).size())
    }

    @Test
    fun testCollection() {
        val json = ProJson().toJson(listOf("Andre", null, 28)) as JsonArray
        val result = json.getElements()
        assertEquals(3, result.size)
        assertEquals("Andre", (result[0] as JsonPrimitive).value)
        assertEquals(null, (result[1] as JsonPrimitive).value)
        assertEquals(28, (result[2] as JsonPrimitive).value)
    }

    @Test
    fun testMap() {
        val json = ProJson().toJson(mapOf("name" to "Andre", "number" to 28)) as JsonObject
        assertEquals(null, json.getProperty($$"$type"))
        assertEquals("Andre", (json.getProperty("name") as JsonPrimitive).value)
        assertEquals(28, (json.getProperty("number") as JsonPrimitive).value)
    }

    @Test
    fun testEmptyMap() {
        val json = ProJson().toJson(emptyMap<String, Any>()) as JsonObject
        assertEquals(0, json.getProperties().size)
    }
    @Test
    fun testNestedCollection() {
        val json = ProJson().toJson(listOf(listOf(null, 28), listOf("Andre", 47))) as JsonArray
        assertEquals(2, json.size())
        assertEquals(2, (json.get(0) as JsonArray).size())
    }
    @Test
    fun testObjectWithNullProperty() {
        val json = ProJson().toJson(Task("T1", null, emptyList())) as JsonObject
        assertEquals(null, (json.getProperty("deadline") as JsonPrimitive).value)
    }

    @Test
    fun testObjectWithType() {
        val json = ProJson().toJson(Date(28,4,2026)) as JsonObject
        val type = json.getProperty($$"$type")
        assertEquals("Date", (type as JsonPrimitive).value)
        assertEquals(28, (json.getProperty("day") as JsonPrimitive).value)
        assertEquals(4, (json.getProperty("month") as JsonPrimitive).value)
        assertEquals(2026, (json.getProperty("year") as JsonPrimitive).value)
    }

    @Test
    fun testObjectOutput() {
        val json = ProJson().toJson(Date(31, 4, 2026)) as JsonObject
        json.setProperty("year", 2027)
        val keys = json.getProperties().keys.toList()
        assertEquals(listOf($$"$type", "day", "month", "year"), keys)
        val expected = $$"""
            {
             "$type": "Date",
             "day": 31,
             "month": 4,
             "year": 2027
            }
        """.trimIndent()
        assertEquals(expected, json.toString())
    }

    @Test
    fun testaTaskObject() {
        data class Date(val day: Int, val month: Int, val year: Int)
        data class Task(val description: String, val deadline: Date?, val dependencies: List<Task>)

        val t1 = Task("T1", Date(30, 2, 2026), emptyList())
        val t2 = Task("T2", Date(31, 4, 2026), emptyList())
        val t3 = Task("T3", null, listOf(t1, t2))
        val all = listOf(t1, t2, t3)

        val json = ProJson().toJson(all) as JsonArray

        assertEquals(3, json.size())

        val jsonT1 = json.get(0) as JsonObject
        assertEquals("T1", (jsonT1.getProperty("description") as JsonPrimitive).value)

        val jsonT3 = json.get(2) as JsonObject
        assertEquals("T3", (jsonT3.getProperty("description") as JsonPrimitive).value)
        assertEquals(null,(jsonT3.getProperty("deadline") as JsonPrimitive).value)

        val deps = jsonT3.getProperty("dependencies") as JsonArray
        assertEquals(2, deps.size())
    }

    @Test
    fun testCollectionOutput() {
        val json = ProJson().toJson(listOf(Date(30, 2, 2026), Date(31, 4, 2026))) as JsonArray
        val expected = $$"""
        [
         {
          "$type": "Date",
          "day": 30,
          "month": 2,
          "year": 2026
         },
         {
          "$type": "Date",
          "day": 31,
          "month": 4,
          "year": 2026
         }
        ]
    """.trimIndent()
        assertEquals(expected, json.toString())
    }

    @Test
    fun testTaskOutput() {
        val json = ProJson().toJson(Task("T1", Date(30, 2, 2026), emptyList())) as JsonObject
        val expected = $$"""
        {
         "$type": "Task",
         "description": "T1",
         "deadline": {
          "$type": "Date",
          "day": 30,
          "month": 2,
          "year": 2026
         },
         "dependencies": []
        }
    """.trimIndent()
        assertEquals(expected, json.toString())
    }
}