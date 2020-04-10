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

import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_setup.*
import org.subhipstercollective.deucelibrary.SeekBarSets
import org.subhipstercollective.deucelibrary.Team
import kotlin.random.Random

class SetupFragment(val mainActivity: MainActivity) : Fragment() {
    companion object {
        const val SERVER_ME = 0
        const val SERVER_OPPONENT = 1
        const val SERVER_FLIP = 2
    }

    lateinit var preferences: SharedPreferences
    val ambientMode = mainActivity.ambientMode

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_setup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preferences = PreferenceManager.getDefaultSharedPreferences(context)

        view.post {
            // Set text_num_sets width to largest necessary
            val paint = Paint()
            text_num_sets.width = maxOf(
                paint.measureText(getString(R.string.best_of_1)),
                paint.measureText(getString(R.string.best_of_3)),
                paint.measureText(getString(R.string.best_of_5))
            ).toInt()
        }

        if (mainActivity.ambientMode) {
            text_num_sets.paint.isAntiAlias = false
            text_server.paint.isAntiAlias = false
            radio_server_flip.setTextColor(Color.WHITE)
            radio_server_flip.paint.isAntiAlias = false
            radio_server_me.setTextColor(Color.WHITE)
            radio_server_me.paint.isAntiAlias = false
            radio_server_opponent.setTextColor(Color.WHITE)
            radio_server_opponent.paint.isAntiAlias = false
            button_start.paint.isAntiAlias = false
        }

        text_num_sets.text = seek_num_sets.progressString

        seek_num_sets.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                text_num_sets.text = seek_num_sets.progressString
                preferences.edit().putInt("num_sets", progress).apply()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        radio_server_me.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                preferences.edit().putInt("server", 0).apply()
            }
        }

        radio_server_opponent.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                preferences.edit().putInt("server", 1).apply()
            }
        }

        radio_server_flip.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                preferences.edit().putInt("server", 2).apply()
            }
        }

        button_start.setOnClickListener {
            mainActivity.newMatch(
                seek_num_sets.numSets,
                if (radio_server_me.isChecked || (radio_server_flip.isChecked && Random.nextInt(2) == 0))
                    Team.TEAM1
                else
                    Team.TEAM2,
                preferences.getBoolean("doubles", false),
                preferences.getBoolean("advantage", false)
            )
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        seek_num_sets.progress = preferences.getInt("num_sets", SeekBarSets.BEST_OF_3)
        when (preferences.getInt("server", 2)) {
            SERVER_ME -> radio_server_me.isChecked = true
            SERVER_OPPONENT -> radio_server_opponent.isChecked = true
            SERVER_FLIP -> radio_server_flip.isChecked = true
        }
    }
}
