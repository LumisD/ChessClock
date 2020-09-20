package com.lumisdinos.chessclock.data

object Constants {

    const val GAME = "game"

    const val _15_10 = "15, 0, 10"//15m +10s
    const val _5_5 = "5, 0, 5"    //5m +5s
    const val _3_2 = "3, 0, 2"    //3m +2s
    const val _2_1 = "2, 0, 1"    //2m +1s
    const val _1_1 = "1, 0, 1"    //1m +1s
    const val _45_45 = "45, 0, 45"//45m +45s

    const val _60 = "60, 0, 0"    //60m
    const val _30 = "30, 0, 0"    //30m
    const val _20 = "20, 0, 0"    //20m
    const val _10 = "10, 0, 0"    //10m
    const val _5 = "5, 0, 0"      //5m
    const val _3= "3, 0, 0"       //3m
    const val _1 = "1, 0, 0"      //1m

    const val STRING = "String"
    const val INTEGER = "Integer"
    const val FLOAT = "Float"
    const val DATE = "Date"
    val NULL = "NULL"




    val COLUMN_TYPES = listOf<String>(STRING, INTEGER, FLOAT, DATE)
    val MONTHS = listOf<String>("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
    val MONTHS_SHORT = listOf<String>("Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sep", "Oct", "Nov", "Dec")
    val DAYS = listOf<String>("01", "02", "03", "04", "05", "06", "07", "08", "09", "10",
        "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
        "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31")
    val NONE_ALL_DAYS = listOf<String>("NONE", "ALL")



    const val DISTINCT_COLUMN_NO_LIMIT = -1//-1 - no limit

    const val TEST_TABLE_TXT = "TestTable.txt"
    const val TEST_TABLE_ID = 0

    const val BASE_URL = ""//"https://jsonplaceholder.typicode.com"

    //Count20
    const val COUNT_TOTAL = "countTotal"
    const val COUNT_REST = "countRest"

    const val REST = "Rest"//has to be equal to String.rest tag
    const val TOTAL = "Total"//has to be equal to String.total tag

    //values for ExcludeCombined.combined
    const val REST_CALC = "Rest calculated"
    const val TOTAL_CALC = "Total calculated"
    const val NO_VALUE = "no_value"
    const val NULL_VALUE = "NULL_value"
    const val GROUPED_BY_TIME = "grouped_by_time"

    //values for PivotSettings.groupedBy
    //const val GROUPED_BY_CENTURY = "grouped_by_century"
    const val GROUPED_BY_YEAR = "grouped_by_year"
    const val GROUPED_BY_MONTH = "grouped_by_month"
    const val GROUPED_BY_DAY = "grouped_by_day"
    const val GROUPED_BY_HOUR = "grouped_by_hour"
    const val GROUPED_BY_MINUTE = "grouped_by_minute"
    const val GROUPED_BY_SECOND = "grouped_by_second"

    //Row20
    const val COLUMN_0 = "column_0"
    const val COLUMN_1 = "column_1"
    const val COLUMN_2 = "column_2"
    const val COLUMN_3 = "column_3"
    const val COLUMN_4 = "column_4"
    const val COLUMN_5 = "column_5"
    const val COLUMN_6 = "column_6"
    const val COLUMN_7 = "column_7"
    const val COLUMN_8 = "column_8"
    const val COLUMN_9 = "column_9"
    const val COLUMN_10 = "column_10"
    const val COLUMN_11 = "column_11"
    const val COLUMN_12 = "column_12"
    const val COLUMN_13 = "column_13"
    const val COLUMN_14 = "column_14"
    const val COLUMN_15 = "column_15"
    const val COLUMN_16 = "column_16"
    const val COLUMN_17 = "column_17"
    const val COLUMN_18 = "column_18"
    const val COLUMN_19 = "column_19"

    const val ADDITIONAL_0 = "additional_0"
    const val ADDITIONAL_1 = "additional_1"
    const val ADDITIONAL_2 = "additional_2"
    const val ADDITIONAL_3 = "additional_3"
    const val ADDITIONAL_4 = "additional_4"
    const val ADDITIONAL_5 = "additional_5"
    const val ADDITIONAL_6 = "additional_6"
    const val ADDITIONAL_7 = "additional_7"
    const val ADDITIONAL_8 = "additional_8"
    const val ADDITIONAL_9 = "additional_9"
    const val ADDITIONAL_10 = "additional_10"
    const val ADDITIONAL_11 = "additional_11"
    const val ADDITIONAL_12 = "additional_12"
    const val ADDITIONAL_13 = "additional_13"
    const val ADDITIONAL_14 = "additional_14"
    const val ADDITIONAL_15 = "additional_15"
    const val ADDITIONAL_16 = "additional_16"
    const val ADDITIONAL_17 = "additional_17"
    const val ADDITIONAL_18 = "additional_18"
    const val ADDITIONAL_19 = "additional_19"

    const val ROW_20_FIELD_DELIMITER = "_"

    //Delito
    const val DELITO_FECHA_RECEPCION = "FECHA_RECEPCION"
    const val DELITO_CRR_IDDELITO = "CRR_IDDELITO"
    const val DELITO_DELITO = "DELITO"
    const val DELITO_COD_DELITO = "COD DELITO"
    const val DELITO_FECHA_HECHO = "FECHA_HECHO"
    const val DELITO_CALLE_DELITO = "CALLE DELITO"
    const val DELITO_NUMERO_CALLE_DELITO = "NUMERO CALLE DELITO"
    const val DELITO_NUMERO_BLOCK_DELITO = "NUMERO BLOCK DELITO"
    const val DELITO_NUMERO_DEPTO_DELITO = "NUMERO DEPTO DELITO"
    const val DELITO_POBLACION_DELITO = "POBLACION DELITO"
    const val DELITO_COMUNA_DELITO = "COMUNA DELITO"

    //val SQLITE_DATE_FORMAT = "YYYY-MM-DD HH:MM:SS.SSS" - suggested format but it doesn't work correct, so I use below one
    val SQLITE_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS"
    val SHORT_SQLITE_DATE_FORMAT = "yyyy-MM-dd"
    val DATE_FORMATS =
        listOf<String>(
            //SQLite strftime fun time strings
//            "YYYY-MM-DD",
//            "YYYY-MM-DD HH:MM",
//            "YYYY-MM-DD HH:MM:SS",
//            "YYYY-MM-DD HH:MM:SS.SSS",//SQLITE_DATE_FORMAT to save in DB
//            "YYYY-MM-DDTHH:MM",
//            "YYYY-MM-DDTHH:MM:SS",
//            "YYYY-MM-DDTHH:MM:SS.SSS",
//            "HH:MM",
//            "HH:MM:SS",
//            "HH:MM:SS.SSS",

            "yyyy-MM-dd HH:mm:ss.SSS",        // 2020-04-23 15:01:00.234
            "yyyy-MM-dd",                     // 2020-04-23

            //other time strings
            "dd/MM/yyyy HH:mm:ss",            // 14/01/2014 15:01:00
            "dd/MM/yy HH:mm:ss",              // 14/01/14 15:01:00
            "dd/MM/yyyy",                     // 21/11/1999
            "dd/MM/yy h:mm a",                // 22/03/99 5:06 AM
            "dd/MM/yyyy h:mm a",              // 22/03/1999 5:06 AM

            "M/d/yy h:mm a",                  // 3/22/99 5:06 AM
            "MM/dd/yy h:mm a",                // 03/22/99 5:06 AM
            "MM/dd/yy",                       // 03/24/99
            "MM/dd/yyyy",                     // 03/24/1999

            "yyyy_MM_dd_HH_mm",               // 2020_03_12_23_59 for name of photo
            "yyyy, MM, dd, HH, mm, ss",       // 2018,05,15,16,13,22

            "EEEE, MMM d, yyyy",              // Monday, Mar 4, 2013
            "EEEE, MMMM d, yyyy 'at' h:mm a", // Wednesday, September 4, 2013 at 5:07 PM
            "EEEE, MMMM d, yyyy 'at' hh:mm a",// Wednesday, September 4, 2013 at 05:07 PM

            "h:mma yyyy/MM/dd",                // 2:50pm 2018/01/10
            "dd.MM.yyyy",                      // 21.11.1999
            "dd.MM.yy"                         // 21.11.99
        )

//    val DATE_FORMATS_EXAMPLES =
//        listOf<String>(
//            "2020-04-23 15:01:00.234",        //yyyy-MM-dd HH:mm:ss.SSS
//            "2020-04-23",                     //  yyyy-MM-dd
//
//            //other time strings
//            "23/04/2020 15:01:00",            //  dd/MM/yyyy HH:mm:ss
//            "23/04/2020",                     //  dd/MM/yyyy
//            "23/04/20 5:06 AM",               //  dd/MM/yy h:mm a
//            "22/04/2020 5:06 AM",             //  dd/MM/yyyy h:mm a
//
//            "4/23/20 5:06 AM",                //  M/d/yy h:mm a
//            "04/23/20 5:06 AM",               //  MM/dd/yy h:mm a
//            "04/23/20",                       //  MM/dd/yy
//            "04/23/2020",                     //  MM/dd/yyyy
//
//            "2020_04_23_23_59",               //  yyyy_MM_dd_HH_mm for name of photo
//            "2020,04,23,16,13,22",            //  yyyy, MM, dd, HH, mm, ss
//
//            "Thursday, Apr 23, 2020",        //  EEEE, MMM d, yyyy
//            "Thursday, April 23, 2020 at 5:07 PM", //  EEEE, MMMM d, yyyy 'at' h:mm a
//            "Thursday, April 23, 2020 at 05:07 PM",// EEEE, MMMM d, yyyy 'at' hh:mm a
//
//            "2:50pm 2020/04/23",              //   h:mma yyyy/MM/dd
//            "23.04.2020",                     //  dd.MM.yyyy
//            "23.04.20"                        //  dd.MM.yy
//        )


    const val COMMA_DELIMITER = "|,|,| "//to avoid cases when columnValue contains simple ","
    const val INSIDE_COMMA_DELIMITER = "|,,|,,| "
    const val EQUAL_DELIMITER = "|=|=| "//to avoid cases when columnValue contains simple "="

    //table types
    const val TABLE_HEADER = "table_header"
    const val TABLE_BOTTOM = "table_bottom"
    const val TABLE_COUNT = "table_count"

    //table bottom types
    const val PERCENT_CHANGE_MONTH = "percent_change_month"
    //const val PERCENT_CHANGE_DAY = "percent_change_day"
    const val PREDICTION_MONTH = "prediction_month"
    const val PREDICTION_DAY = "prediction_day"

    // prediction's errors
    const val ERROR_PREDICTION = "error_prediction"
    const val ERROR_UNEXPECTED = "error_unexpected"
    const val ERROR_NO_YEARS_FOR_MONTH_AS_BASE = "error_no_years_for_month_as_base"
    const val ERROR_NO_YEARS_FOR_MONTH_TO_PREDICT = "error_no_years_for_month_to_predict"

}