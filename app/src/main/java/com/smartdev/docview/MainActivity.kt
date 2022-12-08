package com.smartdev.docview


import android.app.DownloadManager
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File


class MainActivity : AppCompatActivity() {

    private lateinit var pDialog: ProgressDialog

    companion object{
        const val FOLDER_NAME="JoeyCo"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)




      /*  webview.webViewClient = AppWebViewClients()
        webview.settings.javaScriptEnabled = true
        webview.settings.useWideViewPort = true*/

init()
        listener()


//downloadPdf("https://www.clickdimensions.com/links/TestPDFfile.pdf","Download","","")
//return
        //val destinationPath = File(Environment.getStorageDirectory().absolutePath+"/"+Environment.DIRECTORY_DOWNLOADS.toString() + "/JoeyCo/Docs")

//        if (!destinationPath.exists()) {
//            destinationPath.mkdirs()
//        }
       // val destinationPath = File(Environment.getStorageDirectory().absolutePath+"/"+Environment.DIRECTORY_DOWNLOADS.toString() + "/JoeyCo/Docs")
        pdfView.fromUri(Uri.fromFile(File(Environment.DIRECTORY_DOWNLOADS+"/JoeyCoApp/TestPDFfile.pdf")))
//        pdfView.fromFile(File(Environment.getStorageDirectory().absolutePath+"/"+Environment.DIRECTORY_DOWNLOADS.toString()+"/testabc.pdf"))
            .pages(0, 2, 1, 3, 3, 3) // all pages are displayed by default
            .enableSwipe(true) // allows to block changing pages using swipe
            .swipeHorizontal(false)
            .enableDoubletap(true)
            .defaultPage(0)
            // allows to draw something on the current page, usually visible in the middle of the screen
           // .onDraw(onDrawListener)
            // allows to draw something on all pages, separately for every page. Called only for visible pages
            //.onDrawAll(onDrawListener)
           // .onLoad(onLoadCompleteListener) // called after document is loaded and starts to be rendered
           // .onPageChange(onPageChangeListener)
          //  .onPageScroll(onPageScrollListener)
            //.onError(this)
           // .onPageError(this)
          //  .onRender(onRenderListener) // called after document is rendered for the first time
            // called on single tap, return true if handled, false to toggle scroll handle visibility
         //   .onTap(onTapListener)
            .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
            .password(null)
            .scrollHandle(null)
            .enableAntialiasing(true) // improve rendering a little bit on low-res screens
            // spacing between pages in dp. To define spacing color, set view background
            //.spacing(0)
            //.invalidPageColor(Color.WHITE) // color of page that is invalid and cannot be loaded
            .load();
    }

    private fun init() {

        val baseUrl="https://drive.google.com/viewerng/viewer?embedded=true&url="
        webview.settings.javaScriptEnabled = true
        pDialog = ProgressDialog(this@MainActivity)
        pDialog.setTitle("PDF")
        pDialog.setMessage("Loading...")
        pDialog.setIndeterminate(false)
        pDialog.setCancelable(false)
        webview.loadUrl(baseUrl+"http://www.pdf995.com/samples/pdf.pdf")
    }

    private fun listener() {
        webview.webViewClient = object : WebViewClient() {
            override fun onPageStarted(
                view: WebView,
                url: String,
                favicon: Bitmap
            ) {
                super.onPageStarted(view, url, favicon)
                pDialog.show()
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                pDialog.dismiss()
            }
        }
    }
    fun downloadPdf(
        url: String?,
        sem: String,
        title: String,
        branch: String
    ) {

        val destinationPath = File(Environment.DIRECTORY_DOWNLOADS+"/JoeyCoApp")

        if (!destinationPath.exists()) {
            destinationPath.mkdirs()
        }

        val downloadManager =
            getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        val Download_Uri = Uri.parse(url)
        val request = DownloadManager.Request(Download_Uri)

        //Restrict the types of networks over which this download may proceed.
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        //Set whether this download may proceed over a roaming connection.
        request.setAllowedOverRoaming(false)
        //Set the title of this download, to be displayed in notifications (if enabled).
        request.setTitle("Downloading")
        //Set a description of this download, to be displayed in notifications (if enabled)
        request.setDescription("Downloading File")
        //Set the local destination for the downloaded file to a path within the application's external files directory
//        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,
//            title + "_" + branch + "_" + sem + "Year" + System.currentTimeMillis() + ".pdf")

        request.setDestinationInExternalPublicDir(destinationPath.toString(),Download_Uri.lastPathSegment)

        //Enqueue a new download and same the referenceId
        downloadManager.enqueue(request)
    }

    /*  Helper method to create file*/
    @Nullable
    private fun generateFile(path: String, fileName: String): File? {
        var file: File? = null
        if (isExternalStorageAvailable()) {
            val root = File(Environment.getExternalStorageDirectory().absolutePath,
                    FOLDER_NAME.toString() + File.separator + path)
            var dirExists = true
            if (!root.exists()) {
                dirExists = root.mkdirs()
            }
            if (dirExists) {
                file = File(root, fileName)
            }
        }
        return file
    }

    /* Helper method to determine if external storage is available*/
    private fun isExternalStorageAvailable(): Boolean {
        return Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()
    }


}