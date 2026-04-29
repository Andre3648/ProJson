package pt.iscte.pa.projson

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertNull

class JsonObjectTests {

    @Test
    fun testSetProperty() {
        val json = JsonObject()
        json.setProperty("name", "Andre")
        assertEquals("\"Andre\"", json.getProperty("name").toString())
    }

    @Test
    fun testSetPropertyAll() {
        val json = JsonObject()
        json.setProperty("name", "Andre")
        val expected = """
        {
         "name": "Andre"
        }
    """.trimIndent()
        assertEquals(expected, json.toString())
    }

    @Test
    fun testEmpty() {
        val json = JsonObject()
        assertEquals("{}", json.toString())
    }

    @Test
    fun testNonExistProperty() {
        val json = JsonObject()
        assertNull(json.getProperty("name"))
    }

    @Test
    fun testRemoveProperty() {
        val json = JsonObject()
        json.setProperty("name", "Andre")
        json.removeProperty("name")
        assertNull(json.getProperty("name"))
    }

    //@Test
    //fun testValue() {
    //    val json = JsonObject()
    //    json.setProperty("name", "Andre")
    //    assertEquals(JsonPrimitive("Andre"), json.getProperty("name"))
    //}

    @Test
    fun testRewrite() {
        val json = JsonObject()
        json.setProperty("day", 28)
        json.setProperty("day", 5)
        val expected = """
            {
             "day": 5
            }
        """.trimIndent()
        assertEquals(expected, json.toString())
    }

    @Test
    fun testRewriteTwo() {
        val json = JsonObject()
        json.setProperty("day", 28)
        json.setProperty("day", 5)
        val result = json.getProperty("day") as JsonPrimitive
        assertEquals(5, result.value)
    }

    @Test
    fun testGetSize() {
        val json = JsonObject()
        json.setProperty("name", "Andre")
        json.setProperty("day", 5)
        assertEquals(2, json.getProperties().size)
    }

    @Test
    fun testSetPropertyNull() {
        val json = JsonObject()
        json.setProperty("data", null)
        val expected = """
        {
         "data": null
        }
    """.trimIndent()
        assertEquals(expected, json.toString())
    }

    @Test
    fun testObjectNested() {
        val json = JsonObject()
        val date = JsonObject()
        date.setProperty("day", 31)
        date.setProperty("month", 4)
        json.setProperty("data", date)
        val expected = """
        {
         "data": {
          "day": 31,
          "month": 4
         }
        }
    """.trimIndent()
        assertEquals(expected, json.toString())
    }

    @Test
    fun testSetPropertyInvalidType() {
        val json = JsonObject()
        assertFailsWith<IllegalArgumentException> {
            json.setProperty("key", listOf("Andre", null, 3, 28))
        }
    }

    // adicionar teste com array quando implementado e visitor no fim ou noutra classe

}