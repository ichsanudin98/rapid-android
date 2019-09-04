package com.ichsanudinstore.loka.model;

import org.jetbrains.annotations.NotNull;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;

/**
 * @author Ichsanudin_Chairin
 * @since Thursday 8/15/2019 9:51 AM
 */
public class ModelMigration implements RealmMigration {
    @Override
    public void migrate(@NotNull DynamicRealm realm, long oldVersion, long newVersion) {
        if (oldVersion == 1) {
            oldVersion++;
        }

        if (oldVersion == 2) {
            oldVersion++;
        }
    }
}
