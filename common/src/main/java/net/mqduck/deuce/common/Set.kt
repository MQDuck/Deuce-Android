/*
 * Copyright (C) 2020 Jeffrey Thomas Piercy
 *
 * This file is part of Deuce-Android.
 *
 * Deuce-Android is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Deuce-Android is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Deuce-Android.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.mqduck.deuce.common

/**
 * Created by mqduck on 10/31/17.
 */
class Set(
    private val winMinimum: Int, winMargin: Int,
    private val winMinimumGame: Int, private val winMarginGame: Int,
    private val winMinimumGameTiebreak: Int, private val winMarginGameTiebreak: Int,
    private val overtime: Overtime,
    private val controller: ScoreController
) {
    var games = ArrayList<Game>()
    private var mScore = Score(winMinimum, winMargin)

    init {
        games.add(Game(winMinimumGame, winMarginGame, controller))
    }

    fun score(team: Team) = mScore.score(team)

    fun addNewGame() {
        if (overtime == Overtime.TIEBREAK && mScore.scoreP1 == winMinimum && mScore.scoreP2 == winMinimum) {
            mScore.winMargin = 1
            games.add(Game(winMinimumGameTiebreak, winMarginGameTiebreak, controller, true))
        } else {
            games.add(Game(winMinimumGame, winMarginGame, controller, false))
        }
    }

    fun getScore(team: Team) = mScore.getScore(team)

    fun getScoreStrs() = ScoreStrings(mScore.scoreP1.toString(), mScore.scoreP2.toString())

    val currentGame get() = games.last()

    val scoreP1 get() = mScore.scoreP1
    val scoreP2 get() = mScore.scoreP2
}