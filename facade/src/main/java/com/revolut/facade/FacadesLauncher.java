package com.revolut.facade;

import com.revolut.facade.orm.DatabaseService;

/**
 * Created by azhuravel on 23.05.16.
 */
public class FacadesLauncher {

    public static void main(String... args) {
        DatabaseService databaseService = new DatabaseService();
        new UserService(databaseService, Integer.parseInt(args[0]));
    }

}
