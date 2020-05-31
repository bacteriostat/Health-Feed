package kotlindays31.project.healthfeed.Feed

import android.content.Context
import android.util.Log
import android.util.Xml
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.xmlpull.v1.XmlPullParser


class FeedViewModel : ViewModel() {

    var cache: Boolean = false
    private var _feedList = MutableLiveData<List<FeedItem>>()
    val feedList: LiveData<List<FeedItem>>
        get() = _feedList
    private val url = "https://www.who.int/rss-feeds/news-english.xml";

    fun getFeed(context: Context?){
        val queue = Volley.newRequestQueue(context)
        Log.i("fetch", "Loading from backend..")
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            Response.Listener { response ->

                parseFeed(response)

            },
            Response.ErrorListener {

                Log.e("fetch", it.toString())

            }
        )
        queue.add(stringRequest)
    }

    private fun parseFeed(response: String){
        val parsedData = mutableListOf<FeedItem>()
        try{
            var isItem = false
            var title: String? = null
            var link: String? = null
            var description: String? = null

            val xmlPullParser = Xml.newPullParser()
            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xmlPullParser.setInput(response.substring(3).byteInputStream(), null)
            xmlPullParser.nextTag()
            while (xmlPullParser.next() != XmlPullParser.END_DOCUMENT) {
                val eventType = xmlPullParser.eventType
                val name = xmlPullParser.name ?: continue
                if (eventType == XmlPullParser.END_TAG) {
                    if (name.equals("item", ignoreCase = true)) {
                        isItem = false
                    }
                    continue
                }
                if (eventType == XmlPullParser.START_TAG) {
                    if (name.equals("item", ignoreCase = true)) {
                        isItem = true
                        continue
                    }
                }

                var result = ""

                if (xmlPullParser.next() == XmlPullParser.TEXT) {
                    result = xmlPullParser.text
                    xmlPullParser.nextTag()
                }
                if (name.equals("title", ignoreCase = true)) {
                    title = result
                } else if (name.equals("link", ignoreCase = true)) {
                    link = result
                } else if (name.equals("description", ignoreCase = true)) {
                    description = result
                }

                if( title != null && link != null && description != null ){

                    if(isItem){
                        parsedData.add(FeedItem(title, description, link))
                        _feedList.value = parsedData
                    }

                    title = null
                    link = null
                    description = null
                    isItem = false
                }
            }
        } finally {
            cache = true
        }

    }

}