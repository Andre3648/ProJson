package pt.iscte.pa.projson

import kotlin.test.Test
import kotlin.test.assertEquals

class JsonReferenceTest {

    @Test
    fun testToString() {
        val ref = JsonReference("9e2e6c64-3236-45b7-8b8a-11271c69e4df")
        assertEquals($$"{ \"$ref\": \"9e2e6c64-3236-45b7-8b8a-11271c69e4df\" }", ref.toString())
    }
}