package com.ghiar.interfaces;


public interface Listeners {

    interface LoginListener {
        void validate();

        void showCountryDialog();
    }

    interface TransFerListener {
        void checkData(String amount);
    }
    interface SkipListener
    {
        void skip();
    }
    interface CreateAccountListener
    {
        void createNewAccount();
    }


    interface SignUpListener
    {
        void checkDataValid();

    }
    interface EditprofileListener
    {
        void Editprofile(String name);
        void Editprofile(String englishname, String arabicname);


    }

    interface BackListener
    {
        void back();
    }


    interface SettingActions
    {
        void commission();
        void shop();
        void contactUs();
        void terms();
        void aboutApp();
        void logout();
        void share();
        void rateApp();
        void arLang();
        void enLang();


    }

    interface AddRequiredListener{

        void openSheet();
        void closeSheet();
        void checkReadPermission();
        void checkCameraPermission();
    }



}
