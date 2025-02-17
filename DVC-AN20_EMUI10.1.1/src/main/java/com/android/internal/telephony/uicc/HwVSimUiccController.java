package com.android.internal.telephony.uicc;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import com.huawei.android.os.AsyncResultEx;
import com.huawei.android.os.RegistrantEx;
import com.huawei.android.os.RegistrantListEx;
import com.huawei.android.telephony.RlogEx;
import com.huawei.internal.telephony.CommandsInterfaceEx;
import com.huawei.internal.telephony.uicc.IccCardStatusExt;
import com.huawei.internal.telephony.uicc.IccRecordsEx;
import com.huawei.internal.telephony.uicc.UiccCardApplicationEx;
import com.huawei.internal.telephony.uicc.UiccCardExt;
import com.huawei.internal.telephony.uicc.UiccSlotEx;
import java.io.FileDescriptor;
import java.io.PrintWriter;

public class HwVSimUiccController extends Handler {
    private static final int EVENT_GET_ICC_STATUS_DONE = 2;
    private static final int EVENT_ICC_STATUS_CHANGED = 1;
    private static final String LOG_TAG = "VSimUiccController";
    private static HwVSimUiccController mInstance;
    private static final Object mLock = new Object();
    private CommandsInterfaceEx mCi;
    private Context mContext;
    protected RegistrantListEx mIccChangedRegistrants = new RegistrantListEx();
    private UiccSlotEx mUiccSlot;

    public static HwVSimUiccController make(Context c, CommandsInterfaceEx ci) {
        HwVSimUiccController hwVSimUiccController;
        synchronized (mLock) {
            if (mInstance != null) {
                throw new RuntimeException("VSimUiccController.make() should only be called once");
            }
            mInstance = new HwVSimUiccController(c, ci);
            hwVSimUiccController = mInstance;
        }
        return hwVSimUiccController;
    }

    private HwVSimUiccController(Context c, CommandsInterfaceEx ci) {
        logi("Creating VSimUiccController");
        this.mContext = c;
        this.mCi = ci;
        this.mCi.registerForIccStatusChanged(this, 1, (Object) null);
        this.mCi.registerForAvailable(this, 1, (Object) null);
    }

    public static HwVSimUiccController getInstance() {
        HwVSimUiccController hwVSimUiccController;
        synchronized (mLock) {
            if (mInstance == null) {
                throw new RuntimeException("VSimUiccController.getInstance can't be called before make()");
            }
            hwVSimUiccController = mInstance;
        }
        return hwVSimUiccController;
    }

    public UiccSlotEx getUiccSlot() {
        UiccSlotEx uiccSlotEx;
        synchronized (mLock) {
            uiccSlotEx = this.mUiccSlot;
        }
        return uiccSlotEx;
    }

    public UiccCardExt getUiccCard() {
        UiccCardExt uiccCard;
        synchronized (mLock) {
            uiccCard = this.mUiccSlot != null ? this.mUiccSlot.getUiccCard() : null;
        }
        return uiccCard;
    }

    public IccRecordsEx getIccRecords(int family) {
        IccRecordsEx iccRecordsEx;
        synchronized (mLock) {
            UiccCardApplicationEx app = getUiccCardApplication(family);
            if (app != null) {
                iccRecordsEx = app.getIccRecords();
            } else {
                iccRecordsEx = null;
            }
        }
        return iccRecordsEx;
    }

    public UiccCardApplicationEx getUiccCardApplication(int family) {
        UiccCardApplicationEx uiccCardApplicationEx;
        synchronized (mLock) {
            UiccCardExt c = getUiccCard();
            if (c != null) {
                uiccCardApplicationEx = c.getApplication(family);
            } else {
                uiccCardApplicationEx = null;
            }
        }
        return uiccCardApplicationEx;
    }

    public void registerForIccChanged(Handler h, int what, Object obj) {
        synchronized (mLock) {
            RegistrantEx r = new RegistrantEx(h, what, obj);
            this.mIccChangedRegistrants.add(r);
            r.notifyRegistrant();
        }
    }

    public void unregisterForIccChanged(Handler h) {
        synchronized (mLock) {
            this.mIccChangedRegistrants.remove(h);
        }
    }

    public void handleMessage(Message msg) {
        synchronized (mLock) {
            AsyncResultEx ar = AsyncResultEx.from(msg.obj);
            switch (msg.what) {
                case 1:
                    logi("Received EVENT_ICC_STATUS_CHANGED, calling getIccCardStatus");
                    this.mCi.getIccCardStatus(obtainMessage(2));
                    break;
                case 2:
                    logi("Received EVENT_GET_ICC_STATUS_DONE");
                    onGetIccCardStatusDone(ar);
                    break;
                default:
                    RlogEx.e(LOG_TAG, " Unknown Event " + msg.what);
                    break;
            }
        }
    }

    private synchronized void onGetIccCardStatusDone(AsyncResultEx ar) {
        if (ar != null) {
            if (ar.getException() == null) {
                IccCardStatusExt status = IccCardStatusExt.from(ar.getResult());
                if (this.mUiccSlot == null) {
                    this.mUiccSlot = new UiccSlotEx(this.mContext, true);
                }
                this.mUiccSlot.update(this.mCi, status, 2, 2);
                logi("Notifying IccChangedRegistrants");
                this.mIccChangedRegistrants.notifyRegistrants();
            }
        }
        RlogEx.e(LOG_TAG, "Error getting ICC status. RIL_REQUEST_GET_ICC_STATUS should never return an error");
    }

    private void logi(String string) {
        RlogEx.i(LOG_TAG, string);
    }

    public void dump(FileDescriptor fd, PrintWriter pw, String[] args) {
        if (!(fd == null || pw == null || args == null)) {
            int iccChangedRegistrantsSize = this.mIccChangedRegistrants.size();
            pw.println("VSimUiccController: " + this);
            pw.println(" mContext=" + this.mContext);
            synchronized (mLock) {
                pw.println(" mInstance=" + mInstance);
            }
            pw.println(" mIccChangedRegistrants: size=" + iccChangedRegistrantsSize);
            for (int i = 0; i < iccChangedRegistrantsSize; i++) {
                pw.println("  mIccChangedRegistrants[" + i + "]=" + this.mIccChangedRegistrants.get(i).getHandler());
            }
            pw.println();
            pw.flush();
            if (this.mUiccSlot == null) {
                pw.println("  mUiccSlot=null");
                return;
            }
            pw.println("  mUiccSlot=" + this.mUiccSlot);
            this.mUiccSlot.dump(fd, pw, args);
        }
    }
}
