package com.ichsanudinstore.loka.util;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;

/**
 * @author Ichsanudin_Chairin
 * @since Wednesday 8/14/2019 6:43 PM
 */
public class FragmentUtil {

    private static FragmentTransaction fragmentTransaction;

    private static FragmentTransaction getfragmentTransaction(AppCompatActivity activity) {
        return getFragmentManager(activity).beginTransaction();
    }

    public static FragmentManager getFragmentManager(AppCompatActivity activity) {
        return (activity).getSupportFragmentManager();
    }

    public static Boolean isSameFragment(AppCompatActivity activity, Fragment fragment) {
        if (fragment.getClass().getSimpleName().equals(getCurrentFragment(getFragmentManager(activity)).getClass().getSimpleName())) {
            return true;
        }

        return false;
    }

    public static Fragment getCurrentFragment(FragmentManager fragmentManager) {
        try {
            List<Fragment> fragments = fragmentManager.getFragments();
            if (fragments.size() != 0) {
                for (Fragment fragment : fragments) {
                    if (fragment != null && fragment.isVisible())
                        return fragment;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void addFragment(AppCompatActivity activity, Fragment fragment, int id, boolean add_to_backstack) {
        fragmentTransaction = getfragmentTransaction(activity);
        fragmentTransaction.setCustomAnimations(android.R.animator.fade_in,
                android.R.animator.fade_out,
                android.R.animator.fade_in,
                android.R.animator.fade_out);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.add(id, fragment, fragment.getClass().getName());
        if (add_to_backstack)
            fragmentTransaction.addToBackStack(fragment.getClass().getName());
        fragmentTransaction.commit();
    }


    public static void replaceFragment(AppCompatActivity activity, Fragment fragment, int id, boolean add_to_backstack) {
        Fragment check_Fragment = getFragmentManager(activity).findFragmentByTag(fragment.getClass().getName());
        if (check_Fragment == null) {
            fragmentTransaction = getfragmentTransaction(activity)
                    .replace(id, fragment, fragment.getClass().getName());
            if (add_to_backstack)
                fragmentTransaction.addToBackStack(fragment.getClass().getName());
            fragmentTransaction.commit();
        } else {
            fragmentTransaction = getfragmentTransaction(activity);
            fragmentTransaction.replace(id, check_Fragment, check_Fragment.getClass().getName())
                    .addToBackStack(null)
                    .commit();
        }
    }
}
