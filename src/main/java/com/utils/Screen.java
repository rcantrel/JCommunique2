package com.utils;

import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;

import com.notification.Notification;
import com.notification.NotificationFactory;
import com.platform.Mac;
import com.platform.Platform;

public class Screen {
	private int mWidth = 0;
	private int mHeight = 0;

	private int mLeftX = 0;
	private int mCenterX = 0;
	private int mRightX = 0;

	private int mTopY = 0;
	private int mCenterY = 0;
	private int mBottomY = 0;

	private int mPadding = 0;

	private Platform platform = new Platform();

	private Screen(boolean spanMultipleMonitors, int padding) {
		mPadding = padding;
		setupDimensions(spanMultipleMonitors);
		calculatePositions();
	}

	public static Screen standard() {
		return new Screen(true, 60);
	}

	public static Screen withSpan(boolean spanMultipleMonitors) {
		return new Screen(spanMultipleMonitors, 60);
	}

	public static Screen withPadding(int padding) {
		return new Screen(true, padding);
	}

	public static Screen withSpanAndPadding(boolean spanMultipleMonitors, int padding) {
		return new Screen(spanMultipleMonitors, padding);
	}

	private void setupDimensions(boolean spanMultipleMonitors) {
		if (spanMultipleMonitors) {
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			mWidth = screenSize.width;
			mHeight = screenSize.height;

		} else {
			GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
			mWidth = gd.getDisplayMode().getWidth();
			mHeight = gd.getDisplayMode().getHeight();
		}
	}

	private void calculatePositions() {
		mLeftX = mPadding;
		mCenterX = (int) (mWidth / 2d);
		mRightX = mWidth - mPadding;

		mTopY = mPadding;
		mCenterY = (int) (mHeight / 2d);
		mBottomY = mHeight - mPadding - (platform.getOperatingSystem() instanceof Mac ? 50 : 0);
	}

	public int getX(String loc, Notification note) {
		switch (loc) {
		case NotificationFactory.SOUTHWEST:
			return mLeftX;
		case NotificationFactory.WEST:
			return mLeftX;
		case NotificationFactory.NORTHWEST:
			return mLeftX;
		case NotificationFactory.NORTH:
			return mCenterX - note.getWidth() / 2;
		case NotificationFactory.SOUTH:
			return mCenterX - note.getWidth() / 2;
		case NotificationFactory.SOUTHEAST:
			return mRightX - note.getWidth();
		case NotificationFactory.EAST:
			return mRightX - note.getWidth();
		case NotificationFactory.NORTHEAST:
			return mRightX - note.getWidth();
		default:
			return -1;
		}
	}

	public int getY(String loc, Notification note) {
		switch (loc) {
		case NotificationFactory.SOUTHWEST:
			return mBottomY - note.getHeight();
		case NotificationFactory.WEST:
			return mCenterY - note.getHeight() / 2;
		case NotificationFactory.NORTHWEST:
			return mTopY;
		case NotificationFactory.NORTH:
			return mTopY;
		case NotificationFactory.SOUTH:
			return mBottomY - note.getHeight();
		case NotificationFactory.SOUTHEAST:
			return mBottomY - note.getHeight();
		case NotificationFactory.EAST:
			return mCenterY - note.getHeight() / 2;
		case NotificationFactory.NORTHEAST:
			return mTopY;
		default:
			return -1;
		}
	}

	public int getPadding() {
		return mPadding;
	}
}
