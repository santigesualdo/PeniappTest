package com.mobile.santige.nuevaaplicacion;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "peniaManager";

    // tables name
    private static final String TABLE_PERSON= "person";
    private static final String TABLE_GASTO= "gasto";
    private static final String TABLE_PENIA= "penia";

    // Person table column name
    private static final String PERSON_KEY_ID_ = "id";
    private static final String PERSON_KEY_ID_PENIA = "id_penia";
    private static final String PERSON_KEY_NOMBRE = "nombre";

    // Penia table column name
    private static final String PENIA_KEY_ID_= "id";
    private static final String PENIA_KEY_MONTO = "monto";
    private static final String PENIA_KEY_FECHA = "fecha";
    private static final String PENIA_KEY_COUNTPERSONS = "countpersons";
    private static final String PENIA_KEY_ACTIVE = "activa";

    // Gastos table column name
    private static final String GASTO_KEY_ID = "id";
    private static final String GASTO_KEY_ID_PERSON= "id_person";
    private static final String GASTO_KEY_MONTO= "monto";

    private static DatabaseHandler sInstance;
    Context savedContext;

    public static synchronized DatabaseHandler getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DatabaseHandler(context.getApplicationContext());
        }
        return sInstance;
    }

    private DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        savedContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PENIA_TABLE = "CREATE TABLE " + TABLE_PENIA + "("
                + PENIA_KEY_ID_ + " INTEGER PRIMARY KEY,"
                + PENIA_KEY_MONTO + " TEXT,"
                + PENIA_KEY_FECHA + " TEXT,"
                + PENIA_KEY_COUNTPERSONS + " INTEGER, "
                + PENIA_KEY_ACTIVE + " INTEGER )";

        String CREATE_PERSON_TABLE = "CREATE TABLE " + TABLE_PERSON + "("
                + PERSON_KEY_ID_+ " INTEGER PRIMARY KEY,"
                + PERSON_KEY_ID_PENIA + " INTEGER,"
                + PERSON_KEY_NOMBRE+ " TEXT)";

        String CREATE_GASTO_TABLE = "CREATE TABLE " + TABLE_GASTO + "("
                + GASTO_KEY_ID + " INTEGER PRIMARY KEY,"
                + GASTO_KEY_ID_PERSON + " INTEGER,"
                + GASTO_KEY_MONTO + " TEXT)";

        db.execSQL(CREATE_PENIA_TABLE);
        db.execSQL(CREATE_PERSON_TABLE);
        db.execSQL(CREATE_GASTO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PENIA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PERSON);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GASTO);

        onCreate(db);
    }

    // PEÑA
        /* Agregar peña */
        public Long addPenia(Penia penia){
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(PENIA_KEY_MONTO, penia.getMonto()); // Penia monto
            values.put(PENIA_KEY_FECHA, penia.getFecha()); // Penia monto
            values.put(PENIA_KEY_COUNTPERSONS, penia.getCountPersons()); // Penia monto
            values.put(PENIA_KEY_ACTIVE, penia.getActiva()); // Penia monto

            Long result = db.insert(TABLE_PENIA, null, values);
            db.close();

            return result;
        }

        /* Obtener nuevo ID para peña */
        public Integer getPeniaNextId(){
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor cursor = db.query(TABLE_PENIA, new String[] { "max("+PENIA_KEY_ID_+")"}
                            , null, null , null, null, null, null);

            if (cursor != null){
                cursor.moveToFirst();
                return 1;
            }

            return cursor.getInt(0)+1;
        }

        /* Obtener penia por id */
        public Penia getPenia(int id) {
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor cursor = db.query(TABLE_PENIA, new String[] { PENIA_KEY_ID_,
                            PENIA_KEY_MONTO, PENIA_KEY_FECHA , PENIA_KEY_COUNTPERSONS}, PENIA_KEY_ID_ + "=? and "+ PENIA_KEY_ACTIVE+ "=1",
                    new String[] { String.valueOf(id) }, null, null, null, null);

            if (cursor != null)
                cursor.moveToFirst();

            Penia penia = new Penia(
                    cursor.getInt(0),
                    Double.parseDouble(cursor.getString(1)),
                    cursor.getString(2),
                    cursor.getInt(3),
                    cursor.getInt(4)
            );

            // return contact
            return penia;
        }

        /* Obtener todas las penias */
        public List<Penia> getAllPenias(){
            List<Penia> peniaList = new ArrayList<Penia>();
            String selectQuery = "SELECT  * FROM " + TABLE_PENIA;

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    Penia penia = new Penia(
                            cursor.getInt(0),
                            Double.parseDouble(cursor.getString(1)),
                            cursor.getString(2),
                            cursor.getInt(3),
                            cursor.getInt(4)
                    );
                    peniaList.add(penia);
                } while (cursor.moveToNext());
            }

            return peniaList;
        }

        /* Actualizar penia */
        public int updatePenia(Penia penia) {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(PENIA_KEY_MONTO, penia.getMonto());
            values.put(PENIA_KEY_FECHA, penia.getFecha());
            values.put(PENIA_KEY_COUNTPERSONS, penia.getCountPersons());
            values.put(PENIA_KEY_ACTIVE, penia.getActiva());

            // updating row
            return db.update(TABLE_PENIA, values, PENIA_KEY_ID_ + " = ?",
                    new String[] { String.valueOf(penia.getId()) });
        }

        /* Delete penia */
        public void deletePenia(Penia penia) {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_PENIA, PENIA_KEY_ID_ + " = ?",
                    new String[] { String.valueOf(penia.getId()) });
            db.close();
        }

        /* Obtener cuenta de penias */
        public Integer getPeniaCount() {
            SQLiteDatabase db = this.getReadableDatabase();
            Integer res = 0;

            String countQuery = "SELECT  * FROM " + TABLE_PENIA;
            if (db!=null){
                Cursor cursor = db.rawQuery(countQuery, null);
                res = cursor.getCount();
                cursor.close();
            }
            return res;
        }

    // PERSONA
        /* Agregar persona */
        public Long addPerson(Persona persona, Penia penia){
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(PERSON_KEY_ID_PENIA, penia.getId()); // Penia monto
            values.put(PERSON_KEY_NOMBRE, persona.getNombre()); // Penia monto

            Long result = db.insert(TABLE_PERSON, null, values);
            db.close();

            return result;
        }

        /* Obtener persona por Id */
        public Persona getPersona(int id){
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor cursor = db.query(TABLE_PERSON, new String[] { PERSON_KEY_ID_,
                            PERSON_KEY_ID_PENIA, PERSON_KEY_NOMBRE}, PERSON_KEY_ID_ + "=? ",
                    new String[] { String.valueOf(id) }, null, null, null, null);

            if (cursor != null)
                cursor.moveToFirst();

            Persona persona = new Persona();
            persona.setId(cursor.getInt(0));
            persona.setPeniaId(cursor.getInt(1));
            persona.setNombre(cursor.getString(2));

            // return contact
            return persona;
        }

        /* Obtener todas las personas de una peña*/
        public List<Persona> getPersonasByPenia(int peniaId){
            List<Persona> personasList = new ArrayList<Persona>();
            String personQuery = "SELECT * FROM " + TABLE_PERSON + " WHERE " + PERSON_KEY_ID_PENIA + " = " + peniaId ;

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(personQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    Persona persona = new Persona();
                    persona.setId(cursor.getInt(0));
                    String gastosQuery = "SELECT * FROM " + TABLE_GASTO + " WHERE " + GASTO_KEY_ID_PERSON + " = " + persona.getId();

                    Cursor cursor2 = db.rawQuery(gastosQuery, null);
                    List<Gasto>  gastosPerson = new ArrayList<Gasto>();
                    if (cursor2.moveToFirst()){
                        do{
                            Gasto g = new Gasto("", cursor2.getDouble(2));
                            gastosPerson.add(g);
                        }while (cursor2.moveToNext());
                    }
                    if (gastosPerson!=null)
                        persona.setGastos(gastosPerson);
                    persona.setPeniaId(cursor.getInt(1));
                    persona.setNombre(cursor.getString(2));
                    personasList.add(persona);
                } while (cursor.moveToNext());
            }

            return personasList;
        }

        /* Obtener todas las personas */
        public List<Persona> getAllPersonas(){
            List<Persona> personasList = new ArrayList<Persona>();
            String selectQuery = "SELECT  * FROM " + TABLE_PERSON;

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    Persona persona = new Persona();
                    persona.setId(cursor.getInt(0));
                    persona.setPeniaId(cursor.getInt(1));
                    persona.setNombre(cursor.getString(2));
                    personasList.add(persona);
                } while (cursor.moveToNext());
            }

            return personasList;
        }

        /* Actualizar persona */
        public int updatePerson(Persona persona) {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(PERSON_KEY_ID_PENIA, persona.getPeniaId());
            values.put(PERSON_KEY_NOMBRE, persona.getNombre());

            // updating row
            return db.update(TABLE_PERSON, values, PERSON_KEY_ID_+ " = ?",
                    new String[] { String.valueOf(persona.getId()) });
        }

        /* Delete persona */
        public void deletePerson(Persona persona) {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_PERSON, PERSON_KEY_ID_ + " = ?",
                    new String[] { String.valueOf(persona.getId()) });
            db.close();
        }

        /* Obtener cuenta de personas */
        public int getPersonasCount() {
            String countQuery = "SELECT  * FROM " + TABLE_PERSON;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(countQuery, null);
            cursor.close();

            // return count
            return cursor.getCount();
        }

    // GASTO
        /* Agregar gasto */
        public Long addGasto(Gasto gasto, Integer personId){
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(GASTO_KEY_ID_PERSON, personId); // Penia monto
            values.put(GASTO_KEY_MONTO, gasto.getMonto()); // Penia monto

            Long result = db.insert(TABLE_GASTO, null, values);
            db.close();

            return result;
        }

        /* Obtener gasto por Id */
        public Gasto getGasto(int id){
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor cursor = db.query(TABLE_GASTO, new String[] { GASTO_KEY_ID,
                            GASTO_KEY_MONTO, GASTO_KEY_ID_PERSON}, GASTO_KEY_ID + "=? ",
                    new String[] { String.valueOf(id) }, null, null, null, null);

            if (cursor != null)
                cursor.moveToFirst();

            Gasto gasto= new Gasto("", cursor.getDouble(1));
            gasto.setId(cursor.getInt(2));

            return gasto;
        }

        /* Obtener todas las gastos */
        public List<Gasto> getAllGastos(){
            List<Gasto> gastosList = new ArrayList<Gasto>();
            String selectQuery = "SELECT  * FROM " + TABLE_GASTO;

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    Gasto gasto= new Gasto("", cursor.getDouble(1));
                    gasto.setId(cursor.getInt(2));
                    gastosList.add(gasto);
                } while (cursor.moveToNext());
            }

            return gastosList;
        }

        /* Actualizar gastos */
        public int updateGasto(Gasto gasto) {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(GASTO_KEY_ID, gasto.getId());
            values.put(GASTO_KEY_ID_PERSON, gasto.getIdPerson());
            values.put(GASTO_KEY_MONTO, gasto.getMonto());

            return db.update(TABLE_GASTO, values, GASTO_KEY_ID + " = ?",
                    new String[] { String.valueOf(gasto.getId()) });
        }

        /* Delete penia */
        public void deleteGasto(Gasto gasto) {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_GASTO, GASTO_KEY_ID + " = ?",
                    new String[] { String.valueOf(gasto.getId()) });
            db.close();
        }

        /* Obtener cuenta de gastos */
        public int getGastosCount() {
            String countQuery = "SELECT  * FROM " + TABLE_GASTO;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(countQuery, null);
            cursor.close();

            // return count
            return cursor.getCount();
        }

}
