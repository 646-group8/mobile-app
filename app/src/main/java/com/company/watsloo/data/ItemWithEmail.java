package com.company.watsloo.data;

import android.content.Context;

public class ItemWithEmail extends ItemWithContactInfo {
    private static final String contactMethod = "email";

    public ItemWithEmail(ItemInterface decoratedItem, String email) {
        super(decoratedItem, email);
    }

    @Override
    public void addItem(Context context) {
        super.addItem(context);
        DataOperation.addContactInfo(context, super.getName(),
                contactMethod, super.getContactInfo());
    }
}
