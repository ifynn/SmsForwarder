package com.fynn.smsforwarder.model.consts;

import org.fynn.appu.common.Immutable;

/**
 * @author lifs
 * @date 2018/7/2
 */
public final class Sim extends Immutable {

    public static final String CONTENT_SIM_INFO = "content://telephony/siminfo";
    public static final String CONTENT_ITEM_INFO = "content://mms-sms/itemInfo";

    public static final String COLUMN_SIM_ID = "sim_id";
    public static final String COLUMN_SUB_ID = "sub_id";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DISPLAY_NAME = "display_name";
    public static final String COLUMN_NUMBER = "number";
    public static final String COLUMN_SLOT_INDEX = "network_type";
}
