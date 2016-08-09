package com.coderockets.referandumproject.db;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.coderockets.referandumproject.model.ModelUser;

/**
 * Created by aykutasil on 9.08.2016.
 */
public class DbManager {

    public static ModelUser getModelUser() {
        return new Select().from(ModelUser.class).executeSingle();
    }

    public static void deleteModelUser() {
        new Delete().from(ModelUser.class).execute();
    }
}
