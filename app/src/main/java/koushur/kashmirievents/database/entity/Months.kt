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
            1 to chaitraKrishnaPaksha,
            "Chaitra Krishna Paksha" to chaitraKrishnaPaksha,
            2 to chaitraShuklaPaksha,
            "Chaitra Shukla Paksha" to chaitraShuklaPaksha,
            3 to vaisaakhKrishnaPaksha,
            "Vaishakh Krishna Paksha" to vaisaakhKrishnaPaksha,
            4 to vaisaakhShuklaPaksha,
            "Vaishakh Shukla Paksha" to vaisaakhShuklaPaksha,
            5 to zyethKrishnaPaksha,
            "Zyeth Krishna Paksha" to zyethKrishnaPaksha,
            6 to zyethShuklaPaksha,
            "Zyeth Shukla Paksha" to zyethShuklaPaksha,
            7 to aashaadKrishnaPaksha,
            "Aashaad Krishna Paksha" to aashaadKrishnaPaksha,
            8 to aashaadShuklaPaksha,
            "Aashaad Shukla Paksha" to aashaadShuklaPaksha,
            9 to shravanKrishnaPaksha,
            "Shravan Krishna Paksha" to shravanKrishnaPaksha,
            10 to shravanShuklaPaksha,
            "Shravan Shukla Paksha" to shravanShuklaPaksha,
            11 to bhadraKrishnaPaksha,
            "Bhadra Krishna Paksha" to bhadraKrishnaPaksha,
            12 to bhadraShuklaPaksha,
            "Bhadra Shukla Paksha" to bhadraShuklaPaksha,
            13 to ashwinKrishnaPaksha,
            "Ashwin Krishna Paksha" to ashwinKrishnaPaksha,
            14 to ashwinShuklaPaksha,
            "Ashwin Shukla Paksha" to ashwinShuklaPaksha,
            15 to kartikKrishnaPaksha,
            "Kartik Krishna Paksha" to kartikKrishnaPaksha,
            16 to kartikShuklaPaksha,
            "Kartik Shukla Paksha" to kartikShuklaPaksha,
            17 to maargKrishnaPaksha,
            "Maarg Krishna Paksha" to maargKrishnaPaksha,
            18 to maargShuklaPaksha,
            "Maarg Shukla Paksha" to maargShuklaPaksha,
            19 to pohKrishnaPaksha,
            "Poh Krishna Paksha" to pohKrishnaPaksha,
            20 to pohShuklaPaksha,
            "Poh Shukla Paksha" to pohShuklaPaksha,
            21 to maaghKrishnaPaksha,
            "Maagh Krishna Paksha" to maaghKrishnaPaksha,
            22 to maaghShuklaPaksha,
            "Maagh Shukla Paksha" to maaghShuklaPaksha,
            23 to phalgunKrishnaPaksha,
            "Phalgun Krishna Paksha" to phalgunKrishnaPaksha,
            24 to phalgunShuklaPaksha,
            "Phalgun Shukla Paksha" to phalgunShuklaPaksha,

            )
    }
}