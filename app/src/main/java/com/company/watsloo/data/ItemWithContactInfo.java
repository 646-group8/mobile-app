package com.company.watsloo.data;

import android.content.Context;
import android.graphics.Bitmap;

import java.util.List;
import java.util.concurrent.RecursiveTask;

/**
 * The Decorator base class for ItemInterface
 */
public abstract class ItemWithContactInfo implements ItemInterface {
    protected final ItemInterface decoratedItem;
    protected String contactInfo;

    public ItemWithContactInfo(ItemInterface decoratedItem, String contactInfo) {
        this.decoratedItem = decoratedItem;
        this.contactInfo = contactInfo;
    }

    @Override
    public String getName() {
        return decoratedItem.getName();
    }

    @Override
    public double getLatitude() {
        return decoratedItem.getLatitude();
    }

    @Override
    public double getLongitude() {
        return decoratedItem.getLongitude();
    }

    @Override
    public String getDescription() {
        return decoratedItem.getDescription();
    }

    @Override
    public List<String> getStories() {
        return decoratedItem.getStories();
    }

    @Override
    public List<Bitmap> getBitmaps() {
        return decoratedItem.getBitmaps();
    }

    @Override
    public boolean isAudited() {
        return decoratedItem.isAudited();
    }

    @Override
    public boolean isEasterEgg() {
        return decoratedItem.isEasterEgg();
    }

    public String getContactInfo() {
        return contactInfo;
    }

    @Override
    public void addItem(Context context) {
        decoratedItem.addItem(context);
    }
}
