/*
 * Copyright (C) 2019 Jeffrey Thomas Piercy
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

package org.subhipstercollective.deuce

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_score.*
import org.subhipstercollective.deucelibrary.ScoreView

class ScoreFragment : Fragment(), ScoreView {
    override lateinit var buttonScoreP1: Button
    override lateinit var buttonScoreP2: Button
    override lateinit var textScoreP1: TextView
    override lateinit var textScoreP2: TextView
    override lateinit var imageBallServingT1: ImageView
    override lateinit var imageBallNotservingT1: ImageView
    override lateinit var imageBallServingT2: ImageView
    override lateinit var imageBallNotservingT2: ImageView
    override lateinit var textScoresMatchP1: TextView
    override lateinit var textScoresMatchP2: TextView
    override var posXBallLeftT1 = 0f
    override var posXBallRightT1 = 0f
    override var posXBallLeftT2 = 0f
    override var posXBallRightT2 = 0f

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_score, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonScoreP1 = button_score_p1
        buttonScoreP2 = button_score_p2
        textScoreP1 = button_score_p1
        textScoreP2 = button_score_p2

        imageBallServingT1 = ball_serving_t1
        imageBallNotservingT1 = ball_notserving_t1
        imageBallServingT2 = ball_serving_t2
        imageBallNotservingT2 = ball_notserving_t2

        textScoresMatchP1 = text_scores_match_p1
        textScoresMatchP2 = text_scores_match_p2

        posXBallLeftT1 = ball_notserving_t1.x
        posXBallRightT2 = posXBallLeftT1
        posXBallRightT1 = view.width - ball_notserving_t1.x - ball_notserving_t1.width
        posXBallLeftT2 = posXBallRightT1
    }
}
