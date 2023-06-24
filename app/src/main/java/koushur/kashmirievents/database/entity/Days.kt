package koushur.kashmirievents.database.entity


/**
 * File Description
 * Created by: Vikesh Dass
 * Email: vikeshdass@gmail.com
 */
annotation class Days {
    companion object {
        private const val okdoh = "Okdoh"
        private const val dwitya = "Dwitya"
        private const val tritya = "Tritya"
        private const val Choram = "Choram"
        private const val paancham = "Paancham"
        private const val shishthi = "Shishthi"
        private const val satam = "Satam"
        private const val ashtami = "Ashtami"
        private const val navum = "Navum"
        private const val dhashmi = "Dhashmi"
        private const val ekadashi = "Ekadashi"
        private const val dwadashi = "Dwadashi"
        private const val triyodashi = "Triyodashi"
        private const val chaturdashi = "Chaturdashi"

        private const val poornima = "Poornima"
        private const val amavasya = "Amavasya"

        private const val zero = amavasya
        private const val one = okdoh
        private const val two = dwitya
        private const val three = tritya
        private const val four = Choram
        private const val five = paancham
        private const val six = shishthi
        private const val seven = satam
        private const val eight = ashtami
        private const val nine = navum
        private const val ten = dhashmi
        private const val eleven = ekadashi
        private const val twelve = dwadashi
        private const val thirteen = triyodashi
        private const val fourteen = chaturdashi
        private const val fifteen = poornima

        val daysList = listOf(
            amavasya, okdoh, dwitya, tritya, Choram, paancham, shishthi, satam,
            ashtami, navum, dhashmi, ekadashi, dwadashi, triyodashi, chaturdashi, poornima
        )

        val days = mapOf(
            "Amavasya" to zero,
            0 to zero,
            "Okdoh" to one,
            1 to one,
            "Dwitya" to two,
            2 to two,
            "Tritya" to three,
            3 to three,
            "Choram" to four,
            4 to four,
            "Paancham" to five,
            5 to five,
            "Shishthi" to six,
            6 to six,
            "Satam" to seven,
            7 to seven,
            "Ashtami" to eight,
            8 to eight,
            "Navum" to nine,
            9 to nine,
            "Dhashmi" to ten,
            10 to ten,
            "Ekadashi" to eleven,
            11 to eleven,
            "Dwadashi" to twelve,
            12 to twelve,
            "Triyodashi" to thirteen,
            13 to thirteen,
            "Chaturdashi" to fourteen,
            14 to fourteen,
            "Poornima" to fifteen,
            15 to fifteen
        )
    }
}
