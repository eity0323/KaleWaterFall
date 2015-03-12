package com.kale.kalewaterfall.util;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.kale.kalewaterfall.application.KaleApplication;
import com.kale.kalewaterfall.mode.Club;

public class DataTask {

	private Gson mGson;

	public DataTask() {
		mGson = new Gson();
	}

	public void getJsonDataByIndex(int index) {
		
		StringRequest getStringRequest = new StringRequest(
				"http://www.duitang.com/napi/blog/list/by_club_id/?club_id=54bcb931a310829b892b1a9e&limit=0&start=" + index,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						System.out.println("response " + response);
						Club club = mGson.fromJson(response, Club.class);
						if (mListener != null) {
							mListener.onResponse(club);
						}
					}

				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						if (mListener != null) {
							mListener.onError(error.toString());
						}
					}
				});
		KaleApplication.requestQueue.add(getStringRequest);
	}
	
	
	private AnalysisListener mListener;
	
	public void setOnAnalysisListener(AnalysisListener listener) {
		mListener = listener;
	}
}
