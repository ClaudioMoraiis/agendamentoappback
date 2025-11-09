package com.example.demo.Util;

import javax.swing.text.MaskFormatter;
import java.text.ParseException;

public abstract class Util {
    public static String formatarTelefone(String mNumero){
        try {
            mNumero = mNumero.replaceAll("[-/.()\\s]", "");
            MaskFormatter mMascara = new MaskFormatter("(##)#####-####");
            mMascara.setValueContainsLiteralCharacters(false);

            return mMascara.valueToString(mNumero);
        } catch (ParseException e){
            e.printStackTrace();
            return mNumero;
        }
    };

    public static Boolean validaTelefone(String mNumero){
        mNumero = mNumero.trim().replaceAll("[-/.()]", "");
        if (mNumero.length() != 11){
            return false;
        }

        return true;
    }

    public static String formatarCpf(String mCpf){
        mCpf = mCpf.replaceAll("[./-]", "");
        return mCpf.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
    }
}
