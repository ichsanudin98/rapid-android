package com.ichsanudinstore.loka.util;

import androidx.appcompat.app.AppCompatActivity;

import com.ichsanudinstore.loka.R;
import com.ichsanudinstore.loka.adapter.SpinnerAdapter;
import com.ichsanudinstore.loka.bottomsheet.BaseUrlBottomSheet;
import com.ichsanudinstore.loka.bottomsheet.CategoryOfficeBottomSheet;
import com.ichsanudinstore.loka.bottomsheet.ChangePasswordBottomSheet;
import com.ichsanudinstore.loka.bottomsheet.ConfirmationBottomSheet;
import com.ichsanudinstore.loka.bottomsheet.MessageBottomSheet;
import com.ichsanudinstore.loka.bottomsheet.RentBottomSheet;
import com.ichsanudinstore.loka.bottomsheet.SpinnerBottomSheet;
import com.ichsanudinstore.loka.model.entity.CategoryOfficeModel;
import com.ichsanudinstore.loka.model.entity.SeatModel;

import java.util.ArrayList;

/**
 * @author Ichsanudin_Chairin
 * @since Friday 8/23/2019 3:10 PM
 */
public class BottomSheetUtil {

    public static void categoryOffice(
            AppCompatActivity appCompatActivity,
            CategoryOfficeBottomSheet.CategoryOfficeBottomSheetCallback callback,
            byte type,
            CategoryOfficeModel data
    ) {
        CategoryOfficeBottomSheet categoryOfficeBottomSheet = CategoryOfficeBottomSheet.newInstance(callback, type, data);
        categoryOfficeBottomSheet.show(appCompatActivity.getSupportFragmentManager(), CategoryOfficeBottomSheet.class.getSimpleName());
    }

    public static void changePassword(
            AppCompatActivity appCompatActivity,
            ChangePasswordBottomSheet.ChangePasswordBottomSheetCallback callback
    ) {
        ChangePasswordBottomSheet changePasswordBottomSheet = ChangePasswordBottomSheet.newInstance(callback);
        changePasswordBottomSheet.show(appCompatActivity.getSupportFragmentManager(), ChangePasswordBottomSheet.class.getSimpleName());
    }

    public static void rentSeat(
            AppCompatActivity appCompatActivity,
            RentBottomSheet.RentBottomSheetCallback callback,
            byte type,
            SeatModel data
    ) {
        RentBottomSheet rentBottomSheet = RentBottomSheet.newInstance(callback, type, data);
        rentBottomSheet.show(appCompatActivity.getSupportFragmentManager(), RentBottomSheet.class.getSimpleName());
    }

    public static void message(
            AppCompatActivity baseActivity,
            CharSequence title,
            CharSequence message,
            CharSequence dismiss,
            boolean showButton,
            boolean cancelable,
            MessageBottomSheet.MessageBottomSheetCallback messageBottomSheetCallback
    ) {
        MessageBottomSheet messageBottomSheet = MessageBottomSheet.newInstance(title, message, dismiss, showButton, messageBottomSheetCallback != null);

        if (messageBottomSheetCallback != null) {
            messageBottomSheet.callback(messageBottomSheetCallback);
        }

        messageBottomSheet.setCancelable(cancelable);

        if (!baseActivity.getSupportFragmentManager().isStateSaved()) {
            messageBottomSheet.show(baseActivity.getSupportFragmentManager(), MessageBottomSheet.class.getSimpleName());
        }
    }

    public static void message(
            AppCompatActivity baseActivity,
            CharSequence message,
            CharSequence dismiss,
            MessageBottomSheet.MessageBottomSheetCallback messageBottomSheetCallback
    ) {
        message(baseActivity, null, message, dismiss, true, true, messageBottomSheetCallback);
    }

    public static void message(
            AppCompatActivity baseActivity,
            CharSequence title,
            CharSequence message,
            CharSequence dismiss,
            Boolean isRequired
    ) {
        message(baseActivity, title, isRequired ? message + " " + LocaleUtil.getString(baseActivity.getApplicationContext(), R.string.required) : message, dismiss, true, true, null);
    }

    public static void message(
            AppCompatActivity baseActivity,
            CharSequence message,
            CharSequence dismiss
    ) {
        message(baseActivity, null, message, dismiss, true, true, null);
    }

    public static void message(
            AppCompatActivity baseActivity,
            CharSequence message,
            MessageBottomSheet.MessageBottomSheetCallback messageBottomSheetCallback
    ) {
        message(baseActivity, null, message, null, true, true, messageBottomSheetCallback);
    }

    public static void message(
            AppCompatActivity baseActivity,
            CharSequence message
    ) {
        message(baseActivity, null, message, null, true, true, null);
    }

    public static void confirmation(
            AppCompatActivity baseActivity,
            CharSequence title,
            CharSequence message,
            CharSequence negative,
            CharSequence positive,
            boolean cancelable,
            ConfirmationBottomSheet.ConfirmationBottomSheetCallback confirmationBottomSheetCallback
    ) {
        ConfirmationBottomSheet confirmationBottomSheet = ConfirmationBottomSheet.newInstance(title, message, negative, positive);

        if (confirmationBottomSheetCallback != null) {
            confirmationBottomSheet.callback(confirmationBottomSheetCallback);
        }

        confirmationBottomSheet.setCancelable(cancelable);
        confirmationBottomSheet.show(baseActivity.getSupportFragmentManager(), ConfirmationBottomSheet.class.getSimpleName());
    }

    public static void confirmation(
            AppCompatActivity baseActivity,
            CharSequence title,
            CharSequence message,
            CharSequence negative,
            CharSequence positive,
            ConfirmationBottomSheet.ConfirmationBottomSheetCallback confirmationBottomSheetCallback
    ) {
        confirmation(baseActivity, title, message, negative, positive, true, confirmationBottomSheetCallback);
    }

    public static void confirmation(
            AppCompatActivity baseActivity,
            CharSequence title,
            CharSequence negative,
            CharSequence positive,
            boolean cancelable,
            ConfirmationBottomSheet.ConfirmationBottomSheetCallback confirmationBottomSheetCallback
    ) {
        confirmation(baseActivity, title, null, negative, positive, cancelable, confirmationBottomSheetCallback);
    }

    public static void confirmation(
            AppCompatActivity baseActivity,
            CharSequence title,
            CharSequence negative,
            CharSequence positive,
            ConfirmationBottomSheet.ConfirmationBottomSheetCallback confirmationBottomSheetCallback
    ) {
        confirmation(baseActivity, title, null, negative, positive, true, confirmationBottomSheetCallback);
    }

    public static void baseUrl(
            AppCompatActivity appCompatActivity,
            BaseUrlBottomSheet.BaseUrlBottomSheetCallback baseUrlBottomSheetCallback
    ) {
        BaseUrlBottomSheet baseUrlBottomSheet = BaseUrlBottomSheet.newInstance(baseUrlBottomSheetCallback);
        baseUrlBottomSheet.show(appCompatActivity.getSupportFragmentManager(), BaseUrlBottomSheet.class.getSimpleName());
    }

    public static void spinner(
            AppCompatActivity appCompatActivity,
            CharSequence title,
            ArrayList<SpinnerAdapter.Item> items,
            SpinnerBottomSheet.SpinnerBottomSheetCallback spinnerBottomSheetCallback
    ) {
        SpinnerBottomSheet spinnerBottomSheet = SpinnerBottomSheet.newInstance(title, items);

        if (spinnerBottomSheetCallback != null) {
            spinnerBottomSheet.callback(spinnerBottomSheetCallback);
        }

        spinnerBottomSheet.show(appCompatActivity.getSupportFragmentManager(), SpinnerBottomSheet.class.getSimpleName());
    }
}
