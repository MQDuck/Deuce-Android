package org.subhipstercollective.deucelibrary

import android.content.Context

class Game(winMinimum: Int, winMargin: Int, private val controller: ControllerMain, val tiebreak: Boolean = false) {
    companion object {
        private lateinit var scoreMap: Array<String>
        private lateinit var strAdIn: String
        private lateinit var strAdOut: String
        private lateinit var strDeuce: String

        fun init(context: Context) {
            scoreMap = arrayOf(context.resources.getString(R.string.love), "15", "30", "40", "")
            strAdIn = context.getString(R.string.ad_in)
            strAdOut = context.getString(R.string.ad_out)
            strDeuce = context.getString(R.string.deuce)
        }
    }

    private val mScore = Score(winMinimum, winMargin)

    fun score(player: Player = Player.NONE) = mScore.score(player)

    fun getScore(player: Player) = mScore.getScore(player)

    fun getScoreStrs(): ScoreStrings {
        return if (tiebreak) {
            ScoreStrings(mScore.scoreP1.toString(), mScore.scoreP2.toString())
        } else {
            when {
                mScore.winner == Player.PLAYER1 -> ScoreStrings("\uD83C\uDFC6", "")
                mScore.winner == Player.PLAYER2 -> ScoreStrings("", "\uD83C\uDFC6")
                mScore.scoreP1 < 3 || mScore.scoreP2 < 3 -> ScoreStrings(scoreMap[mScore.scoreP1], scoreMap[mScore.scoreP2])
                mScore.scoreP1 > mScore.scoreP2 -> when (controller.serving) {
                    Serving.PLAYER1_LEFT, Serving.PLAYER1_RIGHT -> ScoreStrings(strAdIn, "")
                    Serving.PLAYER2_LEFT, Serving.PLAYER2_RIGHT -> ScoreStrings(strAdOut, "")
                }
                mScore.scoreP1 < mScore.scoreP2 -> when (controller.serving) {
                    Serving.PLAYER1_LEFT, Serving.PLAYER1_RIGHT -> ScoreStrings("", strAdOut)
                    Serving.PLAYER2_LEFT, Serving.PLAYER2_RIGHT -> ScoreStrings("", strAdIn)
                }
                else -> ScoreStrings(strDeuce, strDeuce)
            }
        }
    }
}
