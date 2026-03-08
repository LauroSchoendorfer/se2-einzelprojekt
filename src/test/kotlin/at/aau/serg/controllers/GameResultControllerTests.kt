package at.aau.serg.controllers

import at.aau.serg.models.GameResult
import at.aau.serg.services.GameResultService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.times
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import kotlin.test.Test
import kotlin.test.assertEquals
import org.mockito.Mockito.`when` as whenever // when is a reserved keyword in Kotlin

class GameResultControllerTests {
    private lateinit var mockedService: GameResultService
    private lateinit var controller: GameResultController

    @BeforeEach
    fun setup() {
        mockedService = mock<GameResultService>()
        controller = GameResultController(mockedService)
    }

    @Test
    fun test_getGameResult() {
        val gameResult1 = GameResult(1, "player1", 17, 15.3)

        whenever(mockedService.getGameResult(1)).thenReturn(gameResult1)
        val res = controller.getGameResult(1)
        verify(mockedService).getGameResult(1)
        assertEquals(gameResult1, res)
    }

    @Test
    fun test_getAllGameResults() {
        val gameResult1 = GameResult(1, "player1", 17, 15.3)
        val gameResult2 = GameResult(2, "player2", 17, 15.0)
        whenever(mockedService.getGameResults()).thenReturn(listOf(gameResult1, gameResult2))
        val res = controller.getAllGameResults()
        verify(mockedService).getGameResults()
        assertEquals(gameResult1, res[0])
        assertEquals(gameResult2, res[1])
    }

    @Test
    fun test_addGameResult() {
        val gameResult1 = GameResult(1, "player1", 17, 15.3)
        controller.addGameResult(gameResult1)
        verify(mockedService).addGameResult(gameResult1)
    }

    @Test
    fun test_deleteGameResult() {
        val gameResult1 = GameResult(1, "player1", 17, 15.3)
        controller.deleteGameResult(1)
        verify(mockedService).deleteGameResult(1)
    }
}