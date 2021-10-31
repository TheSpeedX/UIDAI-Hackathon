package `in`.gov.uidai.auasample.settings.auaConfig

data class ConfigParams(
    val auaCode: String,
    val auaLicenceKey: String,
    val asaCode: String,
    val asaLicenceKey: String,
    val subAUACode: String,
    val signingCert: String,
    val authUrl: String,
    val eKycUrl: String,
    val signingP12Path: String,
    val p12Password: String
)
