package dsa.proyectoandroid.g6;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MyService extends Service {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Tu lógica aquí
        return START_STICKY; // Hace que el servicio se reinicie automáticamente si es eliminado
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Código para limpiar recursos si es necesario
    }
}
