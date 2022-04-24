package com.example.mygreenapp

import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask
import org.jsoup.Jsoup

class CnSScrapeFirst(val context: Context) : AsyncTask<Void, Void, String>() {

    // Initialize arrays for storing information from the website
    private var titleList = ArrayList<String>()
    private var imageList = ArrayList<String>()
    private var urlList = ArrayList<String>()
    private var confirmCount = 0
    //progressDialog
    private lateinit var progressDialog: ProgressDialog

    override fun onPreExecute() {
        super.onPreExecute()

        //configure progress dialog
        progressDialog = ProgressDialog(context)
        progressDialog.setTitle("Please Wait")
        progressDialog.setMessage("Retrieving Clubs and Societies first page data...")
        progressDialog.setCanceledOnTouchOutside(false)

        //show progress
        progressDialog.show()
    }

    override fun doInBackground(vararg params: Void?): String? {
        var doc = Jsoup.connect("https://www.nsbm.ac.lk/life-at-nsbm/clubs-societies/").get()
        var allInfo = doc.getElementsByClass("club-item-wrapper")
        //println(allInfo)

        // This loop will look for all the elements in the variable allInfo
        for (i in allInfo) {
            var webTitle = i.getElementsByClass("club-title").text()
            var webImage = i.getElementsByTag("img").attr("src")
            var webUrl = i.getElementsByTag("a").attr("href")
            // Add the scraped data into the arrays
            titleList.add(webTitle)
            imageList.add(webImage)
            urlList.add(webUrl)
        }

        // println(urlList)

        confirmCount = 1

        // println("The size of title list in webscrape class is "+titleList.size)

        return null
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)

        progressDialog.dismiss()
    }

    // Getters

    fun getTitleList(): ArrayList<String> {
        while (confirmCount == 0)
        {
            //println("Waiting for webscrape")
        }
        return titleList
    }

    fun getImageList(): ArrayList<String> {
        while (confirmCount == 0)
        {
            //println("Waiting for webscrape")
        }
        return imageList
    }

    fun getUrlList(): ArrayList<String> {
        while (confirmCount == 0)
        {
            //println("Waiting for webscrape")
        }
        return urlList
    }

}