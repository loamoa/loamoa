package com.loamoa.loamoa.utility;

/**
 * 유틸리티 메소드를 저장하는 클래스
 */
public class Utills {
    /**
     * 문자열로 이루어진 가격("xx,xxx.x") 을 float형 가격으로 변환한다.
     * @param str float형으로 변환하고자 하는 문자열, xx,xxx.x 혹은 xx,xxx 혹은 xxx와 같은 형식의 입력
     * @return str을 float형으로 변환하여 리턴한다.
     */
    public static float toFloatNumber(String str) {
        float floatNumber = 0.0F;
        String splitedStr[] = str.split("\\."); // 점으로 구분
        if(splitedStr.length == 1) {
            // 가격이 5000, 6000과 같이 정수일 경우
            floatNumber += Float.parseFloat(splitedStr[0].replace(",",""));
        } else if(splitedStr.length == 2) {
            // 가격이 1.1, 1810.2와 같이 실수일 경우
            floatNumber += Float.parseFloat(splitedStr[0].replace(",",""));
            floatNumber += Float.parseFloat(splitedStr[1])/10;
        } else {
            // 가격이 이상하게 나눠짐
            System.out.println("[ERROR] toFloatNumber : split error.");
        }
        return floatNumber;
    }


}
