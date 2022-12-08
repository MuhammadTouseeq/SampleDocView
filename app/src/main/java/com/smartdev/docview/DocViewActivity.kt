package com.smartdev.docview

import android.app.ProgressDialog
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.Bundle
import android.text.format.Formatter.formatIpAddress
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.shockwave.pdfium.PdfDocument
import com.shockwave.pdfium.PdfiumCore
import kotlinx.android.synthetic.main.activity_docview.*
import java.util.*
import  android.text.format.Formatter

class DocViewActivity : AppCompatActivity() {

    private lateinit var pDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_docview)

       // init(url = "http://www.pdf995.com/samples/pdf.pdf")
        init(url = "https://api.staging.joeyco.com/orders/604cd06c4bd00-1615646828.pdf")
//        init(url = "https://s23.q4cdn.com/202968100/files/doc_downloads/test.pdf")

        //listener()
       // generateImageFromPdf(Uri.parse("https://s23.q4cdn.com/202968100/files/doc_downloads/test.pdf"))

    }
    fun generateImageFromPdf(pdfUri: Uri?) {
        val pageNumber = 0
        val pdfiumCore = PdfiumCore(this)
        try {
            //http://www.programcreek.com/java-api-examples/index.php?api=android.os.ParcelFileDescriptor
            val fd = contentResolver.openFileDescriptor(pdfUri!!, "r")
            val pdfDocument: PdfDocument = pdfiumCore.newDocument(fd)
            pdfiumCore.openPage(pdfDocument, pageNumber)
            val width = pdfiumCore.getPageWidthPoint(pdfDocument, pageNumber)
            val height = pdfiumCore.getPageHeightPoint(pdfDocument, pageNumber)
            val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            pdfiumCore.renderPageBitmap(pdfDocument, bmp, pageNumber, 0, 0, width, height)
           // saveImage(bmp)
            pdfiumCore.closeDocument(pdfDocument) // important!
        } catch (e: Exception) {
           e.printStackTrace()
        }
    }
    private fun init(url: String) {
       // val url = Uri.encode(url)
        val baseUrl = "https://docs.google.com/viewer?url=$url&embedded=true"
       // val baseUrl = "https://docs.google.com/gview?embedded=true&url=" + url
        webview.settings.javaScriptEnabled = true
        webview.settings.useWideViewPort = true
        webview.settings.builtInZoomControls=true
        webview.settings.loadWithOverviewMode=true
        webview.scrollBarStyle= View.SCROLLBARS_OUTSIDE_OVERLAY
        webview.settings.setSupportZoom(true)
        webview.webViewClient = AppWebViewClients()

        webview.loadUrl(baseUrl)
    }


}