package indonesia.ima.com.el_contact.activity;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.TextView;
import android.widget.Toast;

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

import indonesia.ima.com.el_contact.R;
import indonesia.ima.com.el_contact.adapter.KontakAdapter;
import indonesia.ima.com.el_contact.json.Kontak;

public class DaftarKontak extends AppCompatActivity implements KontakAdapter.ContactsAdapterListener {
    private String mPostKeyIdGroup = null;
    private String mPostKeyNamaGroup = null;
    private TextView namagroup;

    private static final String TAG = DaftarKontak.class.getSimpleName();
    private RecyclerView recyclerView;
    private List<Kontak> contactList;
    private KontakAdapter staggeredRecyclerViewAdapter;
    private SearchView searchView;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_kontak);

        namagroup = findViewById(R.id.nama_group);
        recyclerView = findViewById(R.id.recycler_view);

        contactList = new ArrayList<>();

        mPostKeyIdGroup = getIntent().getExtras().getString("id_group");
        mPostKeyNamaGroup = getIntent().getExtras().getString("nama_group");

        namagroup.setText("DAFTAR KONTAK " + mPostKeyNamaGroup);

        staggeredRecyclerViewAdapter = new KontakAdapter(this, contactList, (KontakAdapter.ContactsAdapterListener) this);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(staggeredRecyclerViewAdapter);

        loadProducts();

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) findViewById(R.id.search_view);
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                staggeredRecyclerViewAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                staggeredRecyclerViewAdapter.getFilter().filter(query);
                return false;
            }
        });
    }

    private void loadProducts() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://el-contact.000webhostapp.com/kontak_preview.php",

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
                                if (product.getString("id_group").equals(mPostKeyIdGroup)) {
                                    contactList.add(new Kontak(
                                            product.getString("id_kontak"),
                                            product.getString("id_group"),
                                            product.getString("nama_kontak"),
                                            product.getString("no_hp"),
                                            product.getString("status_show")
                                    ));
                                }
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
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        // close search view on back button pressed
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    @Override
    public void onContactSelected(Kontak contact) {
        Toast.makeText(this, "" + contact.getNama_kontak(), Toast.LENGTH_SHORT).show();
    }
}
