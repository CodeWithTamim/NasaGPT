package com.nasahacker.nasagpt.adapter

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.nasahacker.nasagpt.R
import com.nasahacker.nasagpt.databinding.ChatItemBinding
import com.nasahacker.nasagpt.model.Chat
import io.noties.markwon.Markwon
import io.noties.markwon.syntax.SyntaxHighlight

/**
 * CodeWithTamim
 *
 * @developer Tamim Hossain
 * @mail tamimh.dev@gmail.com
 */
class ChatAdapter(private val context: Context, private var chatList: List<Chat>) :
    Adapter<ChatAdapter.ChatViewHolder>()
{

    inner class ChatViewHolder(itemView: View) : ViewHolder(itemView)
    {
        val binding: ChatItemBinding = ChatItemBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder
    {
        return ChatViewHolder(
            LayoutInflater.from(context).inflate(R.layout.chat_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int)
    {
        val currentChat = chatList[position]
        val linearLayout = holder.binding.rootLayout
        if (currentChat.isChatBot)
        {
            holder.binding.imgIconChat.setImageResource(R.drawable.robot)
            holder.binding.txtName.text = context.getString(R.string.app_name)
            linearLayout.gravity = GravityCompat.START
            holder.binding.txtContent.setTextIsSelectable(true)
            holder.binding.txtContent.highlightColor = context.resources.getColor(R.color.highlight_color)
        } else
        {
            holder.binding.imgIconChat.setImageResource(R.drawable.person)
            holder.binding.txtName.text = context.getString(R.string.label_you)
            linearLayout.gravity = GravityCompat.END
            holder.binding.btnCopy.visibility = View.GONE
        }
        val markwon = Markwon.create(context)
        markwon.setMarkdown(holder.binding.txtContent,currentChat.chat)
        val clipboardManager =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("response", currentChat.chat)
        //handle copy
        holder.binding.btnCopy.setOnClickListener {
            clipboardManager.setPrimaryClip(clipData)
            Toast.makeText(context, "Copied to clipboard.", Toast.LENGTH_SHORT).show()
        }
        holder.binding.txtTimestamp.text = currentChat.timestamp


    }

    override fun getItemCount(): Int = chatList.size
    fun updateChats(chats: List<Chat>)
    {
        this.chatList = chats
        notifyDataSetChanged()
    }


}