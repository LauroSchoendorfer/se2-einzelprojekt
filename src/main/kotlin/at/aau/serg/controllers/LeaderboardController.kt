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

    @GetMapping("/{rank}")
    fun getLeaderBoardByRank(@PathVariable rank: Int): List<GameResult> {
        val leaderboard = this.getLeaderboard();
        val rankIndex = rank-1;

        if(rankIndex >= leaderboard.size || rank < 1) {
            return leaderboard;
        }

        var leaderboardByRank: List<GameResult> = listOf();
        for(i in rankIndex-3 until rankIndex+4) {
            if(i < 0) {
                continue;
            }
            if(i >= leaderboard.size) {
                return leaderboardByRank;
            }
            leaderboardByRank += leaderboard[i];
        }
        return leaderboardByRank;
    }
}