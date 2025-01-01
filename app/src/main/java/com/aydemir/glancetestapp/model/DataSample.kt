package com.aydemir.glancetestapp.model

object DataSample {

    fun getListStandings(): List<Team> {
        return arrayListOf(
            Team(id = 1, "Galatasaray", position = 1, point = 44),
            Team(id = 2, "Fenerbahçe", position = 2, point = 36),
            Team(id = 3, "Samsunspor", position = 3, point = 30),
            Team(id = 4, "Göztepe", position = 4, point = 28),
            Team(id = 5, "Eyüpspor", position = 5, point = 27),
            Team(id = 6, "Beşiktaş", position = 6, point = 26),
            Team(id = 7, "Başakşehir", position = 7, point = 23),
            Team(id = 8, "Gaziantep", position = 8, point = 21),
            Team(id = 9, "Antalyaspor", position = 9, point = 21),
            Team(id = 10, "Kasımpaşa", position = 10, point = 20),
            Team(id = 11, "Konyaspor", position = 11, point = 20),
            Team(id = 12, "Rizespor", position = 12, point = 20),
            Team(id = 13, "Trabzonspor", position = 13, point = 19),
            Team(id = 14, "Sivasspor", position = 14, point = 19),
            Team(id = 15, "Alanyaspor", position = 15, point = 18),
            Team(id = 16, "Kayserispor", position = 16, point = 15),
            Team(id = 17, "Bodrumspor", position = 17, point = 14),
            Team(id = 18, "Hatayspor", position = 18, point = 9),
            Team(id = 19, "Adana Demirspor", position = 19, point = 9),
        )
    }

}