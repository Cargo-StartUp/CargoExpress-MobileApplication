import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import android.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cargoexpress.app.core.domain.Trip
import com.cargoexpress.app.core.presentation.dashboard.DashboardViewModel

@Composable
fun DashboardScreen(viewModel: DashboardViewModel = viewModel()) {
    val sampleData = listOf(10f, 20f, 30f, 40f, 50f)
    BarChartView(data = sampleData)
}

@Composable
fun BarChartView(data: List<Trip>) {
    AndroidView(factory = { context ->
        BarChart(context).apply {
            val entries = data.mapIndexed { index, value -> BarEntry(index.toFloat(), value) }
            val dataSet = BarDataSet(entries, "Sample Data").apply {
                color = Color.BLUE
            }
            this.data = BarData(dataSet)
            this.invalidate() \
        }
    })
}