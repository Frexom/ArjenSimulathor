package quoi.feur.arjensimulathor.services

import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothAdapter


class BluetoothService() {
    var adapter: BluetoothAdapter? = null

    init{
        BluetoothAdapter.getDefaultAdapter()
    }


}