package com.gov.particeproject.MyCafe

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.gov.particeproject.R

data class CartSummary(
    val subtotal: Double,
    val sgst: Double,
    val cgst: Double,
    val total: Double
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    cartState: CartState,
    productMap: Map<String, Product>,
    onBack: () -> Unit
) {
    val cartItems = cartState.getAllItems()
    val summary = calculateCartSummary(cartItems, productMap)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Your Cart",
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                cartItems.forEach { (key, quantity) ->
                    val product = productMap[key] ?: return@forEach
                    val price = product.price.toDoubleOrNull() ?: 0.0
                    val total = price * quantity

                    item {
                        Card(
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                contentColor = MaterialTheme.colorScheme.onSurface
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(12.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AsyncImage(
                                    model = product.imageUrl,
                                    contentDescription = product.name,
                                    modifier = Modifier
                                        .size(64.dp)
                                        .clip(RoundedCornerShape(8.dp)),
                                    contentScale = ContentScale.Crop
                                )

                                Spacer(Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        product.name,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        "₹${price} x $quantity",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                    )

                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        IconButton(onClick = { cartState.decreaseItem(product) }) {
                                            Icon(
                                                imageVector = ImageVector.vectorResource(id = R.drawable.mince),
                                                contentDescription = "Decrease",
                                                tint = MaterialTheme.colorScheme.primary
                                            )
                                        }
                                        Text(
                                            "$quantity",
                                            modifier = Modifier.padding(horizontal = 8.dp),
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        IconButton(onClick = { cartState.increaseItem(product) }) {
                                            Icon(
                                                Icons.Default.Add,
                                                contentDescription = "Increase",
                                                tint = MaterialTheme.colorScheme.primary
                                            )
                                        }
                                    }
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        "₹${"%.2f".format(total)}",
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    IconButton(onClick = { cartState.removeItem(product) }) {
                                        Icon(
                                            Icons.Default.Delete,
                                            contentDescription = "Remove",
                                            tint = MaterialTheme.colorScheme.error
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Divider(color = MaterialTheme.colorScheme.outline)

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text("Subtotal: ₹${"%.2f".format(summary.subtotal)}", color = MaterialTheme.colorScheme.onBackground)
                Text("SGST (9%): ₹${"%.2f".format(summary.sgst)}", color = MaterialTheme.colorScheme.onBackground)
                Text("CGST (9%): ₹${"%.2f".format(summary.cgst)}", color = MaterialTheme.colorScheme.onBackground)
                Text(
                    "Total: ₹${"%.2f".format(summary.total)}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = { /* Handle checkout */ },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text("Proceed to Checkout")
                }
            }
        }
    }
}

fun calculateCartSummary(
    cartItems: Map<String, Int>,
    productMap: Map<String, Product>
): CartSummary {
    var subtotal = 0.0

    for ((key, quantity) in cartItems) {
        val product = productMap[key] ?: continue
        val price = product.price.toDoubleOrNull() ?: 0.0
        subtotal += price * quantity
    }

    val sgst = subtotal * 0.09
    val cgst = subtotal * 0.09
    val total = subtotal + sgst + cgst

    return CartSummary(subtotal, sgst, cgst, total)
}

