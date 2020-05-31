package kotlindays31.project.healthfeed.Feed

import android.os.Build
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlindays31.project.healthfeed.R

class FeedListAdapter : RecyclerView.Adapter<ViewHolder>(){

    var data = listOf<FeedItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.feed_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.feedItemTitle.text = item.title

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.feedItemDescription.text= Html.fromHtml(item.description, Html.FROM_HTML_MODE_COMPACT)
        } else {
            @Suppress("DEPRECATION")
            holder.feedItemDescription.text= Html.fromHtml(item.description)
        }

        holder.feedItemLink.movementMethod = LinkMovementMethod.getInstance()
        holder.feedItemLink.text= item.link

    }
}

class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    val feedItemTitle: TextView = itemView.findViewById(R.id.titleText)
    val feedItemDescription: TextView = itemView.findViewById(R.id.descriptionText)
    val feedItemLink: TextView = itemView.findViewById(R.id.linkText)
}