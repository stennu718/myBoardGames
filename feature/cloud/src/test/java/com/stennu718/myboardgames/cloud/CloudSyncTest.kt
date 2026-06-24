package com.stennu718.myboardgames.cloud

import org.junit.Test
import org.junit.Assert.*

class CloudSyncTest {

    @Test
    fun testUpload() {
        val sync = LocalCloudSync()
        val data = SyncData(
            stats = listOf(GameStats("chess", gamesPlayed = 5)),
            achievements = listOf("first_win"),
            settings = mapOf("theme" to "classic"),
            lastModified = 12345L
        )
        assertTrue(sync.upload(data))
    }

    @Test
    fun testDownloadReturnsUploadedData() {
        val sync = LocalCloudSync()
        val data = SyncData(
            stats = listOf(GameStats("chess", gamesPlayed = 10)),
            achievements = listOf("chess_master"),
            settings = emptyMap(),
            lastModified = 99999L
        )
        sync.upload(data)
        val downloaded = sync.download()
        assertNotNull(downloaded)
        assertEquals(10, downloaded!!.stats.first().gamesPlayed)
    }

    @Test
    fun testDownloadReturnsNullWhenEmpty() {
        val sync = LocalCloudSync()
        assertNull(sync.download())
    }

    @Test
    fun testSyncFirstTime() {
        val sync = LocalCloudSync()
        val manager = SyncManager(sync) { _, _ -> SyncData(emptyList(), emptyList(), emptyMap(), 0) }
        val localData = SyncData(listOf(GameStats("chess")), emptyList(), emptyMap(), 1000L)
        val result = runBlocking { manager.sync(localData) }
        assertTrue(result is SyncManager.SyncResult.Uploaded)
    }

    @Test
    fun testSyncAvailable() {
        val sync = LocalCloudSync()
        assertTrue(sync.isAvailable())
    }

    private suspend fun <T> runBlocking(block: suspend () -> T): T {
        return kotlinx.coroutines.runBlocking { block() }
    }
}
