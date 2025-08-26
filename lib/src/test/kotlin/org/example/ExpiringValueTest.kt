package org.example

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.extensions.clock.TestClock
import io.kotest.matchers.shouldBe
import java.time.Instant
import java.time.ZoneOffset
import java.util.concurrent.LinkedBlockingQueue


class ExpiringValueTest : ShouldSpec({
    context("An expiringValue") {
        should("return its value") {
            val expiringValue = ExpiringValue { "value" to Instant.MAX }
            expiringValue.get() shouldBe "value"
        }

        should("Should return new value if value expires") {
            val testClock = testClock()
            val valueQueue = LinkedBlockingQueue(
                listOf(
                    "value" to Instant.ofEpochSecond(1000),
                    "new value" to Instant.ofEpochSecond(2000),
                )
            )

            val expiringValue = ExpiringValue(clock = testClock, valueSupplier = { valueQueue.poll() })

            // Get old value
            expiringValue.get() shouldBe "value"

            //Advance clock
            testClock.setInstant(Instant.ofEpochSecond(1500))

            //Get new value
            expiringValue.get() shouldBe "new value"
        }

        should("Should throw exception is value supplied is already expired") {
            val testClock = testClock(Instant.ofEpochSecond(1500))

            shouldThrow<ValueExpiredException> {
                ExpiringValue(clock = testClock, valueSupplier = { "value" to Instant.ofEpochSecond(1000) })
            }
        }
    }
})

fun testClock(instant: Instant = Instant.MIN, offset: ZoneOffset = ZoneOffset.UTC) =
    TestClock(instant, offset)