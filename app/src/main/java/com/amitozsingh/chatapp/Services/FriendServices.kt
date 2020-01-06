package com.amitozsingh.chatapp.Services

import android.content.Context
import android.util.Log
import android.widget.EditText
import com.amitozsingh.chatapp.Activities.BaseActivity
import com.amitozsingh.chatapp.Activities.MessagesActivity
import com.google.firebase.database.DatabaseError

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import com.amitozsingh.chatapp.FindFriendsAdapter
import com.amitozsingh.chatapp.Fragments.SearchFriendsFragment
import com.amitozsingh.chatapp.utils.User
import io.socket.client.Socket
import org.json.JSONException
import org.json.JSONObject
import rx.Observable
import rx.Observer
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Func1
import rx.schedulers.Schedulers
import android.text.Editable
import android.text.TextWatcher




class FriendServices {

    public var myFriendServices: FriendServices? = null
    var messagesActivity = MessagesActivity()
    var baseActivity: BaseActivity? = null

    private val SERVER_SUCCESS = 6
    private val SERVER_FAILURE = 7
    fun getInstance(): FriendServices {
        if (myFriendServices == null) {
            myFriendServices = FriendServices()
        }
        return myFriendServices as FriendServices
    }

    fun getFriendRequestsSent(
        adapter: FindFriendsAdapter,fragment:SearchFriendsFragment
    ): ValueEventListener {
        val userHashMap=HashMap<String,User>()
       // var userHashMap: HashMap<String, User>? = null

        return object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                userHashMap.clear()
                for (snapshot in dataSnapshot.children) {
                    val user = snapshot.getValue(User::class.java)
                    userHashMap.put(user?.email!!, user!!)
                }

                adapter.setmFriendRequestSentMap(userHashMap)
                fragment.setmFriendRequestsSentMap(userHashMap)
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
    }

    fun getFriendRequestsRecieved(
        adapter: FindFriendsAdapter
    ): ValueEventListener {
        val userHashMap=HashMap<String,User>()
        // var userHashMap: HashMap<String, User>? = null

        return object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                userHashMap.clear()
                for (snapshot in dataSnapshot.children) {
                    val user = snapshot.getValue(User::class.java)
                    userHashMap.put(user?.email!!, user!!)
                }

                adapter.setmFriendRequestRecievedMap(userHashMap)

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
    }


    fun sendorremoverequests(
       socket: Socket,
       useremail:String,friendEmail:String,requestcode:String
    ): Subscription? {



        val userDetails = arrayListOf<String>()
        userDetails.add(useremail)
        userDetails.add(friendEmail)
        userDetails.add(requestcode)

        val listObservable = Observable.just(userDetails)

        return listObservable
            .subscribeOn(Schedulers.io())
            .map(object : Func1<List<String>, Int> {


                override fun call(strings: List<String>): Int? {
                    val userEmail = strings[0]
                    val email = strings[1]
                    val requestCode = strings[2]


                        val sendData = JSONObject()
                        try {
                            sendData.put("userEmail", userEmail)
                            sendData.put("email", email)
                            sendData.put("requestCode", requestCode)
                            socket.emit("friendRequest", sendData)
                            return SERVER_SUCCESS
                        } catch (e: JSONException) {
//                            Log.i(
//                                AccountServices::class.java!!.getSimpleName(),
//                                e.message
//                            )
                            return SERVER_FAILURE
                        }

                    }

            })
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .subscribe(object : Observer<Int> {
                override fun onCompleted() {

                }


                override fun onNext(t: Int) {

//                    if (t == EMPTY_EMAIL) {
//                        userEmailEt.error = "Email  Can't Be Empty"
//                    } else if (t == EMAIL_BAD_FORMAT) {
//                        userEmailEt.error = "check your email format"
//                    }
                }




                override fun onError(e: Throwable) {

                }


            })
    }

    fun getMatchingUsers(users: List<User>): List<User> {



        return users
    }



}
