package com.genieapps.writeshort.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.Toast
import com.genieapps.writeshort.R
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import java.util.*


class Splash: AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {

    private var mGoogleApiClient:GoogleApiClient? = null
    private var btnSignIn:Button? = null
    private val RC_SIGN_IN = 9001
    private var mAuth:FirebaseAuth? = null
    private val bgColors = arrayOf("#52B3D9", "#E26A6A", "#87D37C")
    private lateinit var rootView:ConstraintLayout

    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        initGoogleServices()
        initUI()
        setActionListeners()

        /*val handler = Handler();
        handler.postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 2000)*/
    }

    private fun setActionListeners() {
        btnSignIn!!.setOnClickListener({
            if (mAuth!!.currentUser != null)
                signOut()
            else
                signIn()
        })


    }

    private fun initUI() {
        btnSignIn = findViewById(R.id.btnSignIn) as Button
        rootView = findViewById(R.id.rootView) as ConstraintLayout
        rootView.setBackgroundColor(Color.parseColor(bgColors[Random().nextInt(bgColors.size)]))
    }

    private fun initGoogleServices() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        mGoogleApiClient = GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()

        mAuth = FirebaseAuth.getInstance()
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = mAuth?.currentUser
        updateUI(true, currentUser)
    }

    override fun onResume() {
        super.onResume()
        hideProgressDialog()
    }

    public override fun onActivityResult(requestCode:Int, resultCode:Int, data:Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            handleSignInResult(result)
        }
    }

    private fun handleSignInResult(result:GoogleSignInResult) {
        if (result.isSuccess) {
            val acct = result.signInAccount
            if (acct != null) {
                firebaseAuthWithGoogle(acct)
            }
        } else {
            updateUI(false, null)
        }
    }

    private fun firebaseAuthWithGoogle(acct:GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth?.signInWithCredential(credential)?.addOnCompleteListener(this, { task ->
            if (task.isSuccessful) {
                val user = mAuth!!.currentUser
                updateUI(true, user)
            } else {
                Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
                updateUI(false, null)
            }
        })
    }

    private fun signIn() {
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun signOut() {
        FirebaseAuth.getInstance().signOut();
        updateUI(false, null)
    }

    private fun revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback {
            // [START_EXCLUDE]
            updateUI(false, null)
            // [END_EXCLUDE]
        }
    }

    override fun onConnectionFailed(connectionResult:ConnectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        //Log.d(FragmentActivity.TAG, "onConnectionFailed:" + connectionResult)
    }

    override fun onStop() {
        super.onStop()
        /* if (mProgressDialog != null) {
             mProgressDialog.dismiss()
         }*/
    }

    private fun showProgressDialog() {
        /* if (mProgressDialog == null) {
             mProgressDialog = ProgressDialog(this)
             mProgressDialog.setMessage(getString(R.string.loading))
             mProgressDialog.setIndeterminate(true)
         }

         mProgressDialog.show()*/
    }

    private fun hideProgressDialog() {
        /*if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide()
        }*/
    }

    private fun updateUI(signedIn:Boolean, acct:FirebaseUser?) {
        if (signedIn) {
            if (acct != null) {
                btnSignIn!!.text = "Welcome, " + acct.displayName
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        } else {
            //  mStatusTextView.setText(R.string.signed_out)
            btnSignIn!!.text = "SignIn"
            // findViewById(R.id.sign_in_button).visibility = View.VISIBLE
            // findViewById(R.id.sign_out_and_disconnect).visibility = View.GONE
        }
    }
}
