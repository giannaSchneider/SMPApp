/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.inventory.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.inventory.ui.home.HomeDestination
import com.example.inventory.ui.home.HomeScreen
import com.example.inventory.ui.item.*
import com.example.inventory.ui.routines.*

/**
 * Provides Navigation graph for the application.
 */
@Composable
fun InventoryNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen(
                navigateToItemEntry = { navController.navigate(ItemEntryDestination.route) },
                navigateToItemUpdate = {
                    navController.navigate("${ItemDetailsDestination.route}/${it}")
                }
            )
        }
        composable(route = ItemEntryDestination.route) {
            ItemEntryScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
        composable(
            route = ItemDetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(ItemDetailsDestination.itemIdArg) {
                type = NavType.IntType
            })
        ) {
            ItemDetailsScreen(
                navigateToEditItem = {navController.navigate("${ItemEditDestination.route}/$it") },
                //navigateToItemEntry = {navController.navigate(ItemEntryDestination.route)},
                navigateBack = { navController.navigateUp() },
                navigateToTimerRoutineEntry = {navController.navigate(TimerRoutineEntryDestination.route)},
                navigateToClockRoutineEntry = {navController.navigate(ClockRoutineEntryDestination.route)},
                navigateToMultiRoutineEntry = {navController.navigate(MultiRoutineEntryDestination.route)},
                navigateToMixRoutineEntry = {navController.navigate(MixRoutineEntryDestination.route)},

                navigateToTimerRoutineUpdate = {
                    navController.navigate("${TimerRoutineDetailsDestination.route}/${it}")
                }

                /*navigateToTimerRoutineDetails = { navController.navigate("${TimerRoutineDetailsDestination.route}/${it}") },
                navigateToClockRoutineDetails = { navController.navigate("${ClockRoutineDetailsDestination.route}/${it}") },
                navigateToMultiRoutineDetails = { navController.navigate("${MultiRoutineDetailsDestination.route}/${it}") },
                navigateToMixRoutineDetails = { navController.navigate("${MixRoutineDetailsDestination.route}/${it}") },*/

                )
        }
        composable(
            route = ItemEditDestination.routeWithArgs,
            arguments = listOf(navArgument(ItemEditDestination.itemIdArg) {
                type = NavType.IntType
            })
        ) {
            ItemEditScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
        composable(
            route = TimerRoutineEntryDestination.route
        ) {
            TimerRoutineEntryScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() },

            )
        }
        composable(
            route = TimerRoutineDetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(TimerRoutineDetailsDestination.timerRoutineIdArg) {
                type = NavType.IntType
            })
        ){TimerRoutineDetailsScreen(
                navigateBack = { navController.navigateUp() },
                //onNavigateUp = { navController.navigateUp() },
                navigateToEditTimerRoutine = { navController.navigate("${TimerRoutineEditDestination.route}/$it") },
                //navigateToTimerRoutineEntry = {navController.navigate(TimerRoutineEntryDestination.route)},

            )
        }
        composable(
            route = TimerRoutineEditDestination.routeWithArgs,
            arguments = listOf(navArgument(TimerRoutineEditDestination.timerRoutineIdArg) {
                type = NavType.IntType
            })
        ) {
            TimerRoutineEditScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
        composable(
            route = ClockRoutineEntryDestination.route
        ) {
            ClockRoutineEntryScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() },
            )
        }
        /*composable(
            route = ClockRoutineDetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(ClockRoutineDetailsDestination.clockRoutineIdArg) {
                type = NavType.IntType
            })
        ){ClockRoutineDetailsScreen(
            navigateBack = { navController.navigateUp() },
            //onNavigateUp = { navController.navigateUp() },
            navigateToEditClockRoutine = { navController.navigate("${ClockRoutineEditDestination.route}/$it") }


            )
        }*/
        composable(
            route = ClockRoutineEditDestination.routeWithArgs,
            arguments = listOf(navArgument(ClockRoutineEditDestination.clockRoutineIdArg) {
                type = NavType.IntType
            })
        ) {
            ClockRoutineEditScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }

        composable(
            route = MultiRoutineEntryDestination.route
        ) {
            MultiRoutineEntryScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() },
            )
        }
        /*composable(
            route = MultiRoutineDetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(MultiRoutineDetailsDestination.multiRoutineIdArg) {
                type = NavType.IntType
            })
        ){MultiRoutineDetailsScreen(
            navigateBack = { navController.navigateUp() },
            //onNavigateUp = { navController.navigateUp() },
            navigateToEditMultiRoutine = { navController.navigate("${MultiRoutineEditDestination.route}/$it") }


            )
        }*/
        composable(
            route = MultiRoutineEditDestination.routeWithArgs,
            arguments = listOf(navArgument(MultiRoutineEditDestination.multiRoutineIdArg) {
                type = NavType.IntType
            })
        ) {
            MultiRoutineEditScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }

        composable(
            route = MixRoutineEntryDestination.route
        ) {
            MixRoutineEntryScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() },
            )
        }
        /*composable(
            route = MixRoutineDetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(MixRoutineDetailsDestination.mixRoutineIdArg) {
                type = NavType.IntType
            })
        ){MixRoutineDetailsScreen(
            navigateBack = { navController.navigateUp() },
            //onNavigateUp = { navController.navigateUp() },
            navigateToEditMixRoutine = { navController.navigate("${MixRoutineEditDestination.route}/$it") }


            )
        }*/
        composable(
            route = MixRoutineEditDestination.routeWithArgs,
            arguments = listOf(navArgument(MixRoutineEditDestination.mixRoutineIdArg) {
                type = NavType.IntType
            })
        ) {
            MixRoutineEditScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
    }
}
