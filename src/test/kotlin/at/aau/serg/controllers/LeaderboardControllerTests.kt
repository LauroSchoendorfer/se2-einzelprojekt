package at.aau.serg.controllers

import at.aau.serg.models.GameResult
import at.aau.serg.services.GameResultService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import kotlin.test.Test
import kotlin.test.assertEquals
import org.mockito.Mockito.`when` as whenever // when is a reserved keyword in Kotlin

class LeaderboardControllerTests {

    private lateinit var mockedService: GameResultService
    private lateinit var controller: LeaderboardController

    @BeforeEach
    fun setup() {
        mockedService = mock<GameResultService>()
        controller = LeaderboardController(mockedService)
    }

    @Test
    fun test_getLeaderboard_correctScoreSorting() {
        val first = GameResult(1, "first", 20, 20.0)
        val second = GameResult(2, "second", 15, 10.0)
        val third = GameResult(3, "third", 10, 15.0)

        whenever(mockedService.getGameResults()).thenReturn(listOf(second, first, third))

        val res: List<GameResult> = controller.getLeaderboard()

        verify(mockedService).getGameResults()
        assertEquals(3, res.size)
        assertEquals(first, res[0])
        assertEquals(second, res[1])
        assertEquals(third, res[2])
    }

    @Test
    fun test_getLeaderboard_sameScore_CorrectIdSorting() {
        val first = GameResult(1, "first", 20, 20.0)
        val second = GameResult(2, "second", 20, 20.0)
        val third = GameResult(3, "third", 20, 20.0)

        whenever(mockedService.getGameResults()).thenReturn(listOf(second, first, third))

        val res: List<GameResult> = controller.getLeaderboard()

        verify(mockedService).getGameResults()
        assertEquals(3, res.size)
        assertEquals(first, res[0])
        assertEquals(second, res[1])
        assertEquals(third, res[2])
    }

    @Test
    fun test_getLeaderboard_sameScore_CorrectTimeSorting() {
        val first = GameResult(1, "first", 20, 20.0)
        val second = GameResult(2, "second", 20, 15.0)
        val third = GameResult(3, "third", 20, 10.0)

        whenever(mockedService.getGameResults()).thenReturn(listOf(third, second, first))

        val res: List<GameResult> = controller.getLeaderboard()

        verify(mockedService).getGameResults()
        assertEquals(3, res.size)
        assertEquals(third, res[0])
        assertEquals(second, res[1])
        assertEquals(first, res[2])
    }

    @Test
    fun test_getLeaderBoardByRank_rankOutOfBounds() {
        val first = GameResult(1, "first", 50, 20.0)
        val second = GameResult(2, "second", 45, 15.0)
        val third = GameResult(3, "third", 40, 10.0)
        val fourth = GameResult(4, "fourth", 35, 10.0)
        val fifth = GameResult(5, "fifth", 30, 10.0)
        val sixth = GameResult(6, "sixth", 25, 10.0)
        val seventh = GameResult(7, "seventh", 20, 10.0)

        whenever(mockedService.getGameResults()).thenReturn(listOf(first, second, third, fourth, fifth, sixth, seventh))

        //val res = controller.getLeaderBoardByRank(0)
        val exception = assertThrows<ResponseStatusException> {
            controller.getLeaderBoardByRank(-1)
        }

        verify(mockedService).getGameResults()
        assertEquals(HttpStatus.BAD_REQUEST, exception.statusCode)
    }

    @Test
    fun test_getLeaderBoardByRank_workingNormal() {
        val first = GameResult(1, "first", 50, 20.0)
        val second = GameResult(2, "second", 45, 15.0)
        val third = GameResult(3, "third", 40, 10.0)
        val fourth = GameResult(4, "fourth", 35, 10.0)
        val fifth = GameResult(5, "fifth", 30, 10.0)
        val sixth = GameResult(6, "sixth", 25, 10.0)
        val seventh = GameResult(7, "seventh", 20, 10.0)
        val eigth = GameResult(8, "eigth", 15, 10.0)

        whenever(mockedService.getGameResults()).thenReturn(listOf(first, second, third, fourth, fifth, sixth, seventh, eigth))

        var res = controller.getLeaderBoardByRank(1)
        verify(mockedService).getGameResults()
        assertEquals(first, res[0])
        assertEquals(second, res[1])
        assertEquals(third, res[2])
        assertEquals(fourth, res[3])

        res = controller.getLeaderBoardByRank(4)
        verify(mockedService).getGameResults()
        assertEquals(first, res[0])
        assertEquals(second, res[1])
        assertEquals(third, res[2])
        assertEquals(fourth, res[3])
        assertEquals(fifth, res[4])
        assertEquals(sixth, res[5])
        assertEquals(seventh, res[6])

        res = controller.getLeaderBoardByRank(8)
        verify(mockedService).getGameResults()
        assertEquals(fifth, res[1])
        assertEquals(sixth, res[2])
        assertEquals(seventh, res[3])
        assertEquals(eigth, res[4])
    }

}