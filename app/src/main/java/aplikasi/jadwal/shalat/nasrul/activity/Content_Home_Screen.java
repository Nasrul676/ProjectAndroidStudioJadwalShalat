package aplikasi.jadwal.shalat.nasrul.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import info.blogbasbas.jadwalshalat.R;
import aplikasi.jadwal.shalat.nasrul.activity.listsurah.QuranActivity;

public class Content_Home_Screen extends AppCompatActivity {

    @BindView(R.id.img_al_quran)
    CircleImageView imgAlQuran;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content__home__screen);
    }

    @OnClick(R.id.img_al_quran)
    public void onImgAlQuranClicked() {
        startActivity(new Intent(this, QuranActivity.class));
    }
}
