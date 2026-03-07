package at.aau.serg.controllers

import at.aau.serg.models.GameResult
import at.aau.serg.services.GameResultService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/leaderboard")
class LeaderboardController(
    private val gameResultService: GameResultService
) {
    @GetMapping
    fun getLeaderboard(): List<GameResult> =
        gameResultService.getGameResults().sortedWith(compareBy({ -it.score },{it.timeInSeconds}, { it.id }))

    // bei 10 Einträgen
    // rank = 0 ersten 3 werden angezeigt (rank variable anpassen)
    // rank = 10 alle werden angezeigt, anstatt die letzten 4 (zugriff über index überarbeiten)
    // rank = 4 es werden nur 6 angezeigt anstatt 7 (Z. 31)
    @GetMapping("/{rank}")
    fun getLeaderBoardByRank(@PathVariable rank: Int): List<GameResult> {
        var leaderboard = this.getLeaderboard();

        if(rank > leaderboard.size - 1 || rank < 0 || leaderboard.size == 0) {
            return leaderboard
        }

        var leaderboardByRank: List<GameResult> = listOf();
        for(i in rank-3 until rank+3) {
            if(i < 0) {
                continue
            }
            if(i > leaderboard.size - 1) {
                break
            }
            leaderboardByRank += leaderboard[i]
        }

        return leaderboardByRank

    }
}