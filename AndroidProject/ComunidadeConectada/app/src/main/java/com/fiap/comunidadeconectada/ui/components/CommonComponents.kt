package com.fiap.comunidadeconectada.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.fiap.comunidadeconectada.ui.theme.*

// Card de Recurso (Item no Feed) - Versão Renovada
@Composable
fun ResourceCard(
    title: String,
    description: String,
    reputation: Float = 4.5f,
    offerantName: String,
    imageUrl: String = "",
    onCardClick: () -> Unit = {},
    onRequestClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .shadow(elevation = 8.dp, shape = RoundedCornerShape(20.dp), spotColor = Color.Black.copy(alpha = 0.1f))
            .clickable { onCardClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column {
            // Imagem do Recurso que ocupa todo o topo
            Box(modifier = Modifier.fillMaxWidth().height(180.dp)) {
                if (imageUrl.isNotEmpty()) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize().background(NeutralGray200),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Image, null, modifier = Modifier.size(48.dp), tint = NeutralGray400)
                    }
                }
                
                // Badge de Reputação sobre a imagem
                Surface(
                    modifier = Modifier.padding(12.dp).align(Alignment.TopEnd),
                    color = Color.Black.copy(alpha = 0.6f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Filled.Star, null, Modifier.size(14.dp), tint = HighlightAmber)
                        Text(
                            text = " $reputation",
                            color = Color.White,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF1F2937)
                )

                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    modifier = Modifier.padding(vertical = 6.dp),
                    maxLines = 2
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier.size(24.dp).clip(CircleShape).background(PrimaryBlueLight),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(offerantName.take(1), color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                        Text(
                            text = offerantName,
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.DarkGray,
                            modifier = Modifier.padding(start = 8.dp),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Button(
                    onClick = onRequestClick,
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp).height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SecondaryGreen),
                    shape = RoundedCornerShape(12.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                ) {
                    Text("SOLICITAR", fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                }
            }
        }
    }
}

// Botão Primário Estilizado
@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth().height(54.dp),
        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
        shape = RoundedCornerShape(16.dp),
        enabled = enabled,
        elevation = ButtonDefaults.buttonElevation(pressedElevation = 0.dp)
    ) {
        Text(text = text, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
}

// Card de Perfil do Usuário Renovado
@Composable
fun UserProfileCard(
    userName: String,
    reputation: Float = 4.5f,
    transactionCount: Int = 12,
    onEditClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(16.dp).shadow(12.dp, RoundedCornerShape(24.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier.background(
                brush = Brush.verticalGradient(
                    colors = listOf(PrimaryBlue.copy(alpha = 0.05f), Color.White)
                )
            ).padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Foto de Perfil com borda gradiente
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(NeutralGray200)
                    .border(3.dp, PrimaryBlue, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(userName.take(1), fontSize = 40.sp, color = PrimaryBlue, fontWeight = FontWeight.Black)
            }

            Text(
                text = userName,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(top = 16.dp),
                color = Color(0xFF111827)
            )

            // Estrelas Modernas
            Row(
                modifier = Modifier.padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val fullStars = reputation.toInt()
                val hasHalfStar = (reputation - fullStars) >= 0.5f

                repeat(fullStars) {
                    Icon(Icons.Filled.Star, null, tint = HighlightAmber, modifier = Modifier.size(24.dp))
                }
                if (hasHalfStar) {
                    Icon(Icons.Filled.StarHalf, null, tint = HighlightAmber, modifier = Modifier.size(24.dp))
                }
                repeat(5 - fullStars - (if (hasHalfStar) 1 else 0)) {
                    Icon(Icons.Filled.Star, null, tint = NeutralGray300, modifier = Modifier.size(24.dp))
                }
                Text(
                    text = " $reputation",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 8.dp),
                    color = Color.DarkGray
                )
            }

            Surface(
                color = PrimaryBlue.copy(alpha = 0.1f),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(
                    text = "$transactionCount trocas concluídas",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.labelLarge,
                    color = PrimaryBlue,
                    fontWeight = FontWeight.Bold
                )
            }

            Button(
                onClick = onEditClick,
                modifier = Modifier.padding(top = 24.dp).fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF3F4F6)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("EDITAR PERFIL", color = Color.DarkGray, fontWeight = FontWeight.Bold)
            }
        }
    }
}
