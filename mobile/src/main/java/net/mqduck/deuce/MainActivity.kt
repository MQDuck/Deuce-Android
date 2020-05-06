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

package net.mqduck.deuce

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.wearable.*
import kotlinx.android.synthetic.main.activity_main.*
import net.mqduck.deuce.common.*
import java.util.*

//import android.util.Log

lateinit var mainActivity: MainActivity

class MainActivity : AppCompatActivity(), DataClient.OnDataChangedListener,
    ScoresListFragment.OnMatchInteractionListener {
    private lateinit var scoresListFragment: ScoresListFragment

    init {
        mainActivity = this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Game.init(this)

        scoresListFragment = fragment_scores as ScoresListFragment
    }

    override fun onResume() {
        super.onResume()
        Wearable.getDataClient(this).addListener(this)
    }

    override fun onPause() {
        super.onPause()
        Wearable.getDataClient(this).removeListener(this)
    }

    override fun onDataChanged(dataEvents: DataEventBuffer) {
        Log.d("foo", "outside")
        dataEvents.forEach { event ->
            // DataItem changed
            if (event.type == DataEvent.TYPE_CHANGED) {
                event.dataItem.also { item ->
                    if (item.uri.path?.compareTo(PATH_CURRENT_MATCH) == 0) {
                        DataMapItem.fromDataItem(item).dataMap.apply {
                            Log.d("foo", "inside")

                            if (getBoolean(KEY_NEW_GAME)) {
                                Log.d("foo", "adding new match")
                                val newMatch = DeuceMatch(
                                    NumSets.fromOrdinal(getInt(KEY_NUM_SETS)),
                                    Team.fromOrdinal(getInt(KEY_SERVER)),
                                    OvertimeRule.fromOrdinal(getInt(KEY_OVERTIME_RULE)),
                                    MatchType.fromOrdinal(getInt(KEY_MATCH_TYPE)),
                                    PlayTimesData(getLong(KEY_MATCH_START_TIME), getLong(KEY_MATCH_END_TIME)),
                                    PlayTimesList(getLongArray(KEY_SETS_START_TIMES), getLongArray(KEY_SETS_END_TIMES)),
                                    ScoreStack(getInt(KEY_SCORE_SIZE), BitSet.valueOf(getLongArray(KEY_SCORE_ARRAY))),
                                    getString(KEY_NAME_TEAM1),
                                    getString(KEY_NAME_TEAM2)
                                )

                                if (
                                    scoresListFragment.matches.isNotEmpty() &&
                                    scoresListFragment.matches.last().winner == Winner.NONE
                                ) {
                                    scoresListFragment.matches[scoresListFragment.matches.size - 1] = newMatch
                                } else {
                                    scoresListFragment.matches.add(newMatch)
                                }
                            } else {
                                Log.d("foo", "updating current match")
                                if (scoresListFragment.matches.isEmpty()) {
                                    // TODO: request match information?
                                    return
                                }

                                val currentMatch = scoresListFragment.matches.last()
                                currentMatch.playTimes.endTime = getLong(KEY_MATCH_END_TIME)
                                currentMatch.setsTimesLog = PlayTimesList(
                                    getLongArray(KEY_SETS_START_TIMES),
                                    getLongArray(KEY_SETS_END_TIMES)
                                )
                                currentMatch.scoreLog = ScoreStack(
                                    getInt(KEY_SCORE_SIZE),
                                    BitSet.valueOf(getLongArray(KEY_SCORE_ARRAY))
                                )
                            }

                            scoresListFragment.view.adapter?.notifyDataSetChanged()
                        }
                    } else if (item.uri.path?.compareTo(PATH_CURRENT_MATCH) == 0) {
                        DataMapItem.fromDataItem(item).dataMap.apply {

                        }
                    }
                }
            } else if (event.type == DataEvent.TYPE_DELETED) {
                // DataItem deleted
            }
        }
    }

    override fun onMatchInteraction(item: Match?) {
        // TODO: Make less ugly
        item?.let {
            scoresListFragment.fragmentManager?.let {
                val infoDialog = InfoDialog(item, scoresListFragment)
                infoDialog.show(it, "info")
            }
        }
    }
}
