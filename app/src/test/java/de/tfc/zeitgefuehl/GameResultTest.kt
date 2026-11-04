package de.tfc.zeitgefuehl

import de.tfc.zeitgefuehl.model.GameResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GameResultTest {

    @Test
    fun testExactHit() {
        val result = GameResult(targetTimeMillis = 5000L, elapsedTimeMillis = 5000L)
        assertEquals(0L, result.diffMillis)
        assertEquals(0L, result.absDiffMillis)
        assertEquals(100.0, result.accuracyPercentage, 0.01)
        assertEquals(5, result.ratingStars)
        assertEquals("±0 ms", result.formattedDiff)
    }

    @Test
    fun testSlightlyLate() {
        val result = GameResult(targetTimeMillis = 5000L, elapsedTimeMillis = 5120L)
        assertEquals(120L, result.diffMillis)
        assertEquals(120L, result.absDiffMillis)
        assertTrue(result.accuracyPercentage > 95.0)
        assertEquals("+120 ms", result.formattedDiff)
    }

    @Test
    fun testSlightlyEarly() {
        val result = GameResult(targetTimeMillis = 5000L, elapsedTimeMillis = 4850L)
        assertEquals(-150L, result.diffMillis)
        assertEquals(150L, result.absDiffMillis)
        assertTrue(result.accuracyPercentage > 95.0)
        assertEquals("-150 ms", result.formattedDiff)
    }

    @Test
    fun testLargeDeviation() {
        val result = GameResult(targetTimeMillis = 5000L, elapsedTimeMillis = 8000L)
        assertEquals(3000L, result.diffMillis)
        assertEquals(3000L, result.absDiffMillis)
        assertEquals(40.0, result.accuracyPercentage, 0.01)
        assertEquals(1, result.ratingStars)
    }
}
