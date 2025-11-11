package com.example.financefocus.routes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
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
import com.example.financefocus.Routes
import com.seuapp.financefocus.ui.theme.FinanceFocusTheme
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearningScreen(navController: NavController?) {
    val contentList = remember {
        listOf(
            ContentItem("Como organizar suas finanças e guardar dinheiro?", "Bruno Perini", "https://www.youtube.com/watch?v=C67qPfI8_hg"),
            ContentItem("O que são Fundos Imobiliários (FIIs) e como investir em Fundos Imobiliários?", "Breno Perrucho", "https://www.youtube.com/watch?v=vZ64S8dFpEM"),
            ContentItem("GUIA BÁSICO PRA INVESTIR EM AÇÕES: TUDO que você PRECISA SABER antes de investir em AÇÕES!", "Primo Rico", "youtube.com/watch?v=yHuNhkntc-I&pp=ygUYY29tbyBpbnZlc3RpciBlbSBhw6fDtWVz"),
            ContentItem("COMO MONTAR UMA CARTEIRA DE AÇÕES PARA INICIANTES NA PRÁTICA!", "Geração de dividendos", "https://www.youtube.com/watch?v=96KWmZCefso")
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

            // Header
            Text(
                text = "APRENDIZADO",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 20.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Content List
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(contentList) { content ->
                    ContentItemCard(content = content, navController = navController)
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
fun ContentItemCard(content: ContentItem, navController: NavController?) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController?.navigate(
                    "video/${
                        URLEncoder.encode(content.uri, StandardCharsets.UTF_8.name())
                    }/${
                        URLEncoder.encode(content.title, StandardCharsets.UTF_8.name())
                    }/${
                        URLEncoder.encode(content.author, StandardCharsets.UTF_8.name())
                    }"
                )
            },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = content.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = content.author,
                fontSize = 14.sp,
                color = Color.Gray
            )
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