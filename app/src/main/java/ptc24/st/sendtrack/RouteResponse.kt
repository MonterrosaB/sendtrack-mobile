package ptc24.st.sendtrack

import com.google.gson.annotations.SerializedName


data class RouteResponse(@SerializedName("features")val features:List<Feature>)
data class Feature(@SerializedName("geometry")val geometry:Geometry)
data class Geometry(@SerializedName("coordinates")val coordinates:List<List<Double>>)

