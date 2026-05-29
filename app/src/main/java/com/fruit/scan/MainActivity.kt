package com.fruit.scan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// --- Data Models ---
data class DashboardUiState(
    val isOnline: Boolean = false,
    val conveyorActive: Boolean = true,
    val passCount: Int = 0,
    val rejectCount: Int = 0,
    val passWeight: Float = 0f,
    val rejectWeight: Float = 0f
)

// --- ViewModel ---
class DashboardViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    private val database = FirebaseDatabase.getInstance("https://qc-conveyer-system-default-rtdb.asia-southeast1.firebasedatabase.app")
    private val dbRef = database.reference

    init {
        listenToFirebase()
    }

    private fun listenToFirebase() {
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                _uiState.update {
                    it.copy(
                        passCount = snapshot.child("passCount").getValue(Int::class.java) ?: 0,
                        rejectCount = snapshot.child("rejectCount").getValue(Int::class.java) ?: 0,
                        passWeight = snapshot.child("passWeight").getValue(Float::class.java) ?: 0f,
                        rejectWeight = snapshot.child("rejectWeight").getValue(Float::class.java) ?: 0f,
                        conveyorActive = snapshot.child("conveyorStatus").getValue(String::class.java) == "Running",
                        isOnline = true
                    )
                }
            }
            override fun onCancelled(error: DatabaseError) {
                _uiState.update { it.copy(isOnline = false) }
            }
        })
    }
}

// --- Main Activity ---
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FruityScanTheme {
                DashboardScreen()
            }
        }
    }
}

@Composable
fun DashboardScreen(viewModel: DashboardViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFF0F0F0F)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(28.dp)
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            
            StatusHeaderCard(uiState.isOnline, uiState.conveyorActive)

            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                SectionTitle("Live Sorting Count")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CountCard(
                        label = "PASS FRUIT",
                        count = uiState.passCount,
                        accentColor = Color(0xFF4CAF50),
                        modifier = Modifier.weight(1f)
                    )
                    CountCard(
                        label = "REJECTED FRUIT",
                        count = uiState.rejectCount,
                        accentColor = Color(0xFFF44336),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                SectionTitle("Real-Time Weight Data")
                WeightPanel(
                    label = "PASS FRUIT WEIGHT",
                    subLabel = "HX711 Load Cell 1",
                    weight = uiState.passWeight,
                    accentColor = Color(0xFF4CAF50)
                )
                WeightPanel(
                    label = "REJECTED FRUIT WEIGHT",
                    subLabel = "HX711 Load Cell 2",
                    weight = uiState.rejectWeight,
                    accentColor = Color(0xFFF44336)
                )
            }
            
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun StatusHeaderCard(isOnline: Boolean, conveyorActive: Boolean) {
    ElevatedCard(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                "FruityScan Dashboard",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Gray,
                letterSpacing = 1.sp
            )
            Text(
                "Connected",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                StatusIndicator(
                    icon = Icons.Default.CheckCircle,
                    label = if (isOnline) "ONLINE" else "OFFLINE",
                    color = if (isOnline) Color(0xFF4CAF50) else Color.Gray
                )
                StatusIndicator(
                    icon = Icons.Default.PlayArrow,
                    label = if (conveyorActive) "Conveyor: ACTIVE" else "Conveyor: STOPPED",
                    color = if (conveyorActive) Color(0xFF81D4FA) else Color.Gray
                )
            }
        }
    }
}

@Composable
fun StatusIndicator(icon: ImageVector, label: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text(label, style = MaterialTheme.typography.labelLarge, color = color, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        modifier = Modifier.padding(start = 4.dp)
    )
}

@Composable
fun CountCard(label: String, count: Int, accentColor: Color, modifier: Modifier) {
    ElevatedCard(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A)),
        modifier = modifier.height(150.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp).fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, style = MaterialTheme.typography.labelMedium, color = Color.LightGray)
            Text(
                count.toString(),
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Black,
                color = accentColor
            )
        }
    }
}

@Composable
fun WeightPanel(label: String, subLabel: String, weight: Float, accentColor: Color) {
    ElevatedCard(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(24.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(label, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
                Text(subLabel, style = MaterialTheme.typography.labelSmall, color = Color.DarkGray)
            }
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    "%.1f".format(weight),
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = accentColor
                )
                Text(
                    " g",
                    modifier = Modifier.padding(bottom = 6.dp, start = 4.dp),
                    style = MaterialTheme.typography.titleMedium,
                    color = accentColor.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Composable
fun FruityScanTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(primary = Color(0xFF81D4FA)),
        content = content
    )
}
