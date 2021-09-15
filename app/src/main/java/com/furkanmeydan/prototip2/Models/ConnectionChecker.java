package com.furkanmeydan.prototip2.Models;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.furkanmeydan.prototip2.R;

import java.io.IOException;


public class ConnectionChecker {

    public AlertDialog dialog;
    View popupView;

    //bütün backend google üstünde yüklü olduğu için google'a ping göndermek
    //en mantıklı çözüm olacak gibi geliyor
    //True dönerse internet var
    //False dönerse internet yok
    public boolean isConnected() throws InterruptedException, IOException {
        String command = "ping -c 1 google.com";
        return Runtime.getRuntime().exec(command).waitFor() == 0;
    }

    public void showWindow(Activity activity) throws IOException, InterruptedException {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Bağlantı Hatası");
        builder.setMessage("Lütfen internet bağlantısını kontrol edin.");
        builder.setPositiveButton("Kapat", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        //dialog.setContentView(R.layout.popup_fill_profile);
        //dialog.show();
        builder.show();

    }

}
