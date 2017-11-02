package com.alisure.entity;

/**
 * ���ص�״̬��
 */
public class Status {
    public final static int Status_Error = 0;
    public final static int Status_OK = 1;
    /** ���Ϊ�� */
    public final static int Status_NULL_Result = 2;
    /** �������� */
    public final static int Status_Parameter_Error = 3;
    /* loginSession ��Ч */
    public final static int LoginException = 4;
    /* ��ַ ��Ч */
    public final static int Status_Path_Error = 5;


    public final static int RuntimeException = 10;
    public final static int NullPointerException = 11;
    public final static int ClassCastException = 12;
    public final static int IOException = 13;
    public final static int NoSuchMethodException = 14;
    public final static int IndexOutOfBoundsException = 15;
    public final static int HttpMessageNotReadableException = 16;
    public final static int TypeMismatchException = 17;
    public final static int MissingServletRequestParameterException = 18;
    public final static int HttpRequestMethodNotSupportedException = 19;
    public final static int HttpMediaTypeNotAcceptableException = 20;
    public final static int ConversionNotSupportedException = 21;
}
