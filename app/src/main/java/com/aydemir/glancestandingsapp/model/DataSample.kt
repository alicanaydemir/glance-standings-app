package com.aydemir.glancestandingsapp.model

object DataSample {

    fun getListStandings(): List<Team> {
        return arrayListOf(
            Team(id = 1, "Liverpool", position = 1, point = 45),
            Team(id = 2, "Arsenal", position = 2, point = 39),
            Team(id = 3, "Not. Forest", position = 3, point = 37),
            Team(id = 4, "Chelsea", position = 4, point = 35),
            Team(id = 5, "Newcastle", position = 5, point = 32),
            Team(id = 6, "Man. City", position = 6, point = 31),
            Team(id = 7, "Bournemouth", position = 7, point = 30),
            Team(id = 8, "Fulham", position = 8, point = 29),
            Team(id = 9, "Aston Villa", position = 9, point = 29),
            Team(id = 10, "Brighton", position = 10, point = 27),
            Team(id = 11, "Tottenham", position = 11, point = 24),
            Team(id = 12, "Brentford", position = 12, point = 24),
            Team(id = 13, "West Ham", position = 13, point = 23),
            Team(id = 14, "Man. United", position = 14, point = 22),
            Team(id = 15, "Crystal Palace", position = 15, point = 20),
            Team(id = 16, "Everton", position = 16, point = 17),
            Team(id = 17, "Wolverhampton", position = 17, point = 16),
            Team(id = 18, "Ipswich Town", position = 18, point = 15),
            Team(id = 19, "Leicester City", position = 19, point = 14),
            Team(id = 20, "Southampton", position = 20, point = 6)
        )
    }

}