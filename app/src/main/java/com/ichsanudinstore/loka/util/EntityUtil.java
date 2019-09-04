package com.ichsanudinstore.loka.util;

import android.content.Context;

import com.ichsanudinstore.loka.R;
import com.ichsanudinstore.loka.adapter.SpinnerAdapter;
import com.ichsanudinstore.loka.config.Constant;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

/**
 * @author Ichsanudin_Chairin
 * @since Saturday 8/24/2019 12:09 AM
 */
public class EntityUtil {
    /**
     * @param realm harus dalam keadaan sedang melakukan transaksi.
     **/
    public static void deleteWithClass(Class realmClass, Realm realm) {
        realm.where(realmClass).findAll().deleteAllFromRealm();
    }

    /**
     * @param realm harus dalam keadaan sedang melakukan transaksi.
     **/
    public static void deleteAll(Realm realm) {
        realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();
        realm.close();

        SharedPreferencesUtil.getInstance().begin();
        SharedPreferencesUtil.getInstance().remove(Constant.SharedPreferenceKey.SESSION_ID);
        SharedPreferencesUtil.getInstance().remove(Constant.SharedPreferenceKey.USER_ID);
        SharedPreferencesUtil.getInstance().remove(Constant.SharedPreferenceKey.NAME);
        SharedPreferencesUtil.getInstance().remove(Constant.SharedPreferenceKey.EMAIL);
        SharedPreferencesUtil.getInstance().remove(Constant.SharedPreferenceKey.COMPANY_ID);
        SharedPreferencesUtil.getInstance().remove(Constant.SharedPreferenceKey.ROLE_NAME);
        SharedPreferencesUtil.getInstance().remove(Constant.SharedPreferenceKey.ROLE_ID);
        SharedPreferencesUtil.getInstance().commit();
        SharedPreferencesUtil.getInstance().close();
    }

    public static ArrayList<SpinnerAdapter.Item> setGender(Context context) {
        ArrayList<SpinnerAdapter.Item> genderItems = new ArrayList<>();

        SpinnerAdapter.Item genderItem = new SpinnerAdapter.Item();
        genderItem.setIdentifier(1);
        genderItem.setDescription(LocaleUtil.getString(context, R.string.male));
        genderItems.add(genderItem);

        SpinnerAdapter.Item femaleItem = new SpinnerAdapter.Item();
        femaleItem.setIdentifier(2);
        femaleItem.setDescription(LocaleUtil.getString(context, R.string.female));
        genderItems.add(femaleItem);

        return genderItems;
    }

    public static ArrayList<SpinnerAdapter.Item> setStatus(Context context) {
        ArrayList<SpinnerAdapter.Item> statusItems = new ArrayList<>();

        SpinnerAdapter.Item available = new SpinnerAdapter.Item();
        available.setIdentifier(0);
        available.setDescription(LocaleUtil.getString(context, R.string.available));
        statusItems.add(available);

        SpinnerAdapter.Item unavailable = new SpinnerAdapter.Item();
        unavailable.setIdentifier(1);
        unavailable.setDescription(LocaleUtil.getString(context, R.string.unavailable));
        statusItems.add(unavailable);

        return statusItems;
    }

    public static ArrayList<SpinnerAdapter.Item> setOption(Context context, List<String> description) {
        ArrayList<SpinnerAdapter.Item> optionItems = new ArrayList<>();

        SpinnerAdapter.Item editOption = new SpinnerAdapter.Item();
        editOption.setIdentifier((byte) 1);
        editOption.setDescription(description.get(0));
        optionItems.add(editOption);

        SpinnerAdapter.Item detailOption = new SpinnerAdapter.Item();
        detailOption.setIdentifier((byte) 2);
        detailOption.setDescription(description.get(1));
        optionItems.add(detailOption);

        SpinnerAdapter.Item deleteOption = new SpinnerAdapter.Item();
        deleteOption.setIdentifier((byte) 3);
        deleteOption.setDescription(description.get(2));
        optionItems.add(deleteOption);

        return optionItems;
    }

    public static ArrayList<SpinnerAdapter.Item> setRentOption(Context context, List<String> description) {
        ArrayList<SpinnerAdapter.Item> optionItems = new ArrayList<>();

        SpinnerAdapter.Item assignOption = new SpinnerAdapter.Item();
        assignOption.setIdentifier((byte) 0);
        assignOption.setDescription(description.get(0));
        optionItems.add(assignOption);

        SpinnerAdapter.Item unassignOption = new SpinnerAdapter.Item();
        unassignOption.setIdentifier((byte) 1);
        unassignOption.setDescription(description.get(1));
        optionItems.add(unassignOption);

        return optionItems;
    }
}
