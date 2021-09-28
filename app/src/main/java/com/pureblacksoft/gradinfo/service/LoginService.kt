package com.pureblacksoft.gradinfo.service

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.JobIntentService
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.pureblacksoft.gradinfo.data.Grad
import org.json.JSONException

class LoginService : JobIntentService() {
    companion object {
        private const val TAG = "LoginService"

        private const val URL_LOGIN = DataService.URL_GRADINFO + "script/db_login.php"

        var userGrad: Grad? = null
        var wrongParam = false

        var onSuccess: (() -> Unit)? = null
        var onFailure: (() -> Unit)? = null

        fun enqueueWork(context: Context, intent: Intent) {
            enqueueWork(context, LoginService::class.java, 1, intent)
        }
    }

    override fun onCreate() {
        super.onCreate()

        Log.d(TAG, "onCreate: Running")
    }

    override fun onHandleWork(intent: Intent) {
        Log.d(TAG, "onHandleWork: Running")

        val userNumber = intent.getStringExtra("user_number")
        val userPassword = intent.getStringExtra("user_password")

        if (userNumber != null && userPassword != null) {
            wrongParam = false

            requestLogin(userNumber, userPassword)
        } else {
            wrongParam = true

            onFailure?.invoke()
        }
    }

    override fun onStopCurrentWork(): Boolean {
        Log.d(TAG, "onStopCurrentWork: Running")

        return super.onStopCurrentWork()
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.d(TAG, "onDestroy: Running")
    }

    private fun requestLogin(userNumber: String, userPassword: String) {
        Log.d(TAG, "requestLogin: Running")

        val loginURL = "$URL_LOGIN?user_number=$userNumber&user_password='$userPassword'"
        val jsonRequest = JsonObjectRequest(Request.Method.GET, loginURL, null,
            { response ->
                Log.d(TAG, "Connection successful: $loginURL")

                try {
                    val loginStatus = response.getString("login_status").toInt()
                    if (loginStatus == 1) {
                        val userGradObject = response.getJSONObject("user_grad")

                        userGrad = Grad(
                            number = userGradObject.getInt("grad_number"),
                            name = userGradObject.getString("grad_name"),
                            degree = userGradObject.getString("degree_name"),
                            year = userGradObject.getString("year_name"),
                            image = DataService.URL_IMAGE_GRAD + userGradObject.getString("grad_image"),
                            profession = userGradObject.getString("grad_profession"),
                            province = userGradObject.getString("grad_province"),
                            email = userGradObject.getString("grad_email"),
                            phone = userGradObject.getString("grad_phone"),
                            note = userGradObject.getString("grad_note")
                        )

                        Log.d(TAG, "User Grad Added")

                        onSuccess?.invoke()
                    } else {
                        wrongParam = true

                        onFailure?.invoke()
                    }
                } catch (e: JSONException) {
                    Log.e(TAG, e.toString())

                    onFailure?.invoke()
                }
            },
            { error ->
                Log.d(TAG, "Connection failed: $loginURL")
                Log.e(TAG, error.toString())

                onFailure?.invoke()
            })

        Volley.newRequestQueue(this).add(jsonRequest)
    }
}

//PureBlack Software / Murat BIYIK