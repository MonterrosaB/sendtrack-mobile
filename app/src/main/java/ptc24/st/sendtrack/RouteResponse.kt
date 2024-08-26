package ptc24.st.sendtrack

import com.google.gson.annotations.SerializedName

data class RouteResponse(@SerializedName("features")val features:List<Feature>)
data class Feature(@SerializedName("geometry")val geometry:Geometry)
data class Geometry(@SerializedName("coordinates")val coordinates:List<List<Double>>)

data class DirectionsResponse(
    val routes: List<Route>
)

data class Route(
    val segments: List<Segment>
)

data class Segment(
    val distance: Double,
    val duration: Double,
    val steps: List<Step>
)

data class Step(
    val instruction: String,
    val distance: Double,
    val duration: Double
)
