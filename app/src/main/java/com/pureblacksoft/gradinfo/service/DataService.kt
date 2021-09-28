package com.pureblacksoft.gradinfo.service

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.JobIntentService
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.pureblacksoft.gradinfo.R
import com.pureblacksoft.gradinfo.data.Grad
import com.pureblacksoft.gradinfo.dialog.FilterDialog
import org.json.JSONException

class DataService : JobIntentService()
{
    companion object {
        private const val TAG = "DataService"

        const val URL_GRADINFO = "Gradinfo-URL"
        const val URL_IMAGE_GRAD = URL_GRADINFO + "image/grad/"
        private const val URL_DATA = URL_GRADINFO + "script/db_data.php"

        var degreeList = mutableListOf<String>()
        var yearList = mutableListOf<String>()
        var gradList = mutableListOf<Grad>()
        var filteredGradList = mutableListOf<Grad>()

        var onSuccess: (() -> Unit)? = null
        var onFailure: (() -> Unit)? = null

        fun enqueueWork(context: Context, intent: Intent) {
            enqueueWork(context, DataService::class.java, 1, intent)
        }
    }

    override fun onCreate() {
        super.onCreate()

        Log.d(TAG, "onCreate: Running")
    }

    override fun onHandleWork(intent: Intent) {
        Log.d(TAG, "onHandleWork: Running")

        if (FilterDialog.filterActive) {
            requestFilteredData()
        } else {
            requestData()
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

    private fun requestData() {
        Log.d(TAG, "requestData: Running")

        degreeList.add(getString(R.string.Filter_Degree_All))
        yearList.add(getString(R.string.Filter_Year_All))

        val dataURL = "$URL_DATA?degree_id=0&year_id=0"
        val jsonRequest = JsonObjectRequest(Request.Method.GET, dataURL, null,
            { response ->
                Log.d(TAG, "Connection successful: $dataURL")

                try {
                    //region Degree Data
                    val degreeArray = response.getJSONArray("degree_array")
                    val daLength = degreeArray.length()
                    for (i in 0 until daLength) {
                        if (isStopped) return@JsonObjectRequest

                        val degreeObject = degreeArray.getJSONObject(i)
                        degreeList.add(degreeObject.getString("degree_name"))

                        Log.d(TAG, "Degree ${i + 1}: Added")
                    }
                    //endregion

                    //region Year Data
                    val yearArray = response.getJSONArray("year_array")
                    val yaLength = yearArray.length()
                    for (i in 0 until yaLength) {
                        if (isStopped) return@JsonObjectRequest

                        val yearObject = yearArray.getJSONObject(i)
                        yearList.add(yearObject.getString("year_name"))

                        Log.d(TAG, "Year ${i + 1}: Added")
                    }
                    //endregion

                    //region Grad Data
                    gradList.clear()

                    val gradArray = response.getJSONArray("grad_array")
                    val gaLength = gradArray.length()
                    for (i in 0 until gaLength) {
                        if (isStopped) return@JsonObjectRequest

                        val gradObject = gradArray.getJSONObject(i)
                        gradList.add(
                            Grad(
                                number = gradObject.getInt("grad_number"),
                                name = gradObject.getString("grad_name"),
                                degree = gradObject.getString("degree_name"),
                                year = gradObject.getString("year_name"),
                                image = URL_IMAGE_GRAD + gradObject.getString("grad_image"),
                                profession = gradObject.getString("grad_profession"),
                                province = gradObject.getString("grad_province"),
                                email = gradObject.getString("grad_email"),
                                phone = gradObject.getString("grad_phone"),
                                note = gradObject.getString("grad_note")
                            )
                        )

                        Log.d(TAG, "Grad ${i + 1}: Added")
                    }
                    //endregion

                    onSuccess?.invoke()
                } catch (e: JSONException) {
                    Log.e(TAG, e.toString())

                    onFailure?.invoke()
                }
            },
            { error ->
                Log.d(TAG, "Connection failed: $dataURL")
                Log.e(TAG, error.toString())

                onFailure?.invoke()
            })

        Volley.newRequestQueue(this).add(jsonRequest)
    }

    private fun requestFilteredData() {
        Log.d(TAG, "requestFilteredData: Running")

        val filteredDataURL = "$URL_DATA?degree_id=${FilterDialog.currentDegreeId}&year_id=${FilterDialog.currentYearId}"
        val jsonRequest = JsonObjectRequest(Request.Method.GET, filteredDataURL, null,
            { response ->
                Log.d(TAG, "Connection successful: $filteredDataURL")

                try {
                    //region Filtered Grad Data
                    filteredGradList.clear()

                    val filteredGradArray = response.getJSONArray("grad_array")
                    val fgaLength = filteredGradArray.length()
                    for (i in 0 until fgaLength) {
                        if (isStopped) return@JsonObjectRequest

                        val filteredGradObject = filteredGradArray.getJSONObject(i)
                        filteredGradList.add(
                            Grad(
                                number = filteredGradObject.getInt("grad_number"),
                                name = filteredGradObject.getString("grad_name"),
                                degree = filteredGradObject.getString("degree_name"),
                                year = filteredGradObject.getString("year_name"),
                                image = URL_IMAGE_GRAD + filteredGradObject.getString("grad_image"),
                                profession = filteredGradObject.getString("grad_profession"),
                                province = filteredGradObject.getString("grad_province"),
                                email = filteredGradObject.getString("grad_email"),
                                phone = filteredGradObject.getString("grad_phone"),
                                note = filteredGradObject.getString("grad_note")
                            )
                        )

                        Log.d(TAG, "Filtered Grad ${i + 1}: Added")
                    }
                    //endregion

                    onSuccess?.invoke()
                } catch (e: JSONException) {
                    Log.e(TAG, e.toString())

                    onFailure?.invoke()
                }
            },
            { error ->
                Log.d(TAG, "Connection failed: $filteredDataURL")
                Log.e(TAG, error.toString())

                onFailure?.invoke()
            })

        Volley.newRequestQueue(this).add(jsonRequest)
    }
}

//PureBlack Software / Murat BIYIK