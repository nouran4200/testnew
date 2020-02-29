package com.iti.chat.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterValidation {

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
        if (name.trim().length() == 0 || !name.matches("([a-zA-Z]*)")) {
            flag = false;
        }
        return flag;
    }

    public boolean checkPass(String pass) {
        boolean flag = true;
        if (pass.trim().length() == 0) {
            return false;
        }
        return flag;
    }

}
