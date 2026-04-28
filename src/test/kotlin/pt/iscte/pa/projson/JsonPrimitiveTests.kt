package pt.iscte.pa.projson

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class JsonPrimitiveTests {

    @Test
    fun testString() {
        assertEquals("\"teste\"", JsonPrimitive("teste").toString())
    }

    @Test
    fun testBoolean() {
        assertEquals("false", JsonPrimitive(false).toString())
        assertEquals("true", JsonPrimitive(true).toString())
    }

    @Test
    fun testNull() {
        assertEquals("null", JsonPrimitive(null).toString())
    }

    @Test
    fun testInt() {
        assertEquals("28", JsonPrimitive(28).toString())
    }

    @Test
    fun testDouble() {
        assertEquals("28.1", JsonPrimitive(28.1).toString())
    }

    @Test
    fun testInvalidArray() {
        assertFailsWith<IllegalArgumentException> {
            JsonPrimitive(listOf(28,"r",47))
        }
    }

    @Test
    fun testInvalidObject() {
        assertFailsWith<IllegalArgumentException> {
            JsonPrimitive(Date(28,1,2026))
        }
    }

    @Test
    fun testInvalidMap() {
        assertFailsWith<IllegalArgumentException> {
            JsonPrimitive(mapOf("28" to 1))
        }
    }

    // Strings com escaping testar mais tarde

}