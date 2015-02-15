package anastasoft.rallyvision.controller.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import anastasoft.rallyvision.controller.Data.model.Afericao;
import anastasoft.rallyvision.controller.Observable;

public class DBHelper extends SQLiteOpenHelper {

	// Logcat tag
	public static final String LOG = "DBHelper";

	// Database Version
	public static final int DATABASE_VERSION = 3;

	// Database Name
	public static final String DATABASE_NAME = "RallyVisionDB";

	// Table Names
	public static final String TABLE_AFERICAO = "afericoes";

	// Common column names
	public static final String KEY_ID = "id";
	public static final String KEY_CREATED_AT = "created_at";

	// AFERICAOES Table - column names
	public static final String KEY_AFERICAO_NAME = "name";
    public static final String KEY_AFERICAO_RATIO = "afericao_ratio";


    // Default AFERICAOES names
	public static String AFERICAO_DEFAULT;

    /**
     * Usado para manter-se atualizado de novos updates
     */
    private static Observable aObservable;


	// CHECKLINES Table - column names
	public static final String KEY_CHECKLINES_TEXT = "text";
	public static final String KEY_CHECKLINES_AFERICAO_ID = "actionbox_id";
	public static final String KEY_CHECKLINES_ORDER = "line_order";
	public static final String KEY_CHECKLINES_CHECKED = "checked";
	public static final String KEY_CHECKLINES_UPDATE_TIME = "update_time";
	public static final String KEY_CHECKLINES_CHECK_TIME = "check_time";
	public static final String KEY_CHECKLINES_CALENDAR_ID = "calendar_id";

    // Table Create Statements
	// AFERICAOES table create statement
	private static final String CREATE_TABLE_AFERICAO = "CREATE TABLE "
			+ TABLE_AFERICAO + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
			+ KEY_AFERICAO_NAME + " TEXT UNIQUE NOT NULL," + KEY_AFERICAO_RATIO
			+ " FLOAT" + ")";

    public DBHelper(Context context, Observable aObservable) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.aObservable = aObservable;
    }


    @Override
	public void onCreate(SQLiteDatabase db) {

		// creating required tables
		db.execSQL(CREATE_TABLE_AFERICAO);

	}


	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		// on upgrade drop older tables
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_AFERICAO);

		// create new tables
		onCreate(db);
	}





	// ------------------------ AFERICAOES table methods ----------------//

	// creating an afericao
	public long createAfericao (Afericao afericao) {

		SQLiteDatabase db = this.getWritableDatabase();

		Log.e(LOG, "Creating AFERICAO at: " + afericao.toS());

		ContentValues values = new ContentValues();
		values.put(KEY_AFERICAO_NAME, afericao.getName());
        values.put(KEY_AFERICAO_RATIO, afericao.getRatio());

		// insert row and return id

            return db.insert(TABLE_AFERICAO, null, values);



	}

//	// get actionbox by id
//	public ActionBox getActionBoxById(long id) {
//		SQLiteDatabase db = this.getReadableDatabase();
//
//		String selectQuery = "SELECT  * FROM " + TABLE_AFERICAO + " WHERE "
//				+ KEY_ID + " = " + id;
//
//		Log.e(LOG, selectQuery);
//
//		Cursor c = db.rawQuery(selectQuery, null);
//
//		if (c != null)
//			c.moveToFirst();
//		return new ActionBox(c);
//	}
//
//	// get actionbox by name
//	public ActionBox getActionBoxByName(String name) {
//		SQLiteDatabase db = this.getReadableDatabase();
//
//		String selectQuery = "SELECT  * FROM " + TABLE_AFERICAO + " WHERE "
//				+ KEY_AFERICAO_NAME + " = " + "'" + name + "'";
//
//		Log.e(LOG, selectQuery);
//
//		Cursor c = db.rawQuery(selectQuery, null);
//
//		if (c != null)
//			c.moveToFirst();
//		return new ActionBox(c);
//	}

	/** getting all actionboxes */
	public List<Afericao> getTodasAfericoes() {
		List<Afericao> afericao = new ArrayList<Afericao>();
		String selectQuery = "SELECT  * FROM " + TABLE_AFERICAO;

		Log.e(LOG, selectQuery);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				// adding to afericao list
				afericao.add(new Afericao(c));
			} while (c.moveToNext());
		}
		return afericao;
	}



	/**Count actionboxes */
	public int getAfericaoCount() {
		String countQuery = "SELECT  * FROM " + TABLE_AFERICAO;

		Log.e(LOG, countQuery);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(countQuery, null);

		return c.getCount();
	}

    /**
     * Utilizado quando há um Obsearvable como referência
     */
    public void update()  {
        if(aObservable != null){
            try{
                createAfericao(aObservable.getAfericao());

            }catch (Exception e){
                throw new AfericaoExistenteException();
            }
        }
    }

    public class AfericaoExistenteException extends SQLiteConstraintException {
        public AfericaoExistenteException (){
            super("Aferição já existente");
        }
    }

	/**
     *  Update a checkline
     */
	public int updateActionBox(Afericao afericao) {
		SQLiteDatabase db = this.getWritableDatabase();

		Log.e(LOG, "Updating AFERICAOES at: " + afericao.toS());

		ContentValues values = new ContentValues();
		values.put(KEY_ID, afericao.getId());
		values.put(KEY_AFERICAO_NAME, afericao.getName());
		values.put(KEY_AFERICAO_NAME, afericao.getRatio());

		// updating row
		return db.update(TABLE_AFERICAO, values, KEY_ID + " = ?",
				new String[] { String.valueOf(afericao.getId()) });
	}

    /**
     *     delete a actionbox
     */
	public void deleteActionBox(long id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_AFERICAO, KEY_ID + " = ?",
				new String[] { String.valueOf(id) });
	}

    /**
     * Retorna o objeto aferição referente ao nome fornecido. O objeto é único, dado que os nomes cadastrados devem ser únicos
     * @param afericaoName
     * @return Aferiçao
     */

    public Afericao getAfericaoByName(String afericaoName) {

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_AFERICAO + " WHERE "
                + KEY_AFERICAO_NAME + " = " + "'" + afericaoName + "'";

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();
        return new Afericao(c);
    }
}
