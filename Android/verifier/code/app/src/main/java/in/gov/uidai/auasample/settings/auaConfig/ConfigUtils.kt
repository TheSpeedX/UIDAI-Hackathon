package `in`.gov.uidai.auasample.settings.auaConfig

import `in`.gov.uidai.auasample.javamethods.AssetsPropertyReader
import `in`.gov.uidai.auasample.utils.Utils
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.util.*

class ConfigUtils {

    companion object {
        private lateinit var configSharedPreferences: SharedPreferences

        const val STAGING_CONFIG_DATA = "stagingConfigData"
        const val PREPROD_CONFIG_DATA = "preProdConfigData"
        const val PROD_CONFIG_DATA = "prodConfigData"
        const val DEFAULT_CONFIG_DATA = "defaultConfigData"
        const val certFileSuffix = "-cert.p12"

        fun initialise(context: Context) {
            configSharedPreferences =
                context.getSharedPreferences("configPreferences", Context.MODE_PRIVATE)


            val assetsPropertyReader = AssetsPropertyReader(context)
            val faceAuthProperties: Properties =
                assetsPropertyReader.getProperties("face_auth.properties")

            var cert: InputStream? = null
            try {
                cert = context.assets.open(faceAuthProperties.getProperty("PUBLIC_KEY_CER"))
            } catch (e: IOException) {
                e.printStackTrace()
            }
            val certContent = cert?.bufferedReader().use { it?.readText() }

            val configParams = ConfigParams(
                auaCode = faceAuthProperties.getProperty("AUA_CODE"),
                auaLicenceKey = faceAuthProperties.getProperty("AUA_LICENSE_KEY"),
                asaCode = faceAuthProperties.getProperty("ASA_CODE"),
                asaLicenceKey = faceAuthProperties.getProperty("ASA_LICENSE_KEY"),
                signingCert = certContent!!,
                authUrl = faceAuthProperties.getProperty("KYC_STAGING_URL"),
                eKycUrl = faceAuthProperties.getProperty("KYC_STAGING_URL"),
                subAUACode = faceAuthProperties.getProperty("SUB_AUA_CODE"),
                p12Password = faceAuthProperties.getProperty("P12_PASSWORD"),
                signingP12Path = "default"
            )

            saveConfigData(configParams, DEFAULT_CONFIG_DATA)

        }


        fun saveConfigData(configParams: ConfigParams, configType: String) {
            val gSon = Gson()
            configSharedPreferences.edit()
                .putString(configType, gSon.toJson(configParams))
                .apply()
        }

        fun getConfigData(configType: String): ConfigParams? {
            val gSon = Gson()
            if (configType == STAGING_CONFIG_DATA) {
                var storedData = configSharedPreferences.getString(STAGING_CONFIG_DATA, "")
                val configData = gSon.fromJson(storedData, ConfigParams::class.java)
                return if (configData != null) configData
                else {
                    storedData = configSharedPreferences.getString(DEFAULT_CONFIG_DATA, "")
                    gSon.fromJson(storedData, ConfigParams::class.java)
                }
            } else {
                val storedData = configSharedPreferences.getString(configType, "")
                return if (storedData != "") gSon.fromJson(storedData, ConfigParams::class.java)
                else return null
            }
        }

        fun isConfigExist(): Boolean {
            if (getSelectedConfigEnv() == STAGING_CONFIG_DATA) return true
            val storedData = configSharedPreferences.getString(getSelectedConfigEnv(), "")
            return storedData != ""
        }

        fun resetConfig(context: Context, configType: String) {
            configSharedPreferences.edit().remove(configType).apply()
            val dir: File = context.filesDir
            val file = File(dir, configType + certFileSuffix)
            if (file.exists()) {
                val deleted: Boolean = file.delete()
                if (deleted) {
                    Log.e("test", "file deleted success")
                } else {
                    Log.e("test", "file deleted success")
                }
            }
        }

        fun getSelectedConfigEnv(): String {
            var configDataType = DEFAULT_CONFIG_DATA

            when (Utils.ENVIRONMENT_TAG) {
                "S" -> {
                    configDataType = STAGING_CONFIG_DATA
                }
                "PP" -> {
                    configDataType = PREPROD_CONFIG_DATA
                }
                "P" -> {
                    configDataType = PROD_CONFIG_DATA
                }
            }
            return configDataType
        }


        fun saveCertificate(context: Context, configType: String, data: InputStream) {
            try {
                val fileName = configType + certFileSuffix
                val fos =
                    context.openFileOutput(fileName, Context.MODE_PRIVATE)
                fos.write(data.readBytes())
                fos.close()
            } catch (e: Exception) {
                Log.d("test", "Error accessing file: " + e.message)
            }
        }
    }
}