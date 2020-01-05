package com.amitozsingh.chatapp

import androidx.recyclerview.widget.RecyclerView
import android.R
import android.content.Context
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import com.amitozsingh.chatapp.Activities.BaseActivity
import com.amitozsingh.chatapp.Activities.MessagesActivity
import com.amitozsingh.chatapp.utils.User
import com.amitozsingh.chatapp.utils.isIncludedInMap
import kotlinx.android.synthetic.main.user_list.view.*


class FindFriendsAdapter(
    private val mActivity: MessagesActivity,
    private val mListener: UserListener
) : RecyclerView.Adapter<FindFriendsAdapter.ViewHolder>() {


    private val mUsers: ArrayList<User>
    private val mInflater: LayoutInflater

    private var mFriendRequestSentMap: HashMap<String, User>? = null


    init {
        mInflater = mActivity.layoutInflater
        mUsers = ArrayList()
        mFriendRequestSentMap = HashMap()

    }


    fun setmUsers(users: List<User>) {
        mUsers.clear()
        mUsers.addAll(users)
        notifyDataSetChanged()
    }
    fun setmFriendRequestSentMap(friendRequestSentMap: HashMap<String, User>) {
        mFriendRequestSentMap!!.clear()
        mFriendRequestSentMap!!.putAll(friendRequestSentMap)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var li=parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val userView=li.inflate(com.amitozsingh.chatapp.R.layout.user_list,parent,false)
return  ViewHolder(userView)
      //  val findFriendsViewHolder = FindFriendsViewHolder(userView)
//        findFriendsViewHolder.setOnClickListener(object : View.OnClickListener {
//            override fun onClick(view: View) {
//                val user = findFriendsViewHolder.itemView.tag as User
//                mListener.OnUserClicked(user)
//            }
//        })
     //   return findFriendsViewHolder
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.populate(
//            mActivity,
//            mUsers[position]
//        )
        holder.itemView.reqsentbutton.setOnClickListener {
            var user=mUsers[position]
            mListener.OnUserClicked(user)
        }
        holder.bindItems(mUsers[position],mFriendRequestSentMap)
    }
    class ViewHolder(itemview:View):RecyclerView.ViewHolder(itemview){
        fun bindItems(user: User,friendRequestSentMap:HashMap<String,User>?){
            itemView.username.text=user.userName
            if (isIncludedInMap(friendRequestSentMap,user)){
                //senttv.setVisibility(View.VISIBLE);
                itemView.senttv.text="Friend Request Sent"
                itemView.reqsentbutton.setImageResource(R.drawable.btn_minus);
                //mAddFriend.setVisibility(View.VISIBLE);
            }
            else{
                itemView.senttv.setText("Add Friend")
                itemView.reqsentbutton.setImageResource(R.drawable.btn_plus)
            }
        }
    }

    override fun getItemCount(): Int {
        return mUsers.size
    }

    interface UserListener {
        fun OnUserClicked(user: User)
    }
}