@file:Suppress("DEPRECATION")

package com.example.mygreenapp

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask
import org.jsoup.Jsoup

class CnSScrapeFinal(@SuppressLint("StaticFieldLeak") val context: Context, private var previousUrlList: ArrayList<String>) : AsyncTask<Void, Void, String>() {

    // Initialize arrays for storing information from the website
    private var bannerImageList = ArrayList<String>()
    private var logoImageList = ArrayList<String>()
    private var description1List = ArrayList<String>()
    private var description2List = ArrayList<String>()
    // URL list which contains data on which parent is the current URL from
    private var parentUrlList = ArrayList<String>()
    //progressDialog
    private lateinit var progressDialog: ProgressDialog

    private var confirmCount = 0

    @Deprecated("Deprecated in Java")
    override fun onPreExecute() {
        super.onPreExecute()

        //configure progress dialog
        progressDialog = ProgressDialog(context)
        progressDialog.setTitle("Please Wait")
        progressDialog.setMessage("Retrieving Clubs and Societies third page data...")
        progressDialog.setCanceledOnTouchOutside(false)

        //show progress
        progressDialog.show()
    }

    @Deprecated("Deprecated in Java")
    override fun doInBackground(vararg params: Void?): String? {

        // Nested loop to take all the data inside clubs and societies page 2
        for (i in 0 until previousUrlList.size) {
            val doc = Jsoup.connect(previousUrlList[i]).get()

            /*
                The Clubs and societies pages in the NSBM website is in two formats. Because of this reason, we have to check if the selected page has the first
                format or the second and adjust accordingly. The if functions do exactly that
            */
            var imageInfo = doc.getElementsByClass("widget widget_revslider")
            if (imageInfo.toString() == "") {
                imageInfo = doc.getElementsByClass("kl-blog-single-head-wrapper")
                for (j in imageInfo) {
                    val bannerImage = (j.getElementsByTag("a").attr("href"))
                    bannerImageList.add(bannerImage)
                }
            } else {
                for (j in imageInfo) {
                    val bannerImage = ("https:"+j.getElementsByTag("img").attr("src"))
                    bannerImageList.add(bannerImage)
                }
            }

            var descriptionInfo = doc.getElementsByClass("zn_pb_wrapper clearfix zn_sortable_content")
            if (descriptionInfo.toString() == "") {
                descriptionInfo = doc.getElementsByClass("itemBody kl-blog-post-body kl-blog-cols-1")
                for (j in descriptionInfo) {
                    val description1 = doc.getElementsByTag("p").text()
                    description1List.add(description1)
                }

            } else {
                for (j in descriptionInfo) {
                    val description1 = doc.getElementsByTag("p").text()
                    description1List.add(description1)
                }
            }

            val logoImage = doc.getElementsByClass("image-boxes-img img-responsive cover-fit-img").attr("src")
            val description2 = doc.getElementsByClass("eluid51b8f3f9            col-md-6 col-sm-6   znColumnElement").text()

            logoImageList.add(logoImage)
            description2List.add(description2)
            parentUrlList.add(previousUrlList[i])
        }

        confirmCount = 1

        return null
    }

    @Deprecated("Deprecated in Java")
    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)

        progressDialog.dismiss()
    }

    // Getters

    fun getBannerImage(): ArrayList<String> {
        while (confirmCount == 0)
        {
            //println("Waiting for web-scrape")
        }
        return bannerImageList
    }

    fun getLogoImage(): ArrayList<String> {
        while (confirmCount == 0)
        {
            //println("Waiting for web-scrape")
        }
        return logoImageList
    }

    fun getDescription1(): ArrayList<String> {
        while (confirmCount == 0)
        {
            //println("Waiting for web-scrape")
        }
        return description1List
    }

    fun getDescription2(): ArrayList<String> {
        while (confirmCount == 0)
        {
            //println("Waiting for web-scrape")
        }
        return description2List
    }

    fun getParentUrlList(): ArrayList<String> {
        while (confirmCount == 0)
        {
            //println("Waiting for web-scrape")
        }
        return parentUrlList
    }

}