/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.se2.wecalevent.util;

/**
 *
 * @author Mert Ergun <mert.rgun@gmail.com>
 */
public class HelperMethods {
    /**
     * This method edits the name string like below:
     * mErT jR eRgUn -> Mert Jr Ergun
     * @param name The string that is going to be edited
     * @return Resulting String
     */
    public static String prettifyName(String name) {
        //Seperate words
        String[] names = name.split(" ");
        String result = "";
        for (String ele : names) {
            if (ele.length() > 0)  {
                //merge the seperated words
                result += ele.substring(0,1).toUpperCase() + ele.substring(1).toLowerCase();
            }
        }
        //return resulting string
        return result;
    }
}
