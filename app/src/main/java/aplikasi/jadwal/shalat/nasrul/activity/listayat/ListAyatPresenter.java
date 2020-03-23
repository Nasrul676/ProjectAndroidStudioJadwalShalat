package aplikasi.jadwal.shalat.nasrul.activity.listayat;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import aplikasi.jadwal.shalat.nasrul.base.BasePresenter;
import aplikasi.jadwal.shalat.nasrul.database.DatabaseContract;
import aplikasi.jadwal.shalat.nasrul.database.DatabaseHelper;
import aplikasi.jadwal.shalat.nasrul.modelquran.Ayat;


public class ListAyatPresenter extends BasePresenter<ListAyatView> {
    ListAyatPresenter(ListAyatView view) {
        super.attach(view);
    }

    void loadAyat(String loadSurah , String loadTerjemahan) {
        SQLiteDatabase database = DatabaseHelper.getDatabase();
        Cursor cursor = database.query(DatabaseContract.TableAyat.TABLE_AYAT, null, DatabaseContract.TableAyat.SURAH + " LIKE '" + loadSurah + "'",null, null, null, null);

        ArrayList<Ayat> data = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                String surah = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TableAyat.SURAH));
                String ayat = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TableAyat.AYAT));
                String arab = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TableAyat.ARAB));
                String terjemahan = cursor.getString(cursor.getColumnIndexOrThrow(loadTerjemahan));

                data.add(new Ayat(surah , ayat , arab , terjemahan));
            } while (cursor.moveToNext());
        }
        mView.onLoad(data);

        cursor.close();
        database.close();
    }
}
