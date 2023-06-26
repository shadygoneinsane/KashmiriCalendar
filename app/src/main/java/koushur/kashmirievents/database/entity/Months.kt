package koushur.kashmirievents.database.entity


/**
 * For tracking year-on-year events
 * Created by: Vikesh Dass
 * Email: vikeshdass@gmail.com
 */
annotation class Months {
    companion object {
        private const val chaitraKrishnaPaksha = "Chaitra Krishna Paksha"
        private const val chaitraShuklaPaksha = "Chaitra Shukla Paksha"
        private const val vaisaakhKrishnaPaksha = "Vaishakh Krishna Paksha"
        private const val vaisaakhShuklaPaksha = "Vaishakh Shukla Paksha"
        private const val zyethKrishnaPaksha = "Zyeth Krishna Paksha"
        private const val zyethShuklaPaksha = "Zyeth Shukla Paksha"
        private const val aashaadKrishnaPaksha = "Aashaad Krishna Paksha"
        private const val aashaadShuklaPaksha = "Aashaad Shukla Paksha"
        private const val shravanKrishnaPaksha = "Shravan Krishna Paksha"
        private const val shravanShuklaPaksha = "Shravan Shukla Paksha"
        private const val bhadraKrishnaPaksha = "Bhadra Krishna Paksha"
        private const val bhadraShuklaPaksha = "Bhadra Shukla Paksha"
        private const val ashwinKrishnaPaksha = "Ashwin Krishna Paksha"
        private const val ashwinShuklaPaksha = "Ashwin Shukla Paksha"
        private const val kartikKrishnaPaksha = "Kartik Krishna Paksha"
        private const val kartikShuklaPaksha = "Kartik Shukla Paksha"
        private const val maargKrishnaPaksha = "Maarg Krishna Paksha"
        private const val maargShuklaPaksha = "Maarg Shukla Paksha"
        private const val pohKrishnaPaksha = "Poh Krishna Paksha"
        private const val pohShuklaPaksha = "Poh Shukla Paksha"
        private const val maaghKrishnaPaksha = "Maagh Krishna Paksha"
        private const val maaghShuklaPaksha = "Maagh Shukla Paksha"
        private const val phalgunKrishnaPaksha = "Phalgun Krishna Paksha"
        private const val phalgunShuklaPaksha = "Phalgun Shukla Paksha"

        val monthsList = listOf(
            chaitraKrishnaPaksha,
            chaitraShuklaPaksha,
            vaisaakhKrishnaPaksha,
            vaisaakhShuklaPaksha,
            zyethKrishnaPaksha,
            zyethShuklaPaksha,
            aashaadKrishnaPaksha,
            aashaadShuklaPaksha,
            shravanKrishnaPaksha,
            shravanShuklaPaksha,
            bhadraKrishnaPaksha,
            bhadraShuklaPaksha,
            ashwinKrishnaPaksha,
            ashwinShuklaPaksha,
            kartikKrishnaPaksha,
            kartikShuklaPaksha,
            maargKrishnaPaksha,
            maargShuklaPaksha,
            pohKrishnaPaksha,
            pohShuklaPaksha,
            maaghKrishnaPaksha,
            maaghShuklaPaksha,
            phalgunKrishnaPaksha,
            phalgunShuklaPaksha
        )
        val monthsMap = mapOf(
            chaitraKrishnaPaksha to 0,
            chaitraShuklaPaksha to 1,
            vaisaakhKrishnaPaksha to 2,
            vaisaakhShuklaPaksha to 3,
            zyethKrishnaPaksha to 4,
            zyethShuklaPaksha to 5,
            aashaadKrishnaPaksha to 6,
            aashaadShuklaPaksha to 7,
            shravanKrishnaPaksha to 8,
            shravanShuklaPaksha to 9,
            bhadraKrishnaPaksha to 10,
            bhadraShuklaPaksha to 11,
            ashwinKrishnaPaksha to 12,
            ashwinShuklaPaksha to 13,
            kartikKrishnaPaksha to 14,
            kartikShuklaPaksha to 15,
            maargKrishnaPaksha to 16,
            maargShuklaPaksha to 17,
            pohKrishnaPaksha to 18,
            pohShuklaPaksha to 19,
            maaghKrishnaPaksha to 20,
            maaghShuklaPaksha to 21,
            phalgunKrishnaPaksha to 22,
            phalgunShuklaPaksha to 23
        )
    }
}