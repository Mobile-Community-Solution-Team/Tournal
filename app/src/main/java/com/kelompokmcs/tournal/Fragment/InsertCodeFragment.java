package com.kelompokmcs.tournal.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kelompokmcs.tournal.API.APIRequest;
import com.kelompokmcs.tournal.Listener.RequestResult;
import com.kelompokmcs.tournal.Model.Group;
import com.kelompokmcs.tournal.R;
import com.kelompokmcs.tournal.API.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class InsertCodeFragment extends Fragment implements RequestResult{

    private EditText etGroupCode;
    private Button btnNext;
    private passGroupDataListener listener;
    private APIRequest apiRequest;
    private LinearLayout loadingLayout;
    private Toolbar toolbar;

    public interface passGroupDataListener{
        void passGroupData(Group group);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (passGroupDataListener) getActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                getActivity().finish();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_insert_code, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        View rootView = getView();
        etGroupCode = rootView.findViewById(R.id.et_group_code);
        btnNext = rootView.findViewById(R.id.btn_next);
        loadingLayout = rootView.findViewById(R.id.loading_layout);
        toolbar = rootView.findViewById(R.id.toolbar);
        apiRequest = new APIRequest(this);

        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String groupCode = etGroupCode.getText().toString();
                JsonObjectRequest req = null;

                try {
                    req = apiRequest.verifyGroupCodeRequest(groupCode);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                VolleySingleton.getInstance(getContext()).addToRequestQueue(req);
                loadingLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void notifySuccess(String requestType, JSONObject response) {
        if(requestType.equals("verifyGroupCode")){
            try {
                JSONArray values = response.getJSONArray("response");
                //jsonobject yang didapat pasti berjumlah 1 atau group tidak ditemukan
                if(values.length() > 0){
                    JSONObject result = values.getJSONObject(0);
                    int groupId = result.getInt("group_id");
                    String name = result.getString("group_name");

                    listener.passGroupData(new Group(groupId,"",name,"","","",""));
                }
                else{
                    Toast.makeText(getContext(), "Wrong group id", Toast.LENGTH_SHORT).show();
                }
                loadingLayout.setVisibility(View.GONE);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void notifyError(String requestType, VolleyError error) {
        if(requestType.equals("verifyGroupCode")){
            Toast.makeText(getContext(), "Something Error", Toast.LENGTH_SHORT).show();
            Log.e("text",error.toString());
            loadingLayout.setVisibility(View.GONE);
        }
    }
}
