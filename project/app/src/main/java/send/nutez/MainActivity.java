package send.nutez;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import org.greenrobot.greendao.database.Database;

import send.nutez.model.DaoMaster;
import send.nutez.model.DaoSession;
import send.nutez.model.Nute;
import send.nutez.model.NuteDao;
import send.nutez.model.NuteReferenceValue;
import send.nutez.model.NuteReferenceValueDao;

public class MainActivity extends AppCompatActivity {
    private static Database mDatabase;
    private static DaoSession mDaoSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Nute nute = new Nute("test19", "slutz");
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(getApplicationContext(), "TASKS");
        mDatabase = helper.getWritableDb();
        mDaoSession = new DaoMaster(mDatabase).newSession();
        NuteDao nuteDao = mDaoSession.getNuteDao();
        nuteDao.deleteAll();
        mDaoSession.getNuteReferenceValueDao().deleteAll();
        nuteDao.insert(nute);
        Nute nute1 = nuteDao.queryBuilder().where(NuteDao.Properties.Name.eq("test19")).unique();
        Log.d("LALALA", nute1.getName());

        NuteReferenceValue val = new NuteReferenceValue();
        val.setNute(nute1);
        mDaoSession.getNuteReferenceValueDao().insert(val);
        NuteReferenceValue nuteReferenceValue = mDaoSession.getNuteReferenceValueDao().queryBuilder().where(NuteReferenceValueDao.Properties.Id.eq(val.getId())).list().get(0);
        Log.d("LALALA", nuteReferenceValue.getNute().getName());
        nute1.setName("uufff");
        nuteReferenceValue = mDaoSession.getNuteReferenceValueDao().queryBuilder().where(NuteReferenceValueDao.Properties.Id.eq(val.getId())).list().get(0);
        Log.d("LALALA", nuteReferenceValue.getNute().getName());
    }
}