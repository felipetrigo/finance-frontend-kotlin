package com.example.financefocus.routes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.financefocus.BottomNavigationBar
import com.example.financefocus.ContentItem
import com.seuapp.financefocus.ui.theme.FinanceFocusTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearningScreen(navController: NavController?) {
    val contentList = remember {
        listOf(
            ContentItem("Organize sua gestão financeira!", "Productions Bank"),
            ContentItem("Organize sua gestão financeira!", "Productions Bank"),
            ContentItem("Organize sua gestão financeira!", "Productions Bank"),
            ContentItem("Organize sua gestão financeira!", "Productions Bank"),
            ContentItem("Organize sua gestão financeira!", "Productions Bank"),
            ContentItem("Organize sua gestão financeira!", "Productions Bank"),
            ContentItem("Organize sua gestão financeira!", "Productions Bank"),
            ContentItem("Organize sua gestão financeira!", "Productions Bank"),
            ContentItem("Organize sua gestão financeira!", "Productions Bank"),
            ContentItem("Organize sua gestão financeira!", "Productions Bank"),
            ContentItem("Organize sua gestão financeira!", "Productions Bank")
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0052CC)
                )
            )
        },
        bottomBar = {
            BottomNavigationBar(navController = navController, currentScreen = "learning")
        },
        containerColor = Color(0xFF0052CC)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            // Search Bar
            SearchBar()

            Spacer(modifier = Modifier.height(16.dp))

            // Content List
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(contentList) { content ->
                    ContentItemCard(content = content)
                }
            }
        }
    }
}

@Composable
fun SearchBar() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(25.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = Color.Gray,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Search",
                color = Color.Gray,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun ContentItemCard(content: ContentItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Ação ao clicar */ },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Play Button
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Play",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Content Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = content.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "by ${content.author}",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun BottomNavItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(if (isSelected) Color(0xFF6B46C1) else Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isSelected) Color.White else Color.Gray,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            color = if (isSelected) Color.Black else Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun BottomNavigationBar(navController: NavController?) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            BottomNavItem(
                icon = Icons.Default.Person,
                label = "Conta",
                isSelected = false,
                onClick = { navController?.navigate("Conta") }
            )
            BottomNavItem(
                icon = Icons.Default.Home,
                label = "Início",
                isSelected = false,
                onClick = { navController?.navigate("Inicio") }
            )
            BottomNavItem(
                icon = Icons.Default.School,
                label = "Aprender",
                isSelected = false,
                onClick = { navController?.navigate("Aprender") }
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LearningScreenPreview() {
    FinanceFocusTheme {
        LearningScreen(navController = null)
    }
}