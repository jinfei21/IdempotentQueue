package com.yjfei.queue.common;/** * 绫籘ypeEnum.java鐨勫疄鐜版弿杩帮細浠诲姟鎵ц椤哄簭鏋氫妇 *  * @author finixfei 2015骞�10鏈�29鏃� 涓嬪崍5:34:22 */public enum DirectionEnum {    Forward('F', "鍏堢埗鍚庡瓙"),    Converse('C', "鍏堝瓙鍚庣埗");    private char   code;    private String text;    private DirectionEnum(char code, String text) {        this.code = code;        this.text = text;    }    public static DirectionEnum getDirectionEnum(char code) {        for (DirectionEnum en : DirectionEnum.values()) {            if (en.code == code) {                return en;            }        }        return Forward;    }    public char getCode() {        return code;    }    public void setCode(char code) {        this.code = code;    }    public String getText() {        return text;    }    public void setText(String text) {        this.text = text;    }}