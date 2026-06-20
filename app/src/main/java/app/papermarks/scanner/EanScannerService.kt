package app.papermarks.scanner

import app.papermarks.domain.book.BookIdentity
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode

class EanScannerService {
    fun createScanner(): BarcodeScanner {
        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_EAN_13)
            .build()
        return BarcodeScanning.getClient(options)
    }

    fun parseValidEan13(rawValue: String?): String? {
        val value = rawValue?.filter(Char::isDigit).orEmpty()
        return if (BookIdentity.isValidEan13(value)) value else null
    }
}
