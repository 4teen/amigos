package com.square.apps.amigos;

import android.net.Uri;

import java.util.HashMap;

public class Contract {

    //DATABASE FINAL STRINGS
    public static final int    DATABASE_VERSION         = 1;
    public static final String DATABASE_NAME            = "databaseAmigos";
    public static final String PRIMARY_KEY              = "_id";
    public static final String NAME                     = "name";
    public static final String EMAIL                    = "email";
    //table friends unique strings
    public static final String TABLE_FRIENDS            = "friends";
    public static final String COUNT                    = "count";
    //Table user unique strings
    public static final String TABLE_PROFILE            = "profile";
    public static final String STUDENT_ID               = "StudentID";
    public static final String UID                      = "uid";
    public static final String CREATED_AT               = "created_at";
    public static final String PROFILE_IMAGE            = "ProfilePic";
    public static final String UNIQUE_ID                = "unique_id";
    public static final String PROFILE_PIC              = "ProfilePic";
    public static final String REGID                    = "gcm_regid";
    //Table MESSAGES unique strings
    public static final String TABLE_MESSAGES           = "messages";
    public static final String MESSAGE                  = "message";
    public static final String FROM                     = "col_from";
    public static final String TO                       = "col_to";
    public static final String AT                       = "col_at";
    //Table Courses unique strings
    public static final String TABLE_COURSES            = "courses";
    public static final String COURSE_ID                = "CourseID";
    public static final String COLLEGE                  = "College";
    public static final String DEPARTMENT               = "Department";
    public static final String CRN                      = "CRN";
    public static final String SUBJECT_CRS              = "SubjectCRS";
    public static final String SECTION                  = "Section";
    public static final String TITLE                    = "Title";
    public static final String BUILDING                 = "Building";
    public static final String ROOM                     = "Room";
    public static final String INSTRUCTOR               = "Instructor";
    public static final String CAMPUS                   = "Campus";
    public static final String TIME                     = "Time";
    public static final String DAYS                     = "Days";
    //Table Friend_request unique strings
    public static final String TABLE_FRIEND_REQUESTS    = "friend_request_table";
    //Table pending_friends unique strings
    public static final String TABLE_PENDING_FRIENDS    = "pending_request_table";
    //Table Courses_buffer unique strings
    public static final String TABLE_COURSES_BUFFER     = "courses_buffer";
    public static final String TABLE_FRIENDS_BUFFER     = "friends_buffer";
    //table All_friends unique strings
    public static final String TABLE_ALL_FRIENDS        = "all_friends";
    //Content provider Uris
    public static final Uri    CONTENT_URI_MESSAGES     = Uri.parse("content://com.square.apps.provider/message");
    public static final Uri    CONTENT_URI_PROFILE      = Uri.parse("content://com.square.apps.provider/profile");
    public static final Uri    CONTENT_URI_FRIENDS      = Uri.parse("content://com.square.apps.provider/friends");
    public static final Uri    CONTENT_URI_COURSES      = Uri.parse("content://com.square.apps.provider/courses");
    public static final Uri    CONTENT_URI_GCMUSERS     = Uri.parse("content://com.square.apps.provider/gcm_users");
    //Global Contants
    public static final String PASSWORD                 = "password";
    //PubNub
    public static final String PUB_NUB_PUBLISH_KEY      = "pub-c-f55c194c-1f1e-4e45-a41d-cad63d1a3f40";
    public static final String PUB_NUB_SUBSCRIBE_KEY    = "sub-c-24fe291a-7952-11e5-a643-02ee2ddab7fe";
    //Services Contants
    public static final String LOGIN_COMPLETE           = "login_complete";
    public static final String SEND_COMPLETE            = "send_complete";
    public static final String GETTING_COURSES_COMPLETE = "Done_getting_courses";
    public static final String SEARCH_COURSES_COMPLETE  = "Done_searching_courses";
    public static final String COUNT_UPDATE_COMPLETE    = "Count_update_complete";
    //tags
    public static final String TAG_SHOW_FRIEND_REQUESTS = "showFriendRequests";

    public static HashMap<String, String> searchFormHashMap = new HashMap<>();


    private Contract(){

    }

    public static HashMap<String, String> getHashMaps(){
        //level hashmaps
        searchFormHashMap.put("Undergraduate", "u");
        searchFormHashMap.put("Graduate", "g");

        //Department hashmaps
        searchFormHashMap.put("","");
        searchFormHashMap.put("Accounting (BA)", "ACC");
        searchFormHashMap.put("Accounting (BM)", "ACC");
        searchFormHashMap.put("Accounting (BP)", "ACC");
        searchFormHashMap.put("Aerospace Studies - Air Force ROTC (US)", "AFR");
        searchFormHashMap.put("Africana Studies (AS)", "AFA");
        searchFormHashMap.put("Aging Studies (BC)", "GEY");
        searchFormHashMap.put("Anthropology (AP)", "ANT");
        searchFormHashMap.put("Anthropology (AS)", "ANT");
        searchFormHashMap.put("Architecture and Community Design (FA)", "ARC");
        searchFormHashMap.put("Art and Art History (AP)", "ART");
        searchFormHashMap.put("Art and Art History (FA)", "ART");
        searchFormHashMap.put("Biology (AP)", "BIO");
        searchFormHashMap.put("Biology (AS)", "BIO");
        searchFormHashMap.put("Biology (MM)", "BIO");
        searchFormHashMap.put("Biology - Cell, Microbiology and Molecular Biology (AS)", "BCM");
        searchFormHashMap.put("Biology - Integrative (AS)", "BIN");
        searchFormHashMap.put("Business Administration (BA)", "GBA");
        searchFormHashMap.put("Business Administration (BP)", "GBA");
        searchFormHashMap.put("Chemical &amp; Biomedical Engineering (EN)", "ECH");
        searchFormHashMap.put("Chemistry (AS)", "CHM");
        searchFormHashMap.put("Chemistry (AP)", "CHM");
        searchFormHashMap.put("Chemistry (MM)", "CHM");
        searchFormHashMap.put("Child and Family Studies (BC)", "CFS");
        searchFormHashMap.put("Childhood Education and Literacy Studies (EP)", "EDR");
        searchFormHashMap.put("Childhood/Lang. Arts/Reading (LM)", "EDR");
        searchFormHashMap.put("Civil and Environmental Engineering (EN)", "EGX");
        searchFormHashMap.put("Communication (AP)", "SPE");
        searchFormHashMap.put("Communication (AS)", "SPE");
        searchFormHashMap.put("Communication Sciences and Disorders (BC)", "CSD");
        searchFormHashMap.put("Community and Family Health (PH)", "CFH");
        searchFormHashMap.put("Community Experiential Learning (AS)", "CEL");
        searchFormHashMap.put("Community Mental Health (BC)", "FMH");
        searchFormHashMap.put("Computer Science and Engineering (EN)", "ESB");
        searchFormHashMap.put("Criminology (AP)", "CJP");
        searchFormHashMap.put("Criminology (BC)", "CJP");
        searchFormHashMap.put("Criminology (LM)", "CJP");
        searchFormHashMap.put("Dance (FA)", "DAN");
        searchFormHashMap.put("Dean\'s Office (EP)", "DEA");
        searchFormHashMap.put("Dean\'s Office (AP)", "DEA");
        searchFormHashMap.put("Dean\'s Office (BP)", "DEA");
        searchFormHashMap.put("Economics (AS)", "ECN");
        searchFormHashMap.put("Educational and Psychological Studies (ED)", "EAP");
        searchFormHashMap.put("Educational Leadership (EP)", "ELS");
        searchFormHashMap.put("Educational Measurement and Research (EP)", "EDQ");
        searchFormHashMap.put("Electrical Engineering (EN)", "EGE");
        searchFormHashMap.put("Elementary Education (EP)", "EDE");
        searchFormHashMap.put("Elementary Education (LM)", "EDE");
        searchFormHashMap.put("English (AP)", "ENG");
        searchFormHashMap.put("English (AS)", "ENG");
        searchFormHashMap.put("English (LM)", "ENG");
        searchFormHashMap.put("Environmental and Occupational Health (PH)", "EOH");
        searchFormHashMap.put("Epidemiology and Biostatistics (PH)", "EPB");
        searchFormHashMap.put("Finance (BA)", "FIN");
        searchFormHashMap.put("Finance (BM)", "FIN");
        searchFormHashMap.put("Finance (BP)", "FIN");
        searchFormHashMap.put("Foreign Language Education (LM)", "EDXM");
        searchFormHashMap.put("Geography (AP)", "GPY");
        searchFormHashMap.put("Geography &amp; Environmental Science and Policy (AP)", "GEP");
        searchFormHashMap.put("Geosciences (AS)", "SGS");
        searchFormHashMap.put("Global Health (PH)", "GLO");
        searchFormHashMap.put("Global Sustainability (CS)", "DEA");
        searchFormHashMap.put("Government &amp; International Affairs (AS)", "GIA");
        searchFormHashMap.put("Graduate School (RX)", "GRS");
        searchFormHashMap.put("Graduate School Department (GS)", "GRS");
        searchFormHashMap.put("Health Policy and Management (PH)", "HPM");
        searchFormHashMap.put("History (AP)", "HTY");
        searchFormHashMap.put("History (AS)", "HTY");
        searchFormHashMap.put("Honors (HC)", "HON");
        searchFormHashMap.put("Hospitality Management (HM)", "HRM");
        searchFormHashMap.put("Humanities (LM)", "HUM");
        searchFormHashMap.put("Humanities &amp; Cultural Studies (AS)", "HCS");
        searchFormHashMap.put("Industrial and Management Systems Engineering (EN)", "EGS");
        searchFormHashMap.put("Information Systems and Decision Sciences (BA)", "QMB");
        searchFormHashMap.put("Information Systems and Decision Sciences (BM)", "QMB");
        searchFormHashMap.put("Information Technology (EN)", "EIT");
        searchFormHashMap.put("Information Technology (BM)", "EIT");
        searchFormHashMap.put("Interdisciplinary Arts and Sciences (AS)", "IAS");
        searchFormHashMap.put("Interdisciplinary Engineering (EN)", "EGB");
        searchFormHashMap.put("Interdisciplinary Fine Arts (FA)", "FAI");
        searchFormHashMap.put("Interdisciplinary Social Sciences (AP)", "ISS");
        searchFormHashMap.put("Interdisciplinary Social Sciences (AS)", "ISS");
        searchFormHashMap.put("Interdisciplinary Social Sciences (LM)", "ISS");
        searchFormHashMap.put("Interdisciplinary Studies (AS)", "IDS");
        searchFormHashMap.put("Journalism and Media Studies (AP)", "JMS");
        searchFormHashMap.put("Leadership Studies (US)", "LDR");
        searchFormHashMap.put("Leadership Studies (AP)", "LDR");
        searchFormHashMap.put("Leadership Studies (LM)", "LDR");
        searchFormHashMap.put("Leadership, Counseling, Adult, Career, and Higher Education (ED)", "LCA");
        searchFormHashMap.put("Management (BA)", "MAN");
        searchFormHashMap.put("Management (BP)", "MAN");
        searchFormHashMap.put("Management (BM)", "MAN");
        searchFormHashMap.put("Marine Science (MS)", "MSC");
        searchFormHashMap.put("Marketing (BA)", "MKT");
        searchFormHashMap.put("Marketing (BP)", "MKT");
        searchFormHashMap.put("Mass Communications (AP)", "COM");
        searchFormHashMap.put("Mass Communications (AS)", "MCM");
        searchFormHashMap.put("Mathematics &amp; Statistics (AS)", "MTH");
        searchFormHashMap.put("Mechanical Engineering (EN)", "EGR");
        searchFormHashMap.put("Medical Sciences (MD)", "MSG");
        searchFormHashMap.put("Mental Health Law &amp; Policy (BC)", "MHL");
        searchFormHashMap.put("Military Science - Army ROTC (US)", "MIS");
        searchFormHashMap.put("Music/Music Education (FA)", "MUS");
        searchFormHashMap.put("Naval Science - Naval ROTC (US)", "NVY");
        searchFormHashMap.put("Nursing (NR)", "NUR");
        searchFormHashMap.put("Orthopaedics &amp; Sports Medicine (MD)", "ATH");
        searchFormHashMap.put("Pharmacy (RX)", "PHA");
        searchFormHashMap.put("Philosophy (AS)", "PHI");
        searchFormHashMap.put("Physical Therapy and Rehabilitation Sciences (MD)", "PHT");
        searchFormHashMap.put("Physics (AS)", "PHY");
        searchFormHashMap.put("Political Science (LM)", "POL");
        searchFormHashMap.put("Psych &amp; Social Foundation (LM)", "EAP");
        searchFormHashMap.put("Psychological and Social Foundations (EP)", "EDF");
        searchFormHashMap.put("Psychology (AP)", "PSY");
        searchFormHashMap.put("Psychology (AS)", "PSY");
        searchFormHashMap.put("Psychology (LM)", "PSY");
        searchFormHashMap.put("Psychology (MM)", "PSY");
        searchFormHashMap.put("Public Health (PH)", "PHC");
        searchFormHashMap.put("Reading &amp; Learning Strategies (US)", "RLS");
        searchFormHashMap.put("Rehabilitation &amp; Mental Health Counseling (BC)", "REH");
        searchFormHashMap.put("Religious Studies (AS)", "REL");
        searchFormHashMap.put("School of Information (AS)", "LIS");
        searchFormHashMap.put("School of Public Affairs (AS)", "SPF");
        searchFormHashMap.put("Secondary Education (EP)", "EDI");
        searchFormHashMap.put("Secondary Education (LM)", "EDI");
        searchFormHashMap.put("Social Work (AP)", "SOK");
        searchFormHashMap.put("Social Work (BC)", "SOK");
        searchFormHashMap.put("Society, Culture and Language (AP)", "SCL");
        searchFormHashMap.put("Sociology (AS)", "SOC");
        searchFormHashMap.put("Sociology (LM)", "SOC");
        searchFormHashMap.put("Special Education (EP)", "EDS");
        searchFormHashMap.put("Student Leadership (LM)", "STL");
        searchFormHashMap.put("Teaching and Learning (ED)", "TAL");
        searchFormHashMap.put("Theatre (FA)", "TAR");
        searchFormHashMap.put("Undergraduate Research (US)", "DEA");
        searchFormHashMap.put("Undergraduate Studies (US)", "DEA");
        searchFormHashMap.put("University Experience (US)", "DEA");
        searchFormHashMap.put("Verbal &amp; Visual Arts (AP)", "VVA");
        searchFormHashMap.put("Women\'s and Gender Studies (AS)", "WST");
        searchFormHashMap.put("World Languages (AP)", "WLE");
        searchFormHashMap.put("World Languages (AS)", "WLE");
        searchFormHashMap.put("World Languages (LM)", "WLE");

        //college Hashmaps
        searchFormHashMap.put("", "");
        searchFormHashMap.put("Arts &amp; Sciences (USF)", "AS");
        searchFormHashMap.put("Arts &amp; Sciences (USF St. Petersburg)", "AP");
        searchFormHashMap.put("Behavioral &amp; Community Sciences (USF)", "BC");
        searchFormHashMap.put("Business (USF)", "BA");
        searchFormHashMap.put("Business (USF Sarasota-Manatee)", "BM");
        searchFormHashMap.put("Business (USF St. Petersburg)", "BP");
        searchFormHashMap.put("College of Pharmacy (USF)", "RX");
        searchFormHashMap.put("Education (USF)", "ED");
        searchFormHashMap.put("Education (USF St. Petersburg)", "EP");
        searchFormHashMap.put("Engineering (USF)", "EN");
        searchFormHashMap.put("Global Sustainability (USF)", "CS");
        searchFormHashMap.put("Graduate School (USF)", "GS");
        searchFormHashMap.put("Honors College (USF)", "HC");
        searchFormHashMap.put("Hospitality &amp; Tourism Leadership (USF Sarasota-Manatee)", "HM");
        searchFormHashMap.put("Liberal Arts &amp; Social Sciences (USF Sarasota-Manatee)", "LM");
        searchFormHashMap.put("Marine Science (USF)", "MS");
        searchFormHashMap.put("Medicine (USF)", "MD");
        searchFormHashMap.put("Nursing (USF)", "NR");
        searchFormHashMap.put("Public Health (USF)", "PH");
        searchFormHashMap.put("Science &amp; Mathematics (USF Sarasota-Manatee)", "MM");
        searchFormHashMap.put("The Arts (USF)", "FA");
        searchFormHashMap.put("Undergraduate Studies (USF)", "US");
        return searchFormHashMap;

    }

}
