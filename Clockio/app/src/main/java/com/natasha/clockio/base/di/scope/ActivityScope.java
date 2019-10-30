package com.natasha.clockio.base.di.scope;

import javax.inject.Scope;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

//https://www.journaldev.com/20405/android-dagger-2-retrofit-recyclerview/
@Scope
@Retention(RetentionPolicy.CLASS)
public @interface ActivityScope {
}
