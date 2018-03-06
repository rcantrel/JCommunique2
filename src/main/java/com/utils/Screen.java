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
	private int m_width = 0;
	private int m_height = 0;

	private int m_leftX = 0;
	private int m_centerX = 0;
	private int m_rightX = 0;

	private int m_topY = 0;
	private int m_centerY = 0;
	private int m_bottomY = 0;

	private int m_padding = 0;

	private Platform platform = new Platform();

	private Screen(boolean spanMultipleMonitors, int padding) {
		m_padding = padding;
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
			m_width = screenSize.width;
			m_height = screenSize.height;

		} else {
			GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
			m_width = gd.getDisplayMode().getWidth();
			m_height = gd.getDisplayMode().getHeight();
		}
	}

	private void calculatePositions() {
		m_leftX = m_padding;
		m_centerX = (int) (m_width / 2d);
		m_rightX = m_width - m_padding;

		m_topY = m_padding;
		m_centerY = (int) (m_height / 2d);
		m_bottomY = m_height - m_padding - (platform.getOperatingSystem() instanceof Mac ? 50 : 0);
	}

	public int getX(String loc, Notification note) {
		switch (loc) {
		case NotificationFactory.SOUTHWEST:
			return m_leftX;
		case NotificationFactory.WEST:
			return m_leftX;
		case NotificationFactory.NORTHWEST:
			return m_leftX;
		case NotificationFactory.NORTH:
			return m_centerX - note.getWidth() / 2;
		case NotificationFactory.SOUTH:
			return m_centerX - note.getWidth() / 2;
		case NotificationFactory.SOUTHEAST:
			return m_rightX - note.getWidth();
		case NotificationFactory.EAST:
			return m_rightX - note.getWidth();
		case NotificationFactory.NORTHEAST:
			return m_rightX - note.getWidth();
		default:
			return -1;
		}
	}

	public int getY(String loc, Notification note) {
		switch (loc) {
		case NotificationFactory.SOUTHWEST:
			return m_bottomY - note.getHeight();
		case NotificationFactory.WEST:
			return m_centerY - note.getHeight() / 2;
		case NotificationFactory.NORTHWEST:
			return m_topY;
		case NotificationFactory.NORTH:
			return m_topY;
		case NotificationFactory.SOUTH:
			return m_bottomY - note.getHeight();
		case NotificationFactory.SOUTHEAST:
			return m_bottomY - note.getHeight();
		case NotificationFactory.EAST:
			return m_centerY - note.getHeight() / 2;
		case NotificationFactory.NORTHEAST:
			return m_topY;
		default:
			return -1;
		}
	}

	public int getPadding() {
		return m_padding;
	}
}
