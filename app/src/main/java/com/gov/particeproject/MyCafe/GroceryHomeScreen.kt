package com.gov.particeproject.MyCafe

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.navigation.NavController
import com.gov.particeproject.R
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.runtime.derivedStateOf

@Parcelize
data class Product(
    val name: String,
    val price: String,
    val imageUrl: String,
    val type: String
) : Parcelable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroceryHomeScreen(
    products: List<Product>,
    cartState: CartState,
    navController: NavController,
    onProductClick: (Product) -> Unit
) {
    val pageSize = 20
    var currentPage by remember { mutableStateOf(0) }
    var isLoading by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf("All") }

    val listState = rememberLazyGridState()
    val snackbarHostState = remember { SnackbarHostState() }
    var showLimitSnackbar by remember { mutableStateOf(false) }

    val CursiveFont = FontFamily(Font(R.font.playwritehu_regular, FontWeight.Normal))

    var loadedProducts by remember {
        mutableStateOf(products.take(pageSize))
    }

    // 🧠 Extract unique types
    val productTypes = remember(products) {
        listOf("All") + products.map { it.type }.distinct()
    }

    // 🔍 Filter by type and search
    val filteredProducts = remember(searchQuery, selectedType, loadedProducts) {
        loadedProducts.filter {
            (selectedType == "All" || it.type.equals(selectedType, ignoreCase = true)) &&
                    it.name.contains(searchQuery, ignoreCase = true)
        }
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleItem ->
                val totalItems = listState.layoutInfo.totalItemsCount
                if (lastVisibleItem != null && lastVisibleItem >= totalItems - 4 && !isLoading) {
                    isLoading = true
                    currentPage++
                    val nextItems = products.drop(currentPage * pageSize).take(pageSize)
                    if (nextItems.isNotEmpty()) {
                        loadedProducts = loadedProducts + nextItems
                    }
                    isLoading = false
                }
            }
    }

    LaunchedEffect(showLimitSnackbar) {
        if (showLimitSnackbar) {
            snackbarHostState.showSnackbar("Max 5 items allowed per product")
            showLimitSnackbar = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Profile",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier
                                .size(28.dp)
                                .clickable {
                                    navController.navigate(SealedClassScreen.Profile.route)
                                }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "MOMO CAFE",
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontFamily = CursiveFont
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    IconButton(onClick = {
                        navController.navigate(SealedClassScreen.Cart.route)
                    }) {
                        Icon(
                            Icons.Default.ShoppingCart,
                            contentDescription = "Cart",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {

                // 🔍 Search bar
                Surface(
                    tonalElevation = 2.dp,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    TextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search...") },
                        trailingIcon = {
                            Icon(Icons.Default.Search, contentDescription = "Search Icon")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            cursorColor = MaterialTheme.colorScheme.primary,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                            focusedPlaceholderColor = Color.Gray,
                            unfocusedPlaceholderColor = Color.Gray,
                            focusedTrailingIconColor = MaterialTheme.colorScheme.primary,
                            unfocusedTrailingIconColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }

                // 🍽️ Category Filter Row
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(productTypes) { type ->
                        val isSelected = selectedType == type
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedType = type },
                            label = {
                                Text(type, color = if (isSelected)
                                    MaterialTheme.colorScheme.onPrimary
                                else
                                    MaterialTheme.colorScheme.primary)
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                                containerColor = MaterialTheme.colorScheme.surface
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 🧃 Product Grid
                if (filteredProducts.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No products found", color = Color.Gray, fontSize = 16.sp)
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        state = listState,
                        contentPadding = PaddingValues(8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.weight(1f),
                    ) {
                        items(filteredProducts) { product ->
                            val count = cartState.getCount(product)
                            ProductCard(
                                product = product,
                                count = count,
                                onAddToCart = {
                                    val added = cartState.addItem(product)
                                    if (!added) {
                                        showLimitSnackbar = true
                                    }
                                },
                                onProductClick = onProductClick
                            )
                        }

                        if (isLoading) {
                            item(span = { GridItemSpan(2) }) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                                }
                            }
                        }
                    }
                }
            }

            // 🛒 Floating View Cart Button
            val cartCount by remember { derivedStateOf { cartState.getTotalCount() } }

            if (cartCount > 0) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Button(
                        onClick = { navController.navigate(SealedClassScreen.Cart.route) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier
                            .wrapContentWidth()
                            .height(56.dp)
                    ) {
                        Text("View Cart ($cartCount items)")
                    }
                }
            }
        }
    }
}


@Composable
fun ProductCard(
    product: Product,
    count: Int,
    onAddToCart: (Product) -> Unit,
    onProductClick: (Product) -> Unit
) {
    val CursiveFont = FontFamily(
        Font(R.font.cardtextitalic, FontWeight.Normal)
    )
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onProductClick(product) }
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Image(
                painter = rememberAsyncImagePainter(product.imageUrl),
                contentDescription = product.name,
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = product.name,
                fontFamily = CursiveFont,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = "₹${product.price}",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Added: $count / 5",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { onAddToCart(product) },
//                enabled = count < 5,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text("Add to Cart")
            }
        }
    }
}




