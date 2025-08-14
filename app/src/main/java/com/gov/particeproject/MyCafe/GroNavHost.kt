package com.gov.particeproject.MyCafe

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.gov.particeproject.R
import com.gov.particeproject.ui.theme.ParticeProjectTheme
import kotlinx.coroutines.delay

//@Composable
//fun GroNavHost() {
//    val navController = rememberNavController()
//    NavHost(navController, startDestination = "home") {
//        composable("home") {  GroceryApp() }
//    }
//}


@Composable
fun MyCafe() {
    ParticeProjectTheme {
        val navController = rememberNavController()

        val sampleProducts = listOf(
            Product("Red Velvet", "200", "https://thebusybaker.ca/wp-content/uploads/2020/02/red-velvet-cake-fb-ig-5-scaled.jpg","Cake"),
            Product("Dark Chocolate", "220", "https://tse2.mm.bing.net/th/id/OIP.ZyqLlushKN-qThkwMbexTAHaE7?rs=1&pid=ImgDetMain&o=7&rm=3","Cake"),
            Product("Strawberry", "160", "https://chelsweets.com/wp-content/uploads/2022/01/cake-slice-glamour-shot-better-1080x864.jpg","Cake"),
            Product("Black Forest", "140", "https://c8.alamy.com/comp/2M47JTC/multi-layered-black-forest-cake-slice-with-cherries-cream-and-chocolate-sponge-2M47JTC.jpg","Cake"),
            Product("Fruits Cake", "160", "https://www.wonderfulcake.com.my/cdn/shop/products/WC-web-11-Fruit-Passion-iii.jpg?v=1561617515&width=533","Cake"),
            Product("Vanilla Peach Cake", "200", "https://i.pinimg.com/originals/b1/cd/3b/b1cd3b72085968ac25ad47a1f34a4e34.webp","Cake"),
            Product("Cookie Dough Cheesecake", "220", "https://images.edge-generalmills.com/44e6e682-087a-4e48-8ff9-a0ecd3d6d5eb.jpg","Cheese"),
            Product("S'Mores Cheesecake", "250", "https://www.theodysseyonline.com/media-library/image.jpg?id=14641806&width=620&quality=80","Cheese"),
            Product("Turtle Cheesecake", "220", "https://www.sprinklesomesugar.com/wp-content/uploads/2014/11/IMG_2430-3.jpg","Cheese"),
            Product("Pumpkin Cheesecake", "180", "https://www.theodysseyonline.com/media-library/image.jpg?id=14641814&width=720&quality=80","Cheese"),
            Product("Oreo Cheesecake", "200", "https://www.theodysseyonline.com/media-library/image.jpg?id=14641796&width=640&quality=80","Cheese"),
            Product("Black Coffee", "150", "https://twigscafe.com/wp-content/uploads/2021/06/dark-organic-black-coffee-edited-1.jpg","Coffee"),
            Product("Coffee and Cream", "150", "https://twigscafe.com/wp-content/uploads/2021/06/coffee-with-milk-edited-560x747.jpg","Coffee"),
            Product("Espresso", "200", "https://twigscafe.com/wp-content/uploads/2021/06/cup-of-coffee-espresso-edited-560x747.jpg","Coffee"),
            Product("Cappuccino", "200", "https://twigscafe.com/wp-content/uploads/2021/06/capuccino-edited-560x747.jpg","Coffee"),
            Product("Latte", "200", "https://twigscafe.com/wp-content/uploads/2021/06/cup-of-coffee-with-latte-art-edited-560x747.jpg","Coffee"),
            Product("Caramel Latte", "200", "https://twigscafe.com/wp-content/uploads/2021/06/latte-with-whiped-cream-and-caramel-edited-560x747.jpg","Coffee"),
            Product("Iced Coffee", "200", "https://twigscafe.com/wp-content/uploads/2021/06/iced-coffee-in-glasses-edited-560x747.jpg","Coffee"),
            Product("Iced Americano", "200", "https://twigscafe.com/wp-content/uploads/2021/06/americano-coffee-in-a-mug-on-your-desk-edited.jpg","Coffee"),


        )

        val cartState = remember { CartState(sampleProducts) }

        NavHost(navController = navController, startDestination = SealedClassScreen.Splash.route) {
            composable(SealedClassScreen.Splash.route) {
                SplashScreen(
                    onSplashFinished = {
                        navController.navigate(SealedClassScreen.Login.route) {
                            popUpTo(SealedClassScreen.Splash.route) { inclusive = true }
                        }
                    }
                )
            }
            // 🔐 Login Screen
            composable(SealedClassScreen.Login.route) {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(SealedClassScreen.Home.route) {
                            popUpTo(SealedClassScreen.Login.route) { inclusive = true }
                        }
                    },
                    onNavigateToSignup = {
                        navController.navigate(SealedClassScreen.Signup.route)
                    }
                )
            }

            // 📝 Signup Screen
            composable(SealedClassScreen.Signup.route) {
                SignupScreen(
                    onSignupSuccess = {
                        navController.navigate(SealedClassScreen.Home.route) {
                            popUpTo(SealedClassScreen.Signup.route) { inclusive = true }
                        }
                    },
                    onNavigateToLogin = {
                        navController.popBackStack()
                    }
                )
            }
            composable(SealedClassScreen.Home.route) {
                GroceryHomeScreen(
                    products = sampleProducts,
                    cartState = cartState,
                    navController = navController,
                    onProductClick = { product ->
                        navController.currentBackStackEntry?.savedStateHandle?.set("product", product)
                        navController.navigate(SealedClassScreen.ProductDetails.route)
                    }
                )
            }

            composable(SealedClassScreen.ProductDetails.route) {
                val product = navController.previousBackStackEntry
                    ?.savedStateHandle?.get<Product>("product")
                product?.let {
                    ProductDetailsScreen(
                        product = it,
                        cartState = cartState,
                        navController = navController,
                        onBack = { navController.popBackStack() }
                    )
                }
            }

            composable(SealedClassScreen.Cart.route) {
                CartScreen(
                    cartState = cartState,
                    productMap = cartState.getProductMap(),
                    onBack = { navController.popBackStack() }
                )
            }

            composable(SealedClassScreen.Profile.route) {
                ProfileScreen(
                    onLogout = {
                        navController.navigate(SealedClassScreen.Login.route) {
                            popUpTo(SealedClassScreen.Home.route) { inclusive = true }
                        }
                    },
                    onBack = { navController.popBackStack() }
                )
            }

        }
    }
}

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit
) {
    val CursiveFont = FontFamily(
        Font(R.font.playwritehu_regular, FontWeight.Normal)
    )
    // ⏳ Delay before navigating
    LaunchedEffect(Unit) {
        delay(2000) // 2 seconds
        onSplashFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // 🌄 Logo or Image
            Image(
                painter = rememberAsyncImagePainter("https://tse2.mm.bing.net/th/id/OIP.JUHlXBIFmOhifn4IKqPtTAHaHa?w=900&h=900&rs=1&pid=ImgDetMain&o=7&rm=3"),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ✨ App Name
            Text(
                text = "Momo Cafe",
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = CursiveFont,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "We Serve With Love",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = CursiveFont,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}


