package com.google.firebase.codelab.friendlychat

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.campus.ui.screens.Home
import com.example.campus.ui.screens.Settings
import com.example.campus.ui.theme.CampusTheme
import com.example.campus.ui.viewmodels.ChatVM
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.codelab.friendlychat.model.Message
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.codelab.friendlychat.ui.screens.Chat

import com.google.firebase.codelab.friendlychat.data.sensors.GpsManager
import com.google.firebase.codelab.friendlychat.model.User
import com.google.firebase.codelab.friendlychat.ui.viewmodels.LocationVM

class MainActivity : AppCompatActivity() {

    // Firebase instance variables
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase

    private lateinit var gpsManager: GpsManager

    private val openDocument = registerForActivityResult(MyOpenDocumentContract()) { uri ->
        uri?.let { onImageSelected(it) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        // Initialize Firebase Auth and check if the user is signed in
        auth = Firebase.auth
        if (auth.currentUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
            return
        }

        // Initialize Realtime Database
        db = Firebase.database
        val messagesRef = db.reference.child(MESSAGES_CHILD)
        val usersRef = db.reference.child("users")
        val roomsRef = db.reference.child("rooms")

        super.onCreate(savedInstanceState)
        val locationVM = LocationVM(application,this,db,messagesRef,auth,usersRef,roomsRef)
        locationVM.updateLocation()
        setContent {
            val navController = rememberNavController()
            val vm: ChatVM = viewModel {
                ChatVM(db, messagesRef, auth, usersRef)
            }
            val darkMode by vm.darkMode.collectAsState()

            /*updater location varje 60 seconds max 10 users just nu antal writes
             blir max 1440 per dag och gränsen är 20K per dag*/
            locationVM.updateUserOnInterval(60000)
            CampusTheme(darkTheme = darkMode){
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavigationGraph(navController = navController, vm = vm, locationVM = locationVM )
                }
            }
        }



        if(!userIsInDatabase(auth.currentUser!!)){
            Log.d("MarcusTagUser",auth.currentUser!!.displayName.toString()+" added to database")
            addUserToDatabase(auth.currentUser!!)
            if(auth.currentUser!!.photoUrl == null||auth.currentUser!!.photoUrl.toString() == ""||auth.currentUser!!.photoUrl.toString() == "null"){
                if(auth.currentUser!!.displayName!=null){
                    usersRef.child("photoUrl").setValue(auth.currentUser?.displayName)
                }
            }
        }else{
            Log.d("MarcusTagUser","User already in database")
        }
    }



    public override fun onStart() {
        super.onStart()
        // Check if user is signed in.
        if (auth.currentUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
            return
        }
    }

    public override fun onPause() {
//        adapter.stopListening()
        super.onPause()
    }

    public override fun onResume() {
        super.onResume()
//        adapter.startListening()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.sign_out_menu -> {
                signOut()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun onImageSelected(uri: Uri) {
        Log.d(TAG, "Uri: $uri")
        val user = auth.currentUser
        val tempMessage = Message(null, getUserName(), getPhotoUrl(), LOADING_IMAGE_URL)
        db.reference
                .child(MESSAGES_CHILD)
                .push()
                .setValue(
                        tempMessage,
                        DatabaseReference.CompletionListener { databaseError, databaseReference ->
                            if (databaseError != null) {
                                Log.w(
                                        TAG, "Unable to write message to database.",
                                        databaseError.toException()
                                )
                                return@CompletionListener
                            }

                            // Build a StorageReference and then upload the file
                            val key = databaseReference.key
                            val storageReference = Firebase.storage
                                    .getReference(user!!.uid)
                                    .child(key!!)
                                    .child(uri.lastPathSegment!!)
                            putImageInStorage(storageReference, uri, key)
                        })
    }

    private fun putImageInStorage(storageReference: StorageReference, uri: Uri, key: String?) {
        // First upload the image to Cloud Storage
        storageReference.putFile(uri)
            .addOnSuccessListener(
                this
            ) { taskSnapshot -> // After the image loads, get a public downloadUrl for the image
                // and add it to the message.
                taskSnapshot.metadata!!.reference!!.downloadUrl
                    .addOnSuccessListener { uri ->
                        val message =
                            Message(null, getUserName(), getPhotoUrl(), uri.toString())
                        db.reference
                            .child(MESSAGES_CHILD)
                            .child(key!!)
                            .setValue(message)
                    }
            }
            .addOnFailureListener(this) { e ->
                Log.w(
                    TAG,
                    "Image upload task was unsuccessful.",
                    e
                )
            }
    }

     fun signOut() {
        AuthUI.getInstance().signOut(this)
        startActivity(Intent(this, SignInActivity::class.java))
        finish()
    }

    private fun getPhotoUrl(): String? {
        val user = auth.currentUser
        return user?.photoUrl?.toString()
    }

    private fun getUserName(): String? {
        val user = auth.currentUser
        return if (user != null) {
            user.displayName
        } else ANONYMOUS
    }

    companion object {
        private const val TAG = "MainActivity"
        const val MESSAGES_CHILD = "messages"
        const val ANONYMOUS = "anonymous"
        private const val LOADING_IMAGE_URL = "https://www.google.com/images/spin-32.gif"
    }

    fun userIsInDatabase(user: FirebaseUser):Boolean{
        val usersRef = db.reference.child("users")
        val userRef = usersRef.child(user.uid)
        var userExists = false
        userRef.get().addOnSuccessListener {
            userExists = it.exists()
        }
        return userExists
    }

    fun addUserToDatabase(user: FirebaseUser){
        val usersRef = db.reference.child("users")
        val userRef = usersRef.child(user.uid)
        val userObject = User(user.uid, user.displayName, user.photoUrl.toString(), user.email)
        userRef.setValue(userObject)
    }
    public fun oldViewBining(db: FirebaseDatabase, messagesRef: DatabaseReference){
//        // The FirebaseRecyclerAdapter class and options come from the FirebaseUI library
//        // See: https://github.com/firebase/FirebaseUI-Android
//        val options = FirebaseRecyclerOptions.Builder<FriendlyMessage>()
//            .setQuery(messagesRef, FriendlyMessage::class.java)
//            .build()
//        adapter = FriendlyMessageAdapter(options, getUserName())
//        binding.progressBar.visibility = ProgressBar.INVISIBLE
//        manager = LinearLayoutManager(this)
//        manager.stackFromEnd = true
//        binding.messageRecyclerView.layoutManager = manager
//        binding.messageRecyclerView.adapter = adapter
//
//        // Scroll down when a new message arrives
//        // See MyScrollToBottomObserver for details
//        adapter.registerAdapterDataObserver(
//            MyScrollToBottomObserver(binding.messageRecyclerView, adapter, manager)
//        )
//
//        // Disable the send button when there's no text in the input field
//        // See MyButtonObserver for details
//        binding.messageEditText.addTextChangedListener(MyButtonObserver(binding.sendButton))
//
//        // When the send button is clicked, send a text message
//        binding.sendButton.setOnClickListener {
//            val friendlyMessage = FriendlyMessage(
//                binding.messageEditText.text.toString(),
//                getUserName(),
//                getPhotoUrl(),
//                null
//            )
//            db.reference.child(MESSAGES_CHILD).push().setValue(friendlyMessage)
//            binding.messageEditText.setText("")
//        }
//
//        // When the image button is clicked, launch the image picker
//        binding.addMessageImageView.setOnClickListener {
//            openDocument.launch(arrayOf("image/*"))
//        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationGraph(navController: NavHostController, vm: ChatVM, locationVM: LocationVM) {
    NavHost(navController, startDestination = "home") {
        composable("home") {
            Home(vm = vm, navController = navController, locationVM = locationVM)
        }
        composable("chat") {
            Chat(vm = vm, navController = navController, locationVM = locationVM)
        }
        composable("settings") {
            Settings(vm = vm,navController = navController, locationVM = locationVM)
        }
    }
}