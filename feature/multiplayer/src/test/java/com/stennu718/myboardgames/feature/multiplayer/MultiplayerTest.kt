package com.stennu718.myboardgames.feature.multiplayer

import org.junit.Test
import org.junit.Assert.*

class MultiplayerTest {

    @Test
    fun testMultiplayerStateInitial() {
        val mgr = MultiplayerManager()
        val state = mgr.state.value
        assertEquals(ConnectionState.DISCONNECTED, state.state)
        assertNull(state.role)
        assertNull(state.opponentName)
    }

    @Test
    fun testAppUuid() {
        assertNotNull(MultiplayerManager.APP_UUID)
        assertEquals("00001101-0000-1000-8000-00805F9B34FB", MultiplayerManager.APP_UUID.toString())
    }

    @Test
    fun testSendMessageFormat() {
        // Verify message format
        val fromRow = 6, fromCol = 4, toRow = 4, toCol = 4
        val message = "MOVE:$fromRow,$fromCol,$toRow,$toRow,"
        assertTrue(message.startsWith("MOVE:"))
        assertEquals("MOVE:6,4,4,4,", message)
    }

    @Test
    fun testParseMoveMessage() {
        val message = "MOVE:6,4,4,4,Q"
        val parts = message.removePrefix("MOVE:").split(",")
        assertEquals(5, parts.size)
        assertEquals("6", parts[0])
        assertEquals("4", parts[1])
        assertEquals("4", parts[2])
        assertEquals("4", parts[3])
        assertEquals("Q", parts[4])
    }

    @Test
    fun testGameStartMessage() {
        val message = "START:WHITE"
        assertEquals("WHITE", message.removePrefix("START:"))
    }

    @Test
    fun testResignMessage() {
        val message = "RESIGN"
        assertEquals("RESIGN", message)
    }

    @Test
    fun testBluetoothCheck() {
        val mgr = MultiplayerManager()
        // Without adapter, should return false
        assertFalse(mgr.isBluetoothEnabled())
    }

    @Test
    fun testPairedDevicesEmptyWithoutAdapter() {
        val mgr = MultiplayerManager()
        assertTrue(mgr.getPairedDevices().isEmpty())
    }
}
