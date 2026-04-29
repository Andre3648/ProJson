package pt.iscte.pa.projson

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class JsonArrayTests {
    @Test
    fun testAdd() {
        val json = JsonArray()
        json.add("Andre")
        assertEquals("Andre", (json.get(0) as JsonPrimitive).value)
    }

    @Test
    fun testAddNull() {
        val json = JsonArray()
        json.add(null)
        assertEquals(null, (json.get(0) as JsonPrimitive).value)
    }

    @Test
    fun testAddTwo() {
        val json = JsonArray()
        json.add("Andre")
        assertEquals("[\"Andre\"]", json.toString())
    }

    @Test
    fun testAddMultiple() {
        val json = JsonArray()
        json.add("Andre")
        json.add(null)
        json.add(7)
        assertEquals("[\"Andre\", null, 7]", json.toString())
        assertEquals(3, json.size())
    }

    @Test
    fun testEmpty() {
        val json = JsonArray()
        assertEquals("[]", json.toString())
    }

    @Test
    fun testRemove() {
        val json = JsonArray()
        json.add("a")
        json.add("b")
        json.remove(0)
        assertEquals(1, json.size())
        assertEquals("b", (json.get(0) as JsonPrimitive).value)
    }

    @Test
    fun testGetElementos() {
        val json = JsonArray()
        json.add("a")
        json.add("b")
        assertEquals(2, json.getElements().size)
    }

    @Test
    fun testAddInvalidType() {
        val json = JsonArray()
        assertFailsWith<IllegalArgumentException> {
            json.add(listOf(28, null, "a"))
        }
    }

    @Test
    fun testRemoveInvalidIndex() {
        val json = JsonArray()
        json.add("Andre")
        assertFailsWith<IndexOutOfBoundsException> {
            json.remove(5)
        }
    }

    @Test
    fun testFormatLine() {
        val json = JsonArray()
        json.add("Andre")
        json.add(null)
        json.add(28)
        assertEquals("""["Andre", null, 28]""", json.toString())
    }

    //Analisar este teste devido ao inner array gerado
    @Test
    fun testArrayNested() {
        val json = JsonArray()
        val inner = JsonArray()
        inner.add("Andre")
        inner.add(2)
        json.add(inner)
        val expected = """
        [
         [
          "Andre",
          2
         ]
        ]
    """.trimIndent()
        assertEquals(expected, json.toString())
    }

    @Test
    fun testArrayWithObject() {
        val json = JsonArray()
        val obj = JsonObject()
        obj.setProperty("day", 28)
        obj.setProperty("name", null)
        json.add(obj)
        val expected = """
            [
             {
              "day": 28,
              "name": null
             }
            ]
        """.trimIndent()
        assertEquals(expected, json.toString())
    }

}