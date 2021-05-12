package com.example.fridgeit

object Constants {
    //db name
    const val DB_NAME ="ITEMS_DB"

    //db version
    const val DB_VERSION = 1

    //table name
    const val TABLE_NAME1 = "STORED_ITEMS"
    // columns of table
    const val C_ID = "ID"
    const val C_NAME = "NAME"
    const val C_TYPE = "TYPE"
    const val C_EXPIRE_DATE = "EXPIRE"
    const val C_CREATED_DATE = "CREATE_DATE"

    const val CREATE_TABLE1 = ("CREATE TABLE "+ TABLE_NAME1+" ("+ C_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
            C_NAME+" text,"+
            C_TYPE+" TEXT,"+
            C_EXPIRE_DATE+" TEXT,"+
            C_CREATED_DATE+" TEXT"+" );")

    //table name 2
    const val TABLE_NAME2 = "SHOPPING_LIST"
    //columns of table 2
    const val S_ID = "ID"
    const val S_NAME = "NAME"
    const val S_DONE = "DONE"

    const val CREATE_TABLE2 = ("CREATE TABLE "+ TABLE_NAME2+"("+ S_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
            + S_NAME+" TEXT,"
            + S_DONE+" TEXT"
            +" );")





}