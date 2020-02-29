package com.iti.chat.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterValidation {
    public static final int INVALID = -1, VALID = 1, EMPTY = 0;


    public boolean checkPhone(String phone) {
        boolean flag = true;
        if (phone.trim().length() == 0) {
            flag = false;
        } else {
            if (phone.startsWith("+2"))
                phone = phone.replace("+2", "");
            else if (phone.startsWith("002"))
                phone = phone.replace("002", "");
            Pattern p = Pattern.compile("^(01)[0-9]{9}");
            Matcher m = p.matcher(phone);
            if (!m.matches())
                flag = false;
        }
        return flag;
    }

    public boolean checkName(String name) {
        boolean flag = true;
        if (!name.matches("(^[a-zA-Z]{2,20}$)")) {
            flag = false;
        }
        return flag;
    }
    public int checkEmail(String email) {
        if (email.isEmpty()) {
            return EMPTY; //enter your Email
        } else {
            if (!email.matches("^([\\w-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([\\w-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$"))
                return INVALID;//Enter a valid email address
        }
        return VALID;//match
    }

    public boolean checkPass(String pass) {
        boolean flag = true;
        if (pass.trim().length() == 0 || pass.length()>15) {
            return false;
        }
        return flag;
    }

}
