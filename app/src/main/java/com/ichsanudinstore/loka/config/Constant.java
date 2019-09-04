package com.ichsanudinstore.loka.config;

/**
 * @author Ichsanudin_Chairin
 * @since Wednesday 8/14/2019 6:43 PM
 */

public class Constant {
    public enum SharedPreferenceKey {
        SESSION_ID,
        EMAIL,
        NAME,
        LANGUAGE,
        USER_ID,
        COMPANY_ID,
        ROLE_NAME,
        ROLE_ID,
        BASE_URL
    }

    public static class Application {
        public static final String NAME = "Vireo";
        public static final String SALT = "72e4425c484016c95677d1a2513681ff8e2b2459b11e68c8b67cc7b7fe60c422b629eb45d1a5b236c3df0031860c98f4b0f58c2497212ee20d58a833b9a3ea1d";
        public static final boolean PRODUCTION = false;
        public static final boolean ACTIVATE_HANDLER = false;
    }

    public enum Gender {
        pria,
        wanita,
        male,
        female
    }

    public enum Privileges {
        admin,
        owner,
        keeper
    }

    public static class URL {
        public static final String BASE_PRODUCTION = "";
        //                public static final String BASE_DEVELOPMENT = "http://192.168.30.12/";
//        public static final String BASE_DEVELOPMENT = "http://192.168.137.1/";
        public static final String BASE_DEVELOPMENT = "http://192.168.50.164/";

        public static final String LOGIN = "learn/loka/api/user/login";
        public static final String CREATE_UPDATE_PROFILE = "learn/loka/api/user/create_update_profile";
        public static final String UPDATE_PASSWORD = "learn/loka/api/user/update_password";
        public static final String FORGET_PASSWORD = "learn/loka/api/user/forget_password";
        public static final String DELETE_PROFILE = "learn/loka/api/user/delete";
        public static final String READ_PROFILE = "learn/loka/api/user/read_profile";
        public static final String READ_KEEPER = "learn/loka/api/user/read_keeper";
        public static final String ACTIVATION_PROFILE = "learn/loka/api/user/activation_profile";

        public static final String READ_CATEGORY = "learn/loka/api/category/read_category";
        public static final String DELETE_CATEGORY = "learn/loka/api/category/delete";
        public static final String CREATE_UPDATE_CATEGORY = "learn/loka/api/category/create_update_category";

        public static final String READ_OFFICE = "learn/loka/api/office/read_office";
        public static final String DELETE_OFFICE = "learn/loka/api/office/delete";
        public static final String CREATE_UPDATE_OFFICE = "learn/loka/api/office/create_update_office";

        public static final String READ_SEAT = "learn/loka/api/seat/read_seat";
        public static final String DELETE_SEAT = "learn/loka/api/seat/delete";
        public static final String CREATE_UPDATE_SEAT = "learn/loka/api/seat/create_update_seat";
    }
}
