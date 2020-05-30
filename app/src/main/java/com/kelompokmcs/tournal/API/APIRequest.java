package com.kelompokmcs.tournal.API;


import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kelompokmcs.tournal.Listener.RequestResult;
import com.kelompokmcs.tournal.Model.Group;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class APIRequest {
    public RequestResult requestResult;

    public APIRequest(RequestResult requestResult){
        this.requestResult = requestResult;
    }

    public JsonObjectRequest addNewUser(String userId, String userName, String userEmail, String userPhoto) throws JSONException {
        JSONObject params = new JSONObject();
        params.put("user_id",userId);
        params.put("user_name",userName);
        params.put("user_email",userEmail);
        params.put("user_photo",userPhoto);

        return new JsonObjectRequest(Request.Method.POST,
                "http://ec2-100-25-163-107.compute-1.amazonaws.com:3000/api/addNewUser",
                params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        requestResult.notifySuccess("addNewUser",response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        requestResult.notifyError("addNewUser",error);
                    }
                });
    }

    public JsonObjectRequest createNewGroupRequest(String userId, String groupName, String groupLocation, String startDate, String endDate, final String groupPass) throws JSONException {
        JSONObject params = new JSONObject();
        params.put("user_id",userId);
        params.put("group_name",groupName);
        params.put("group_location",groupLocation);
        params.put("start_date",startDate);
        params.put("end_date",endDate);
        params.put("group_pass",groupPass);

        return new JsonObjectRequest(Request.Method.POST,
                "http://ec2-100-25-163-107.compute-1.amazonaws.com:3000/api/createNewGroup",
                params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        requestResult.notifySuccess("createNewGroup",response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        requestResult.notifyError("createNewGroup", error);
                    }
        });
    }

    public JsonObjectRequest verifyGroupCodeRequest(String groupCode) throws JSONException {
        JSONObject params = new JSONObject();
        params.put("group_code",groupCode);

        return new JsonObjectRequest(Request.Method.POST, "http://ec2-100-25-163-107.compute-1.amazonaws.com:3000/api/verifyGroupCode", params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                requestResult.notifySuccess("verifyGroupCode",response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                requestResult.notifyError("verifyGroupCode",error);
            }
        });
    }

    public JsonObjectRequest verifyGroupPasswordRequest(String userId, int groupId, String password) throws JSONException {
        JSONObject params = new JSONObject();
        params.put("user_id",userId);
        params.put("group_id",groupId);
        params.put("group_pass",password);

        return new JsonObjectRequest(Request.Method.POST,
                "http://ec2-100-25-163-107.compute-1.amazonaws.com:3000/api/verifyGroupPassword",
                params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        requestResult.notifySuccess("verifyGroupPassword",response);
                    }
                },
                new Response.ErrorListener() {
                @Override
                    public void onErrorResponse(VolleyError error) {
                        requestResult.notifyError("verifyGroupPassword",error);
                    }
        });
    }

    public JsonObjectRequest getGroupInformation(int groupId) throws JSONException {
        JSONObject params = new JSONObject();
        params.put("group_id",groupId);

        return new JsonObjectRequest(Request.Method.POST,
                "http://ec2-100-25-163-107.compute-1.amazonaws.com:3000/api/getGroupInformation",
                params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        requestResult.notifySuccess("getGroupInformation",response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        requestResult.notifyError("getGroupInformation",error);
                    }
                });
    }

    public JsonObjectRequest getAnnouncementInformation(int groupId) throws JSONException {
        JSONObject params = new JSONObject();
        params.put("group_id",groupId);

        return new JsonObjectRequest(Request.Method.POST,
                "http://ec2-100-25-163-107.compute-1.amazonaws.com:3000/api/getAnnouncementInformation",
                params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        requestResult.notifySuccess("getAnnouncementInformation",response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        requestResult.notifyError("getAnnouncementInformation",error);
                    }
        });
    }

    public JsonObjectRequest getAgendaInformation(int groupId) throws JSONException {
        JSONObject params = new JSONObject();
        params.put("group_id",groupId);

        return new JsonObjectRequest(Request.Method.POST,
                "http://ec2-100-25-163-107.compute-1.amazonaws.com:3000/api/getAgendaInformation",
                params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        requestResult.notifySuccess("getAgendaInformation",response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        requestResult.notifyError("getAgendaInformation",error);
                    }
        });
    }
}
