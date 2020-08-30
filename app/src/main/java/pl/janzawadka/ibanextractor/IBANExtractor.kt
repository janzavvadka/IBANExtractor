package pl.janzawadka.ibanextractor

import android.content.Context
import android.content.ClipData
import android.content.ClipboardManager
import android.widget.Toast

private const val IBAN_REGEX = """\d{26}"""

class IBANExtractor(val context: Context) {

    fun extractFromClipboard() {
        val textToExtract = getFromClipboard()
        extract(textToExtract, true)
    }

    fun extract(textToExtract: String, isFromClipboard: Boolean = false) {
        val textToExtractWithoutSpace = textToExtract.replace("\\s".toRegex(), "");
        val extractResult = Regex(IBAN_REGEX).find(textToExtractWithoutSpace)

        if (extractResult != null) {
            saveInClipboard(extractResult.value);
            Toast.makeText(context, "IBAN save in clipboard", Toast.LENGTH_SHORT).show()
        } else {
            if(isFromClipboard) {
                Toast.makeText(context, "No IBAN in clipboard", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "No IBAN in text", Toast.LENGTH_SHORT).show()
            }

        }
    }

    fun saveInClipboard(iban: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("result", iban)
        clipboard.setPrimaryClip(clip)
    }

    fun getFromClipboard(): String {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val pData = clipboard.primaryClip
        val item = pData!!.getItemAt(0)
        return item.text.toString()
    }

}