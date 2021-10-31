package `in`.gov.uidai.auasample.online.register

data class ErrorInfo(val txnId: String, val kycErrorCode: String, val authErrorCode: String)
