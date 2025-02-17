package com.huawei.android.app.admin;

import android.content.ComponentName;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import huawei.android.app.admin.HwDevicePolicyManagerEx;

public class DeviceTelephonyManager {
    private static final int CARRIER_MAX_LENGTH = 10;
    private static final String CHANGE_PIN_CODE = "change_pin_code";
    public static final String DAY = "day_mode";
    public static final int DAY_MODE = 1;
    public static final String DAY_MODE_TIME = "day_mode_time";
    private static final String DEFAULT_MAIN_SLOT_CARRIER = "default_main_slot_carrier";
    private static final String DISABLE_AIR_PLANE_MODE = "disable_airplane_mode";
    private static final String DISABLE_DATA = "disable-data";
    private static final String DISABLE_PUSH = "disable-push";
    private static final String DISABLE_SUB = "disable-sub";
    private static final String DISABLE_SYNC = "disable-sync";
    public static final String INCOMING_DAY_LIMIT = "incoming_day_limit";
    public static final String INCOMING_MONTH_LIMIT = "incoming_month_limit";
    private static final String INCOMING_SMS_EXCEPTION_PATTERN = "incoming_sms_exception_pattern";
    private static final String INCOMING_SMS_RESTRICTION_PATTERN = "incoming_sms_restriction_pattern";
    public static final String INCOMING_WEEK_LIMIT = "incoming_week_limit";
    public static final String LIMIT_OF_DAY = "limit_number_day";
    public static final String LIMIT_OF_MONTH = "limit_number_month";
    public static final String LIMIT_OF_WEEK = "limit_number_week";
    private static final int MAX_PIN_CODE_LEN = 8;
    private static final int MIN_PIN_CODE_LEN = 4;
    public static final String MONTH = "month_mode";
    public static final int MONTH_MODE = 3;
    public static final String MONTH_MODE_TIME = "month_mode_time";
    public static final String OUTGOING_DAY_LIMIT = "outgoing_day_limit";
    public static final String OUTGOING_MONTH_LIMIT = "outgoing_month_limit";
    private static final String OUTGOING_SMS_EXCEPTION_PATTERN = "outgoing_sms_exception_pattern";
    private static final String OUTGOING_SMS_RESTRICTION_PATTERN = "outgoing_sms_restriction_pattern";
    public static final String OUTGOING_WEEK_LIMIT = "outgoing_week_limit";
    public static final String POLICY_KEY = "value";
    private static final String SET_PIN_LOCK = "set_pin_lock";
    private static final int SIM_NUM = TelephonyManager.getDefault().getPhoneCount();
    private static final String SUB_STATE = "substate";
    private static final String TAG = "DeviceTelephonyManager";
    public static final String TIME_MODE = "time_mode";
    public static final String WEEK = "week_mode";
    public static final int WEEK_MODE = 2;
    public static final String WEEK_MODE_TIME = "week_mode_time";
    private HwDevicePolicyManagerEx mDpm = new HwDevicePolicyManagerEx();

    public boolean setSlot2Disabled(ComponentName admin, boolean isDisabled) {
        Log.d(TAG, "set dual sim active: " + isDisabled);
        Bundle bundle = new Bundle();
        bundle.putBoolean("value", isDisabled);
        return this.mDpm.setPolicy(admin, DISABLE_SUB, bundle);
    }

    public boolean isSlot2Disabled(ComponentName admin) {
        Bundle bundle = this.mDpm.getPolicy(admin, DISABLE_SUB);
        if (bundle == null) {
            return false;
        }
        boolean isDisabled = bundle.getBoolean("value");
        Log.d(TAG, "get dual sim active: " + isDisabled);
        return isDisabled;
    }

    public boolean setSlot2DataConnectivityDisabled(ComponentName admin, boolean isDisabled) {
        Log.d(TAG, "set sub2 data activ: " + isDisabled);
        Bundle bundle = new Bundle();
        bundle.putBoolean("value", isDisabled);
        return this.mDpm.setPolicy(admin, DISABLE_DATA, bundle);
    }

    public boolean isSlot2DataConnectivityDisabled(ComponentName admin) {
        Bundle bundle = this.mDpm.getPolicy(admin, DISABLE_DATA);
        if (bundle == null) {
            return false;
        }
        boolean isDisabled = bundle.getBoolean("value");
        Log.d(TAG, "get sub2 data activ: " + isDisabled);
        return isDisabled;
    }

    public boolean setAirplaneModeDisabled(ComponentName admin, boolean isDisabled) {
        Log.d(TAG, "set airplane mode: " + isDisabled);
        Bundle bundle = new Bundle();
        bundle.putBoolean("value", isDisabled);
        return this.mDpm.setPolicy(admin, DISABLE_AIR_PLANE_MODE, bundle);
    }

    public boolean isAirplaneModeDisabled(ComponentName admin) {
        Bundle bundle = this.mDpm.getPolicy(admin, DISABLE_AIR_PLANE_MODE);
        if (bundle == null) {
            return false;
        }
        boolean isDisabled = bundle.getBoolean("value");
        Log.d(TAG, "get airplane mode: " + isDisabled);
        return isDisabled;
    }

    public boolean setRoamingSyncDisabled(ComponentName admin, boolean isDisabled) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("value", isDisabled);
        Log.d(TAG, "setRoamingSyncDisabled: " + isDisabled);
        return this.mDpm.setPolicy(admin, DISABLE_SYNC, bundle);
    }

    public boolean isRoamingSyncDisabled(ComponentName admin) {
        Bundle bundle = this.mDpm.getPolicy(admin, DISABLE_SYNC);
        if (bundle != null) {
            boolean isDisabled = bundle.getBoolean("value");
            Log.d(TAG, "isRoamingSyncDisabled: " + isDisabled);
            return isDisabled;
        }
        Log.d(TAG, "has not set the allow, return default false");
        return false;
    }

    public boolean setRoamingPushDisabled(ComponentName admin, boolean isDisabled) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("value", isDisabled);
        Log.d(TAG, "setRoamingPushDisabled: " + isDisabled);
        return this.mDpm.setPolicy(admin, DISABLE_PUSH, bundle);
    }

    public boolean isRoamingPushDisabled(ComponentName admin) {
        Bundle bundle = this.mDpm.getPolicy(admin, DISABLE_PUSH);
        if (bundle != null) {
            boolean isDisabled = bundle.getBoolean("value");
            Log.d(TAG, "isRoamingPushDisabled: " + isDisabled);
            return isDisabled;
        }
        Log.d(TAG, "has not set the allow, return default false");
        return false;
    }

    public boolean setSimPinLock(ComponentName admin, boolean isEnablePinLock, String pinCode, int slotId) {
        if ((slotId < 0 && slotId >= SIM_NUM) || !isValidPinCode(pinCode)) {
            return false;
        }
        Log.d(TAG, "set Pin lock enable. slotId " + slotId);
        Bundle bundle = new Bundle();
        bundle.putString("password", pinCode);
        bundle.putInt("slotId", slotId);
        bundle.putBoolean("pinLockState", isEnablePinLock);
        return this.mDpm.setPolicy(admin, SET_PIN_LOCK, bundle);
    }

    public boolean changeSimPinCode(ComponentName admin, String currentPinCode, String newPinCode, int slotId) {
        if ((slotId < 0 && slotId >= SIM_NUM) || !isValidPinCode(currentPinCode) || !isValidPinCode(newPinCode)) {
            return false;
        }
        Log.d(TAG, "change pin code. slotId " + slotId);
        Bundle bundle = new Bundle();
        bundle.putString("oldPinCode", currentPinCode);
        bundle.putString("newPinCode", newPinCode);
        bundle.putInt("slotId", slotId);
        return this.mDpm.setPolicy(admin, CHANGE_PIN_CODE, bundle);
    }

    private boolean isValidPinCode(String pinCode) {
        if (!TextUtils.isEmpty(pinCode) && pinCode.length() >= 4 && pinCode.length() <= MAX_PIN_CODE_LEN) {
            return true;
        }
        return false;
    }

    public boolean setSMSLimitation(ComponentName admin, boolean isOutgoing, int dateType, int limitNumber) {
        if (limitNumber < 0) {
            return false;
        }
        boolean isSuccess = false;
        if (dateType == 1) {
            isSuccess = saveDayMode(isOutgoing, limitNumber, admin);
        } else if (dateType == 2) {
            isSuccess = saveWeekMode(isOutgoing, limitNumber, admin);
        } else if (dateType == 3) {
            isSuccess = saveMonthMode(isOutgoing, limitNumber, admin);
        }
        Log.d(TAG, "setLimitNumOfSms: " + isSuccess);
        return isSuccess;
    }

    public boolean removeSMSLimitation(ComponentName admin, boolean isOutgoing, int dateType) {
        if (dateType == 1) {
            return removeDayMode(admin, isOutgoing);
        }
        if (dateType == 2) {
            return removeWeekMode(admin, isOutgoing);
        }
        if (dateType != 3) {
            return false;
        }
        return removeMonthMode(admin, isOutgoing);
    }

    public boolean isSMSLimitationSet(ComponentName admin, boolean isOutgoing) {
        Bundle bundleMonth = this.mDpm.getPolicy(admin, isOutgoing ? "outgoing_month_limit" : "incoming_month_limit");
        if (bundleMonth != null && bundleMonth.getString(MONTH) != null) {
            return true;
        }
        Bundle bundleWeek = this.mDpm.getPolicy(admin, isOutgoing ? "outgoing_week_limit" : "incoming_week_limit");
        if (bundleWeek != null && bundleWeek.getString(WEEK) != null) {
            return true;
        }
        Bundle bundleDay = this.mDpm.getPolicy(admin, isOutgoing ? "outgoing_day_limit" : "incoming_day_limit");
        if (bundleDay == null || bundleDay.getString(DAY) == null) {
            return false;
        }
        return true;
    }

    private boolean removeMonthMode(ComponentName admin, boolean isOutgoing) {
        String policyName;
        boolean isSuccess = false;
        if (isOutgoing) {
            policyName = "outgoing_month_limit";
        } else {
            policyName = "incoming_month_limit";
        }
        if (!TextUtils.isEmpty(policyName)) {
            isSuccess = this.mDpm.removePolicy(admin, policyName, null);
        }
        Log.d(TAG, "removeMonthMode: " + isSuccess);
        return isSuccess;
    }

    private boolean removeWeekMode(ComponentName admin, boolean isOutgoing) {
        String policyName;
        boolean isSuccess = false;
        if (isOutgoing) {
            policyName = "outgoing_week_limit";
        } else {
            policyName = "incoming_week_limit";
        }
        if (!TextUtils.isEmpty(policyName)) {
            isSuccess = this.mDpm.removePolicy(admin, policyName, null);
        }
        Log.d(TAG, "removeWeekMode: " + isSuccess);
        return isSuccess;
    }

    private boolean removeDayMode(ComponentName admin, boolean isOutgoing) {
        String policyName;
        boolean isSuccess = false;
        if (isOutgoing) {
            policyName = "outgoing_day_limit";
        } else {
            policyName = "incoming_day_limit";
        }
        if (!TextUtils.isEmpty(policyName)) {
            isSuccess = this.mDpm.removePolicy(admin, policyName, null);
        }
        Log.d(TAG, "removeDayMode: " + isSuccess);
        return isSuccess;
    }

    private boolean saveMonthMode(boolean isOutgoing, int limitNum, ComponentName admin) {
        String policyName;
        if (isOutgoing) {
            policyName = "outgoing_month_limit";
        } else {
            policyName = "incoming_month_limit";
        }
        Bundle bundle = new Bundle();
        bundle.putString(MONTH, "true");
        bundle.putString(LIMIT_OF_MONTH, String.valueOf(limitNum));
        bundle.putString(MONTH_MODE_TIME, String.valueOf(System.currentTimeMillis()));
        boolean isSuccess = false;
        if (!TextUtils.isEmpty(policyName)) {
            isSuccess = this.mDpm.setPolicy(admin, policyName, bundle);
        }
        Log.d(TAG, "saveMonthMode: " + isSuccess);
        return isSuccess;
    }

    private boolean saveWeekMode(boolean isOutgoing, int limitNum, ComponentName admin) {
        String policyName;
        if (isOutgoing) {
            policyName = "outgoing_week_limit";
        } else {
            policyName = "incoming_week_limit";
        }
        Bundle bundle = new Bundle();
        bundle.putString(WEEK, "true");
        bundle.putString(LIMIT_OF_WEEK, String.valueOf(limitNum));
        bundle.putString(WEEK_MODE_TIME, String.valueOf(System.currentTimeMillis()));
        boolean isSuccess = false;
        if (!TextUtils.isEmpty(policyName)) {
            isSuccess = this.mDpm.setPolicy(admin, policyName, bundle);
        }
        Log.d(TAG, "saveWeekMode: " + isSuccess);
        return isSuccess;
    }

    private boolean saveDayMode(boolean isOutgoing, int limitNum, ComponentName admin) {
        String policyName;
        if (isOutgoing) {
            policyName = "outgoing_day_limit";
        } else {
            policyName = "incoming_day_limit";
        }
        Bundle bundle = new Bundle();
        bundle.putString(DAY, "true");
        bundle.putString(LIMIT_OF_DAY, String.valueOf(limitNum));
        bundle.putString(DAY_MODE_TIME, String.valueOf(System.currentTimeMillis()));
        boolean isSuccess = false;
        if (!TextUtils.isEmpty(policyName)) {
            isSuccess = this.mDpm.setPolicy(admin, policyName, bundle);
        }
        Log.d(TAG, "saveDayMode: " + isSuccess);
        return isSuccess;
    }

    public boolean setIncomingSmsExceptionPattern(ComponentName admin, String pattern) {
        if (TextUtils.isEmpty(pattern)) {
            Log.d(TAG, "remove polciy: incoming_sms_exception_pattern");
            return removePattern(admin, INCOMING_SMS_EXCEPTION_PATTERN);
        }
        Bundle bundle = new Bundle();
        bundle.putString("value", pattern);
        return this.mDpm.setPolicy(admin, INCOMING_SMS_EXCEPTION_PATTERN, bundle);
    }

    public boolean setIncomingSmsRestriction(ComponentName admin, String pattern) {
        if (TextUtils.isEmpty(pattern)) {
            Log.d(TAG, "remove polciy: incoming_sms_restriction_pattern");
            return removePattern(admin, INCOMING_SMS_RESTRICTION_PATTERN);
        }
        Bundle bundle = new Bundle();
        bundle.putString("value", pattern);
        return this.mDpm.setPolicy(admin, INCOMING_SMS_RESTRICTION_PATTERN, bundle);
    }

    public boolean setOutgoingSmsExceptionPattern(ComponentName admin, String pattern) {
        if (TextUtils.isEmpty(pattern)) {
            Log.d(TAG, "remove polciy: outgoing_sms_exception_pattern");
            return removePattern(admin, OUTGOING_SMS_EXCEPTION_PATTERN);
        }
        Bundle bundle = new Bundle();
        bundle.putString("value", pattern);
        return this.mDpm.setPolicy(admin, OUTGOING_SMS_EXCEPTION_PATTERN, bundle);
    }

    public boolean setOutgoingSmsRestriction(ComponentName admin, String pattern) {
        if (TextUtils.isEmpty(pattern)) {
            Log.d(TAG, "remove polciy: outgoing_sms_restriction_pattern");
            return removePattern(admin, OUTGOING_SMS_RESTRICTION_PATTERN);
        }
        Bundle bundle = new Bundle();
        bundle.putString("value", pattern);
        return this.mDpm.setPolicy(admin, OUTGOING_SMS_RESTRICTION_PATTERN, bundle);
    }

    private boolean removePattern(ComponentName admin, String pattern) {
        return this.mDpm.removePolicy(admin, pattern, null);
    }

    public boolean setDefaultMainSlotCarrier(ComponentName admin, String carrier) {
        Log.d(TAG, "setDefaultMainSlotCarrier policy: " + carrier);
        if (carrier == null || carrier.isEmpty() || carrier.length() > CARRIER_MAX_LENGTH) {
            Log.e(TAG, "setDefaultMainSlotCarrier: Invalid entry in carrier, invocation failed");
            return false;
        }
        Bundle bundle = new Bundle();
        bundle.putString("value", carrier);
        return this.mDpm.setPolicy(admin, DEFAULT_MAIN_SLOT_CARRIER, bundle);
    }

    public String getDefaultMainSlotCarrier(ComponentName admin) {
        Bundle bundle = this.mDpm.getPolicy(admin, DEFAULT_MAIN_SLOT_CARRIER);
        if (bundle == null) {
            return "";
        }
        String carrier = bundle.getString("value");
        Log.d(TAG, "getDefaultMainSlotCarrier carrier: " + carrier);
        return carrier;
    }

    public boolean removeDefaultMainSlotCarrier(ComponentName admin) {
        return this.mDpm.removePolicy(admin, DEFAULT_MAIN_SLOT_CARRIER, null);
    }
}
