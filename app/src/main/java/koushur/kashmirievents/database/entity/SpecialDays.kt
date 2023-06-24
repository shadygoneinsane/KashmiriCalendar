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
        private const val vaisaakhi = "Vaisaakhi"
        private const val ganeshChaturdashi = "Ganesh Chaturdashi"

        val specialDayEvents = mapOf(
            "Sankranti" to sankranti,
            "Mata Kheer Bhawani Yatra" to kheerBhawani,
            "Mata Roop Bhawani Prakash Utsav" to roopBhavani,
            "Zyeth Ashtami" to zyethAshtami,
            "Nirzala Ekadashi" to nirzalaEkadashi,
            "Vaisaakhi" to vaisaakhi,
            "Ganesh Chaturdashi" to ganeshChaturdashi
        )
    }
}