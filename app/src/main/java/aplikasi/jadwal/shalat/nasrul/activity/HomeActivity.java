package aplikasi.jadwal.shalat.nasrul.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import info.blogbasbas.jadwalshalat.R;
import aplikasi.jadwal.shalat.nasrul.activity.listsurah.QuranActivity;
import aplikasi.jadwal.shalat.nasrul.activity.notes.ActivityAsmaulHusna;
import aplikasi.jadwal.shalat.nasrul.arah_kiblat.arah_kiblat;
import aplikasi.jadwal.shalat.nasrul.model.Items;
import aplikasi.jadwal.shalat.nasrul.model.Jadwal;
import aplikasi.jadwal.shalat.nasrul.network.ApiClient;
import aplikasi.jadwal.shalat.nasrul.network.ApiInterface;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    String zuhur, ashar, magrib, isya, subuh;
    List<Jadwal> jadwal;

    Location location;
    String lokasi;
    ProgressDialog pd;
    @BindView(R.id.tv_tanngal)
    TextView tvTanngal;
    @BindView(R.id.tv_nama_daerah)
    TextView tvNamaDaerah;
    @BindView(R.id.tv_notif)
    TextView tvNotifikasi;
    @BindView(R.id.textView2)
    TextView textView2;
    @BindView(R.id.txtSubuh)
    TextView txtSubuh;
    @BindView(R.id.img_subuh)
    ImageView imgSubuh;
    @BindView(R.id.textView4)
    TextView textView4;
    @BindView(R.id.txtDzuhur)
    TextView txtDzuhur;
    @BindView(R.id.img_zuhur)
    ImageView imgZuhur;
    @BindView(R.id.textView5)
    TextView textView5;
    @BindView(R.id.txtAshar)
    TextView txtAshar;
    @BindView(R.id.img_ashar)
    ImageView imgAshar;
    @BindView(R.id.textView6)
    TextView textView6;
    @BindView(R.id.txtMaghrib)
    TextView txtMaghrib;
    @BindView(R.id.img_magrhib)
    ImageView imgMagrhib;
    @BindView(R.id.textView7)
    TextView textView7;
    @BindView(R.id.txtIsya)
    TextView txtIsya;
    @BindView(R.id.img_isya)
    ImageView imgIsya;
    @BindView(R.id.img_arah_kabah)
    CircleImageView imgArahKabah;
    @BindView(R.id.img_al_quran)
    CircleImageView imgAlQuran;
    @BindView(R.id.asmaulhusna)
    CircleImageView AsmaulHusna;
    @BindView(R.id.inspirasi)
    CircleImageView Inspirasi;
    @BindView(R.id.swipe_id)
    SwipeRefreshLayout swipeId;
    NestedScrollView idHomeActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_home);

        TextView textdoa = findViewById(R.id.btn_doa);
        textdoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActivity.this, doa_harian.class);
                startActivity(i);
            }
        });


        TextView text1 = findViewById(R.id.btn_berita1);
        text1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, berita1.class);
                startActivity(i);
            }
        });

        TextView notif = findViewById(R.id.tv_notif);


        TextView text = findViewById(R.id.btn_sholat);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, tata_cara_sholat_activity.class);
                startActivity(i);
            }
        });

        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        idHomeActivity = findViewById(R.id.id_home_activity);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        pd = new ProgressDialog(HomeActivity.this);
        pd.setTitle("Loading . . . ");
        pd.setMessage("Mohon Tunggu . . .");
        pd.setCancelable(true);
        pd.show();

        actionLoad();
        refresh();
        getTimeFromAndroid();

        Calendar calendar = Calendar.getInstance();
        String currentdate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

        TextView show_date = (TextView) findViewById(R.id.tv_tanngal);
        show_date.setText(currentdate);

    }

    private void getTimeFromAndroid() {

        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if(timeOfDay >= 3 && timeOfDay < 5){
            tvNotifikasi.setText("Menjelang Sholat Subuh");
        }else if(timeOfDay >= 11 && timeOfDay < 12){
            tvNotifikasi.setText("Menjelang Sholat Dzuhur");
        }else if(timeOfDay >= 14 && timeOfDay < 16){
            tvNotifikasi.setText("Menjelang Sholat Ashar");
        }else if(timeOfDay >= 17 && timeOfDay < 18){
            tvNotifikasi.setText("Menjelang Sholat Maghrib");
        }else if(timeOfDay >= 18 && timeOfDay < 23){
            tvNotifikasi.setText("Menjelang Sholat Isya");
        }
    }

    private void refresh() {
        swipeId.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeId.setRefreshing(true);
                actionLoad();
                getTimeFromAndroid();
            }
        });

    }

    private void actionLoad() {

        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
        if (!EasyPermissions.hasPermissions(getApplicationContext(), perms)) {
            EasyPermissions.requestPermissions(HomeActivity.this, "Membutuhkan Izin Akses Lokasi", 10, perms);
        } else {

            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


            //Best location provider is decided by the criteria
            Criteria criteria = new Criteria();
            //location manager will take the best location from the criteria
            locationManager.getBestProvider(criteria, true);

            //nce  you  know  the  name  of  the  LocationProvider,  you  can  call getLastKnownPosition() to  find  out  where  you  were  recently.
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.]

                return;
            }
            location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, true));

            Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
            // latTextView.setText(String.valueOf(location.getLatitude()));
            //longTextView.setText(String.valueOf(location.getLongitude()));
            Log.d("Tag", "1");
            List<Address> addresses;

            try {
                addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                if (addresses.size() > 0)

                {
                    //while(locTextView.getText().toString()=="Location") {
                    Log.d("Cek lokasi", "" + addresses.get(0).getAdminArea());
                    lokasi = addresses.get(0).getAdminArea();
                    tvNamaDaerah.setText(lokasi);
                    swipeId.setRefreshing(false);
                    // }
                }

            } catch (NullPointerException e) {
                e.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();
            }
            // GET CURRENT LOCATION
            FusedLocationProviderClient mFusedLocation = LocationServices.getFusedLocationProviderClient(this);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mFusedLocation.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        // Do it all with location
                        Log.d("My Current location", "Lat : " + location.getLatitude() + " Long : " + location.getLongitude());
                        // Display in Toast
                        Geocoder gcd3 = new Geocoder(getBaseContext(), Locale.getDefault());
                        List<Address> addresses;

                        try {
                            addresses = gcd3.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            if (addresses.size() > 0)

                            {
                                //while(locTextView.getText().toString()=="Location") {
                                Log.d("Cek lokasi", "1 :" + addresses.get(0).getLocality());
                                lokasi = addresses.get(0).getLocality();

                                if (lokasi != null) {
                                    Log.d("location", "locatin :" + lokasi);

                                    ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                                    Call<Items> call = apiInterface.getJadwalSholat(lokasi);
                                    call.enqueue(new Callback<Items>() {
                                        @Override
                                        public void onResponse(Call<Items> call, Response<Items> response) {
                                            Log.d("Data ", " respon" + response.body().getItems());
                                            jadwal = response.body().getItems();
                                            Log.d("respon data ", "" + new Gson().toJson(jadwal));

                                            if (jadwal != null) {
                                                zuhur = jadwal.get(0).getZuhur();
                                                ashar = jadwal.get(0).getAshar();
                                                magrib = jadwal.get(0).getMaghrib();
                                                isya = jadwal.get(0).getIsya();
                                                subuh = jadwal.get(0).getFajar();
                                                Log.d("respon :", "" + zuhur);
                                                txtDzuhur.setText(zuhur);

                                                txtAshar.setText(ashar);
                                                txtMaghrib.setText(magrib);
                                                txtIsya.setText(isya);
                                                txtSubuh.setText(subuh);

                                                Log.d("", "lokasi" + lokasi);
                                                tvNamaDaerah.setText(lokasi);
                                                swipeId.setRefreshing(false);
                                                pd.dismiss();

                                            } else {
                                                Toast.makeText(HomeActivity.this, "Kesalahan Jaringan", Toast.LENGTH_SHORT).show();
                                                swipeId.setRefreshing(false);
                                            }
                                            //  loadData(jadwal);
                                        }

                                        @Override
                                        public void onFailure(Call<Items> call, Throwable t) {
                                            Log.d("Data ", "" + t.getMessage());
                                            swipeId.setRefreshing(false);
                                            pd.dismiss();
                                        }
                                    });

                                }
                            }

                        } catch (NullPointerException e) {
                            e.printStackTrace();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
            });
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        if (requestCode == 10) {
            actionLoad();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }

    @OnClick(R.id.img_subuh)
    public void onImgSubuhClicked() {
        Snackbar.make(idHomeActivity, R.string.coming_soon, Snackbar.LENGTH_LONG).show();
    }

    @OnClick(R.id.img_zuhur)
    public void onImgZuhurClicked() {
        Snackbar.make(idHomeActivity, R.string.coming_soon, Snackbar.LENGTH_LONG).show();
    }

    @OnClick(R.id.img_ashar)
    public void onImgAsharClicked() {

        Snackbar.make(idHomeActivity, R.string.coming_soon, Snackbar.LENGTH_LONG).show();
    }

    @OnClick(R.id.img_magrhib)
    public void onImgMagrhibClicked() {

        Snackbar.make(idHomeActivity, R.string.coming_soon, Snackbar.LENGTH_LONG).show();
    }

    @OnClick(R.id.img_isya)
    public void onImgIsyaClicked() {

        Snackbar.make(idHomeActivity, R.string.coming_soon, Snackbar.LENGTH_LONG).show();
    }

    @OnClick(R.id.img_arah_kabah)
    public void onImgArahKabahClicked() {
        startActivity(new Intent(this, arah_kiblat.class));
    }

    @OnClick(R.id.img_al_quran)
    public void onImgAlQuranClicked() {
        startActivity(new Intent(this, QuranActivity.class));
    }


    @OnClick(R.id.asmaulhusna)
    public void onAsmaulHusnaClicked() {
        startActivity(new Intent(this, ActivityAsmaulHusna.class));
    }

    @OnClick(R.id.inspirasi)
    public void setInspirasi() {
        startActivity(new Intent(this, inspirasi.class));
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Do you want to Exit?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}