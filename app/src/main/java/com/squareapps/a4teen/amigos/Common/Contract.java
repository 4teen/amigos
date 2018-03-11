package com.squareapps.a4teen.amigos.Common;

public class Contract {

    public static final String TABLE_CONTACTS = "my_contacts";
    public static final String MESSAGE = "message";
    public static final String TIMESTAMP = "timeStamp";


    public static final String STUDENTS = "students";
    public static final String USERS = "users";
    public static final String MEMBERS = "members";

    public static final String MESSAGES = "messages";
    public static final String CLASSES = "classes";
    public static final String GROUPS = "groups";
    public static final String MEDIA = "media";


    public static final String COURSES = "courses";
    public static final String SCHOOLS = "schools";
    public static final String CONTACTS = "contacts";
    public static final String USERS_IN_APP = "usersInApp";
    public static final String UNIQUE_ID = "uniqueId";
    public static final String OUTPUT = "output";

    public static final String NOTIFICATIONS = "notifications";
    public static final String ACCESS_TOKEN = "access_xyz";
    public static final String NEW_MESSAGE_NOTIFICATION = "newMessageNotification";
    public static final String GROUP_INVITATIONS = "group_invitations";
    public static final String PENDING_NUM = "pendingNum";
    public static final String PATH = "path";
    public static final String IMAGE_URL = "imageUrl";

    private Contract() {

    }

    public interface User {
        String NAME = "name";
        String AVATAR_URL = "avatarUrl";
        String PHOTO_URL = "photoUrl";
        String PHONE_NUMBER = "phoneNumber";
        String BIRTHDATE = "birthdate";
        String GENDER = "gender";
        String EMAIL = "email";

    }

    public interface Course {
        String COURSE_CODE = "courseCode";
        String COURSE_ID = "courseID";
        String TITLE = "title";
        String DEPARTMENT = "department";
        String COLLEGE = "college";
        String PREFIX = "prefix";
        String NUMBER = "number";
        String CREDITS = "credits";
        String DESCRIPTION = "description";
        String CREDIT_HOURS = "creditHours";
        String PREREQUISITES = "prerequisites";
        String COREQUISITES = "corequisites";
        String COURSE_REQUIREMENTS = "courseRequirements";
        String CO_PREREQUISITES = "coPrerequisites";

    }

    public interface Group {
        String GROUP_ID = "groupId";
        String GROUP_NAME = "groupName";
    }

}
