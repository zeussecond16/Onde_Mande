package com.example.web_app_ondemande.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.web_app_ondemande.ui.cart.CartScreen
import com.example.web_app_ondemande.ui.home.HomeScreen
import com.example.web_app_ondemande.ui.menu.MenuScreen

/**
 * Rute Navigasi Aplikasi Onde Mande.
 */
sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Menu : Screen("menu")
    data object Cart : Screen("cart")
}

/**
 * Komponen Utama Navigasi Compose & Pengelola State Terpusat (State Hoisting).
 *
 * Fitur Compose:
 * - NavHost & composable() untuk berpindah layar secara deklaratif.
 * - State Hoisting: cartState dideklarasikan sekali di tingkat paling atas dan dikirim ke setiap layar
 *   sebagai data pembacaan dan fungsi callback lambda (onIncrease, onDecrease, onConfirmOrder).
 * - rememberSaveable dengan custom Saver agar data keranjang tidak hilang saat rotasi layar HP.
 *
 * Bagian Tampilan:
 * - Menghubungkan alur navigasi antara Home -> Menu -> Keranjang secara mulus.
 */
@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    // Custom Saver untuk menyimpan mutableStateMapOf saat rotasi layar
    val cartMapSaver = Saver<SnapshotStateMapWrapper, ArrayList<String>>(
        save = { wrapper ->
            ArrayList(wrapper.map.entries.map { "${it.key}:${it.value}" })
        },
        restore = { list ->
            val newMap = mutableStateMapOf<Int, Int>()
            list.forEach { item ->
                val parts = item.split(":")
                if (parts.size == 2) {
                    newMap[parts[0].toInt()] = parts[1].toInt()
                }
            }
            SnapshotStateMapWrapper(newMap)
        }
    )

    val cartStateWrapper = rememberSaveable(saver = cartMapSaver) {
        SnapshotStateMapWrapper(mutableStateMapOf())
    }
    val cartState = cartStateWrapper.map

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
// Layar Utama (Home)
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToMenu = {
                    navController.navigate(Screen.Menu.route)
                },
                onNavigateToCart = {
                    navController.navigate(Screen.Cart.route)
                }
            )
        }

        // Layar Katalog Menu
        composable(Screen.Menu.route) {
            MenuScreen(
                cartState = cartState,
                onIncrease = { id ->
                    cartState[id] = (cartState[id] ?: 0) + 1
                },
                onDecrease = { id ->
                    val currentQty = cartState[id] ?: 0
                    if (currentQty > 1) {
                        cartState[id] = currentQty - 1
                    } else {
                        cartState.remove(id)
                    }
                },
                onNavigateToCart = {
                    navController.navigate(Screen.Cart.route)
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // Layar Keranjang / Checkout
        composable(Screen.Cart.route) {
            CartScreen(
                cartState = cartState,
                onIncrease = { id ->
                    cartState[id] = (cartState[id] ?: 0) + 1
                },
                onDecrease = { id ->
                    val currentQty = cartState[id] ?: 0
                    if (currentQty > 1) {
                        cartState[id] = currentQty - 1
                    } else {
                        cartState.remove(id)
                    }
                },
                onConfirmOrder = {
                    cartState.clear()
                    navController.popBackStack(Screen.Home.route, inclusive = false)
                },
                onBackToMenu = {
                    navController.popBackStack()
                }
            )
        }
    }
}

/**
 * Class pembungkus sederhana untuk Saver mutableStateMapOf.
 */
private class SnapshotStateMapWrapper(
    val map: SnapshotStateMap<Int, Int>
)
