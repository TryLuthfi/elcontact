package indonesia.ima.com.el_contact.fragment;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import indonesia.ima.com.el_contact.R;
import indonesia.ima.com.el_contact.activity.DaftarKontak;
import indonesia.ima.com.el_contact.activity.MainActivity;
import indonesia.ima.com.el_contact.adapter.GroupAdapter;
import indonesia.ima.com.el_contact.json.Group;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentBeranda extends Fragment implements GroupAdapter.ContactsAdapterListener {
    private RecyclerView recyclerView;
    private List<Group> contactList;
    private GroupAdapter staggeredRecyclerViewAdapter;
    private SearchView searchView;


    public FragmentBeranda() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_beranda, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);

        contactList = new ArrayList<>();

        staggeredRecyclerViewAdapter = new GroupAdapter(getActivity(), contactList, (GroupAdapter.ContactsAdapterListener) this);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(staggeredRecyclerViewAdapter);

        loadProducts();

        SearchManager searchManager = (SearchManager) Objects.requireNonNull(getActivity()).getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) view.findViewById(R.id.search_view);
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getActivity().getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                staggeredRecyclerViewAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                staggeredRecyclerViewAdapter.getFilter().filter(query);
                return false;
            }
        });

        return view;
    }

    private void loadProducts() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://el-contact.000webhostapp.com/group_preview.php",

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);


                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject product = array.getJSONObject(i);

                                //adding the product to product list
                                contactList.add(new Group(
                                        product.getString("id_group"),
                                        product.getString("nama_group"),
                                        product.getString("gambar_group")
                                ));
                            }


                            staggeredRecyclerViewAdapter.notifyDataSetChanged();

//                            loading.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //adding our stringrequest to queue
        Volley.newRequestQueue(getActivity().getApplicationContext()).add(stringRequest);
    }

    @Override
    public void onContactSelected(Group contact) {
        Intent intent = new Intent(getActivity(), DaftarKontak.class);
        intent.putExtra("id_group", contact.getId_group());
        intent.putExtra("nama_group", contact.getNama_group());
        startActivity(intent);
    }

}
