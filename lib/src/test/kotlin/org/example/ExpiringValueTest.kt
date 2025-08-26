package org.example

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.assertThrows
import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.concurrent.LinkedBlockingQueue
import kotlin.test.assertEquals

class ExpiringValueTest : ShouldSpec({
    context("An expiringValue") {
        should("return its value") {
            val expiringValue = ExpiringValue { "value" to Instant.MAX }
            expiringValue.get() shouldBe "value"
        }

        should("Should return new value if value expires") {
            val testClock = TestClock()
            val valueQueue = LinkedBlockingQueue(
                listOf(
                    "value" to Instant.ofEpochSecond(1000),
                    "new value" to Instant.ofEpochSecond(2000),
                )
            )

            val expiringValue = ExpiringValue(clock = testClock, valueSupplier = { valueQueue.poll() })

            // Get old value
            assertEquals("value", expiringValue.get())

            //Advance clock
            testClock.instant = Instant.ofEpochSecond(1500)

            //Get new value
            assertEquals("new value", expiringValue.get())
        }

        should("Should throw exception is value supplied is already expired") {
            val testClock = TestClock()
            testClock.instant = Instant.ofEpochSecond(1500)

            assertThrows<ValueExpiredException> {
                ExpiringValue(clock = testClock, valueSupplier = { "value" to Instant.ofEpochSecond(1000) })
            }

        }
    }
})

class TestClock : Clock() {
    var instant: Instant = Instant.MIN

    override fun instant(): Instant = instant

    override fun withZone(zone: ZoneId?): Clock {
        throw UnsupportedOperationException()
    }

    override fun getZone(): ZoneId = ZoneOffset.UTC

}