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
import com.kelompokmcs.tournal.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class APIRequest {
    public RequestResult requestResult;
    public String domain;

    public APIRequest(RequestResult requestResult){
        this.requestResult = requestResult;
        domain = "http://ec2-100-25-163-107.compute-1.amazonaws.com:3000/";
    }

    public JsonObjectRequest addNewUser(String userId, String userName, String userEmail, String userPhoto) throws JSONException {
        String endpoint = "api/addNewUser";

        JSONObject params = new JSONObject();
        params.put("user_id",userId);
        params.put("user_name",userName);
        params.put("user_email",userEmail);
        params.put("user_photo",userPhoto);

        return new JsonObjectRequest(Request.Method.POST,
                domain+endpoint,
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
        String endpoint = "api/createNewGroup";

        JSONObject params = new JSONObject();
        params.put("user_id",userId);
        params.put("group_name",groupName);
        params.put("group_location",groupLocation);
        params.put("start_date",startDate);
        params.put("end_date",endDate);
        params.put("group_pass",groupPass);

        return new JsonObjectRequest(Request.Method.POST,
                domain+endpoint,
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
        String endpoint = "api/verifyGroupCode";

        JSONObject params = new JSONObject();
        params.put("group_code",groupCode);

        return new JsonObjectRequest(Request.Method.POST, domain+endpoint, params, new Response.Listener<JSONObject>() {
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
        String endpoint = "api/verifyGroupPassword";

        JSONObject params = new JSONObject();
        params.put("user_id",userId);
        params.put("group_id",groupId);
        params.put("group_pass",password);

        return new JsonObjectRequest(Request.Method.POST,
                domain+endpoint,
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
        String endpoint = "api/getGroupInformation";

        JSONObject params = new JSONObject();
        params.put("group_id",groupId);

        return new JsonObjectRequest(Request.Method.POST,
                domain+endpoint,
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

    public JsonObjectRequest getGroupInformationForUser(String userId) throws JSONException {
        String endpoint = "api/getGroupInformationForUser";

        JSONObject params = new JSONObject();
        params.put("user_id",userId);

        return new JsonObjectRequest(Request.Method.POST,
                domain+endpoint,
                params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        requestResult.notifySuccess("getGroupInformationForUser",response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        requestResult.notifyError("getGroupInformationForUser",error);
                    }
                });
    }

    public JsonObjectRequest getAnnouncementInformationForUser(String userId) throws JSONException {
        String endpoint = "api/getAnnouncementInformationForUser";

        JSONObject params = new JSONObject();
        params.put("user_id",userId);

        return new JsonObjectRequest(Request.Method.POST,
                domain+endpoint,
                params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        requestResult.notifySuccess("getAnnouncementInformationForUser",response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        requestResult.notifyError("getAnnouncementInformationForUser",error);
                    }
                });
    }

    public JsonObjectRequest getAgendaInformationForUser(String userId) throws JSONException {
        String endpoint = "api/getAgendaInformationForUser";

        JSONObject params = new JSONObject();
        params.put("user_id",userId);

        return new JsonObjectRequest(Request.Method.POST,
                domain+endpoint,
                params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        requestResult.notifySuccess("getAgendaInformationForUser",response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        requestResult.notifyError("getAgendaInformationForUser",error);
                    }
                });
    }

    public JsonObjectRequest getAnnouncementInformation(int groupId) throws JSONException {
        String endpoint = "api/getAnnouncementInformation";

        JSONObject params = new JSONObject();
        params.put("group_id",groupId);

        return new JsonObjectRequest(Request.Method.POST,
                domain+endpoint,
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
        String endpoint = "api/getAgendaInformation";

        JSONObject params = new JSONObject();
        params.put("group_id",groupId);

        return new JsonObjectRequest(Request.Method.POST,
                domain+endpoint,
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

    public JsonObjectRequest getSymbolInformation(int groupId) throws JSONException {
        String endpoint = "api/getSymbolInformation";

        JSONObject params = new JSONObject();
        params.put("group_id",groupId);

        return new JsonObjectRequest(Request.Method.POST,
                domain+endpoint,
                params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        requestResult.notifySuccess("getSymbolInformation",response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        requestResult.notifyError("getSymbolInformation",error);
                    }
                });
    }

    public JsonObjectRequest addAnnouncement(int groupId, String announcementTitle, String announcementDesc, String userId) throws JSONException {
        String endpoint = "api/addAnnouncement";

        JSONObject params = new JSONObject();
        params.put("group_id",groupId);
        params.put("announcement_title",announcementTitle);
        params.put("announcement_desc",announcementDesc);
        params.put("user_id",userId);

        return new JsonObjectRequest(Request.Method.POST,
                domain+endpoint,
                params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        requestResult.notifySuccess("addAnnouncement",response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        requestResult.notifyError("addAnnouncement",error);
                    }
                });
    }

    public JsonObjectRequest addAgenda(int groupId, String agendaTitle, double startLat, double startLng, String startTime, String endTime) throws JSONException {
        String endpoint = "api/addAgenda";

        JSONObject params = new JSONObject();
        params.put("group_id",groupId);
        params.put("agenda_title",agendaTitle);
        params.put("agenda_desc","");
        params.put("start_lat",startLat);
        params.put("start_lng",startLng);
        params.put("start_alt",0.0);
        params.put("end_lat",0.0);
        params.put("end_lng",0.0);
        params.put("end_alt",0.0);
        params.put("start_time",startTime);
        params.put("end_time",endTime);

        return new JsonObjectRequest(Request.Method.POST,
                domain+endpoint,
                params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        requestResult.notifySuccess("addAgenda",response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        requestResult.notifyError("addAgenda",error);
                    }
                });
    }
}
