package com.example.leafprediction

data class DiseaseList(var PlantName: String, var LeafDisease: String)

object Disease{

    val diseaseList = listOf<DiseaseList>(
        DiseaseList("Apple", "Apple scab"),
        DiseaseList("Apple", "Black rot"),
        DiseaseList("Apple", "Cedar apple rust"),
        DiseaseList("Apple", "Healty"),
        DiseaseList("Blueberry", "Healty"),
        DiseaseList("Cherry_(including_sour)", "Powdery mildew"),
        DiseaseList("Cherry_(including_sour)", "Healty"),
        DiseaseList("Corn_(maize)", "Cercospora leaf spot Gray leaf spot"),
        DiseaseList("Corn_(maize)", "Common rust"),
        DiseaseList("Corn_(maize)", "Northern Leaf Blight"),
        DiseaseList("Corn_(maize)", "healthy"),
        DiseaseList("Grape", "Black rot"),
        DiseaseList("Grape", "Esca_(Black Measles)"),
        DiseaseList("Grape", "Leaf blight(Isariopsis Leaf Spot)"),
        DiseaseList("Grape", "healthy"),
        DiseaseList("Orange", "Haunglongbing(Citrus greening)"),
        DiseaseList("Peach", "Bacterial_spot"),
        DiseaseList("Peach", "healthy"),
        DiseaseList("Pepper, bell", "Bacterial_spot"),
        DiseaseList("Pepper, bell", "healthy"),
        DiseaseList("Potato", "Early blight"),
        DiseaseList("Potato", "Late blight"),
        DiseaseList("Potato", "healthy"),
        DiseaseList("Raspberry", "healthy"),
        DiseaseList("Soybean", "healthy"),
        DiseaseList("Squash", "Powdery mildew"),
        DiseaseList("Strawberry", "Leaf scorch"),
        DiseaseList("Strawberry", "healthy"),
        DiseaseList("Tomato", "Bacterial spot"),
        DiseaseList("Tomato", "Early blight"),
        DiseaseList("Tomato", "Late blight"),
        DiseaseList("Tomato", "Leaf Mold"),
        DiseaseList("Tomato", "Septoria leaf spot"),
        DiseaseList("Tomato", "Spider mites, Two-spotted spider mite"),
        DiseaseList("Tomato", "Target Spot"),
        DiseaseList("Tomato", "Tomato Yellow Leaf Curl Virus"),
        DiseaseList("Tomato", "Tomato mosaic virus"),
        DiseaseList("Tomato", "healthy"),

    )
}