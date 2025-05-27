package com.example.a21ptask

import org.junit.Test
import org.junit.Assert.*

class ExampleUnitTest {

    @Test
    fun testLengthConversion() {
        val result = convertUnits("inch", "cm", 1.0)
        assertEquals(2.54, result, 0.001)
    }

    @Test
    fun testSameUnit() {
        val result = convertUnits("kg", "kg", 5.0)
        assertEquals(5.0, result, 0.001)
    }

    @Test
    fun testInvalidCategory() {
        val result = convertUnits("inch", "Celsius", 10.0)
        assertTrue(result.isNaN())
    }
}
