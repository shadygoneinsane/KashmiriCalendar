package koushur.kashmirievents.database.entity


/**
 * File Description
 * Created by: Vikesh Dass
 * Email: vikeshdass@gmail.com
 */
annotation class SpecialDays {
    companion object {
        private const val sankranti = "Sankranti"
        private const val kheerBhawani = "Mata Kheer Bhawani Yatra"
        private const val roopBhavani = "Mata Roop Bhawani Prakash Utsav"
        private const val zyethAshtami = "Zyeth Ashtami"
        private const val nirzalaEkadashi = "Nirzala Ekadashi"
        private const val bhimsenEkadashi = "Bhimsen Ekadashi"
        private const val vaisaakhi = "Vaisaakhi"
        private const val ganeshChaturdashi = "Ganesh Chaturdashi"
        private const val jwalaChaturdashi = "Jwala Chaturdashi"
        private const val guruPoornima = "Guru Poornima"

        val specialDayEvents = mapOf(
            sankranti to 0,
            kheerBhawani to 1,
            roopBhavani to 2,
            zyethAshtami to 3,
            nirzalaEkadashi to 4,
            bhimsenEkadashi to 5,
            vaisaakhi to 6,
            ganeshChaturdashi to 7,
            jwalaChaturdashi to 8,
            guruPoornima to 9
        )
    }
}