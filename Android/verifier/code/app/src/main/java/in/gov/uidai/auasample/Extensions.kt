package `in`.gov.uidai.auasample

import android.content.Intent
import android.content.pm.PackageManager

fun Intent.checkIfIntentResolves(packageManager: PackageManager): Boolean = resolveActivity(packageManager) != null