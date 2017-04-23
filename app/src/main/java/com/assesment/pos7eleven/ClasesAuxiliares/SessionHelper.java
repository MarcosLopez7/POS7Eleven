package com.assesment.pos7eleven.ClasesAuxiliares;

import okhttp3.OkHttpClient;

/**
 * Created by marco on 16/04/2017.
 */

public final class SessionHelper {

    private SessionHelper() {
        admin = false;
        logged_in = false;
        staff_user = false;
        first_name = "";
        last_name = "";
        id = -1;
        client = null;
        store_id = 0;
    }

    public static int id;
    public static boolean admin;
    public static boolean logged_in;
    public static String first_name;
    public static String last_name;
    public static boolean staff_user;
    public static int store_id;
    public static OkHttpClient client;
}
