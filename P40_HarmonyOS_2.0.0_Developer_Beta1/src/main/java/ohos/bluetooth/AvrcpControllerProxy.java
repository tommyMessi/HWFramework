package ohos.bluetooth;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageOption;
import ohos.rpc.MessageParcel;
import ohos.rpc.RemoteException;

class AvrcpControllerProxy implements IAvrcpController {
    private static final int COMMAND_GET_DEVICES_BY_STATES_CONTROLLER = 1;
    private static final int COMMAND_GET_DEVICE_STATE_CONTROLLER = 2;
    private static final int DEFAULT_AVRCP_CONTROLLER_NUM = 5;
    private static final int ERR_OK = 0;
    private static final int MIN_TRANSACTION_ID = 1;
    private static final HiLogLabel TAG = new HiLogLabel(3, (int) LogHelper.BT_DOMAIN_ID, "AvrcpControllerProxy");
    private IRemoteObject remote;

    AvrcpControllerProxy() {
    }

    public IRemoteObject asObject() {
        this.remote = null;
        BluetoothHostProxy.getInstace().getSaProfileProxy(5).ifPresent(new Consumer() {
            /* class ohos.bluetooth.$$Lambda$AvrcpControllerProxy$2i_k0427yRVVt74itZ9uHZFHZFE */

            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                AvrcpControllerProxy.this.lambda$asObject$0$AvrcpControllerProxy((IRemoteObject) obj);
            }
        });
        return this.remote;
    }

    public /* synthetic */ void lambda$asObject$0$AvrcpControllerProxy(IRemoteObject iRemoteObject) {
        this.remote = iRemoteObject;
    }

    /* JADX INFO: finally extract failed */
    @Override // ohos.bluetooth.IAvrcpController
    public List<BluetoothRemoteDevice> getDevicesByStates(int[] iArr) {
        if (asObject() == null) {
            HiLog.error(TAG, "AvrcpControllerProxy getDevicesByStates : got null remote", new Object[0]);
            return new ArrayList();
        }
        MessageParcel obtain = MessageParcel.obtain();
        MessageParcel obtain2 = MessageParcel.obtain();
        MessageOption messageOption = new MessageOption(0);
        obtain.writeInterfaceToken("ohos.bluetooth.IBluetoothHost");
        obtain.writeIntArray(iArr);
        try {
            this.remote.sendRequest(1, obtain, obtain2, messageOption);
            int readInt = obtain2.readInt();
            if (readInt == 3756) {
                throw new SecurityException("Permission denied");
            } else if (readInt != 0) {
                HiLog.error(TAG, "getDevicesByStates : an error received after request sent, error:%{public}d", new Object[]{Integer.valueOf(readInt)});
                ArrayList arrayList = new ArrayList();
                obtain.reclaim();
                obtain2.reclaim();
                return arrayList;
            } else {
                ArrayList<BluetoothRemoteDevice> createDeviceList = Utils.createDeviceList(obtain2, 5);
                obtain.reclaim();
                obtain2.reclaim();
                return createDeviceList;
            }
        } catch (RemoteException unused) {
            HiLog.error(TAG, "AvrcpControllerProxy getDevicesByStates : call error", new Object[0]);
            obtain.reclaim();
            obtain2.reclaim();
            return new ArrayList();
        } catch (Throwable th) {
            obtain.reclaim();
            obtain2.reclaim();
            throw th;
        }
    }

    @Override // ohos.bluetooth.IAvrcpController
    public int getDeviceState(BluetoothRemoteDevice bluetoothRemoteDevice) {
        if (asObject() == null) {
            HiLog.error(TAG, "AvrcpControllerProxy getDeviceState : got null remote", new Object[0]);
            return 0;
        }
        MessageParcel obtain = MessageParcel.obtain();
        MessageParcel obtain2 = MessageParcel.obtain();
        MessageOption messageOption = new MessageOption(0);
        obtain.writeInterfaceToken("ohos.bluetooth.IBluetoothHost");
        obtain.writeSequenceable(bluetoothRemoteDevice);
        try {
            this.remote.sendRequest(2, obtain, obtain2, messageOption);
            int readInt = obtain2.readInt();
            if (readInt == 3756) {
                throw new SecurityException("Permission denied");
            } else if (readInt != 0) {
                HiLog.error(TAG, "getDeviceState : an error received after request sent, error:%{public}d", new Object[]{Integer.valueOf(readInt)});
                return 0;
            } else {
                int readInt2 = obtain2.readInt();
                obtain.reclaim();
                obtain2.reclaim();
                return readInt2;
            }
        } catch (RemoteException unused) {
            HiLog.error(TAG, "getDeviceState : call error", new Object[0]);
            return 0;
        } finally {
            obtain.reclaim();
            obtain2.reclaim();
        }
    }
}
