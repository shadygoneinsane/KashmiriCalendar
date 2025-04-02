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
        private const val choram = "Choram"
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

        val daysList = listOf(
            amavasya, okdoh, dwitya, tritya, choram, paancham, shishthi, satam,
            ashtami, navum, dhashmi, ekadashi, dwadashi, triyodashi, chaturdashi, poornima
        )

        val highlights = listOf(amavasya, ashtami, ekadashi, poornima)

        val days = mapOf(
            amavasya to 0,
            okdoh to 1,
            dwitya to 2,
            tritya to 3,
            choram to 4,
            paancham to 5,
            shishthi to 6,
            satam to 7,
            ashtami to 8,
            navum to 9,
            dwadashi to 10,
            ekadashi to 11,
            dwadashi to 12,
            triyodashi to 13,
            chaturdashi to 14,
            poornima to 15
        )
    }
}
