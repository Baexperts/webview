package com.mohsen.webview

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase Auth and Firestore
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Configure Google Sign-In options
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("974602398374-nqkg4r28pdhefqocuk03r5kvgea7ltdf.apps.googleusercontent.com")
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Trigger Sign-In
        signIn()
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        handleSignInResult(task)
    }

    private fun handleSignInResult(task: com.google.android.gms.tasks.Task<GoogleSignInAccount>) {
        try {
            val account = task.getResult(ApiException::class.java)
            Log.d("GoogleSignIn", "Sign-in successful!")
            Log.d("GoogleSignIn", "Display Name: ${account.displayName}")
            Log.d("GoogleSignIn", "Email: ${account.email}")
            Log.d("GoogleSignIn", "ID Token: ${account.idToken}")

            // Authenticate with Firebase using Google account
            account.idToken?.let {
                firebaseAuthWithGoogle(it)
            }

        } catch (e: ApiException) {
            Log.e("GoogleSignIn", "Sign-in failed with code: ${e.statusCode}", e)
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("FirebaseAuth", "signInWithCredential:success")
                    val user = firebaseAuth.currentUser
                    Log.d("FirebaseAuth", "User: ${user?.displayName}")

                    // Save user info to Firestore
                    saveUserToFirestore(user)
                } else {
                    Log.w("FirebaseAuth", "signInWithCredential:failure", task.exception)
                }
            }
    }

    private fun saveUserToFirestore(user: FirebaseUser?) {
        user?.let {
            val userInfo = hashMapOf(
                "name" to it.displayName,
                "email" to it.email
            )

            firestore.collection("users")
                .document(it.uid)
                .set(userInfo)
                .addOnSuccessListener {
                    Log.d("Firestore", "User info saved successfully!")
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "Error saving user info", e)
                }
        }
    }
}
