package com.notification;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JPanel;
import javax.swing.JWindow;

import com.utils.Time;

/**
 * Provides the core methods that a Notification needs.
 */
public abstract class Notification {


	protected JWindow m_window;
	protected JPanel m_panel;
	private NotificationManager m_manager;
	private List<NotificationListener> m_listeners;
	private PauseOnMouseOverListener pauseOnMouseOverListener = new PauseOnMouseOverListener();
	private Timer removalTimer = null;
	private Time removalTime;

	public Notification() {
		m_listeners = new CopyOnWriteArrayList<>();
	}

	/**
	 * Listens for events on the Notification (e.g., a click).
	 *
	 * @param listener
	 *            the NotificationListener to add
	 */
	public void addNotificationListener(NotificationListener listener) {
		m_listeners.add(listener);
	}

	/**
	 * Removes a listener for events on the Notification (e.g., a click).
	 *
	 * @param listener
	 *            the NotificationListener to remove
	 */
	public void removeNotificationListener(NotificationListener listener) {
		m_listeners.remove(listener);
	}

	public void setRemovalTime(Time removalTime) {
		this.removalTime = removalTime;
	}

	public void pauseOnMouseOver(boolean pauseOnMouseOver) {
		if(pauseOnMouseOver) {
			m_window.addMouseListener(pauseOnMouseOverListener);
			m_panel.addMouseListener(pauseOnMouseOverListener);
		} else {
			m_window.removeMouseListener(pauseOnMouseOverListener);
			m_panel.addMouseListener(pauseOnMouseOverListener);
		}
	}

	/**
	 * Adds the removalTimer.
	 *
	 * @param Timer
	 *            the removal timer
	 */
	public void setRemovalTimer(Timer removalTimer) {
		this.removalTimer = removalTimer;
	}

	public void stopRemoval() {
		if(removalTimer != null) {
			removalTimer.cancel();
			removalTimer.purge();
		}
	}

	public void startRemovalTimer() {
		if(m_manager != null) {
			m_manager.scheduleRemoval(this, removalTime);
		}
	}

	/**
	 * @return whether or not this Notification has been added to a NotificationManager
	 */
	public boolean isManaged() {
		return m_manager != null;
	}

	/**
	 * @return the NotificationManager managing this Notification
	 */
	public NotificationManager getNotificationManager() {
		return m_manager;
	}

	protected void setNotificationManager(NotificationManager manager) {
		m_manager = manager;
	}

	/**
	 * Removes the Notification from the Manager. In some cases, this has the same effect as calling hide(); however,
	 * hide() doesn't invoke Manager-related things like fading, etc.
	 */
	public void removeFromManager() {
		m_manager.removeNotification(this);
	}

	protected void fireListeners(String action) {
		for (NotificationListener nl : m_listeners) {
			nl.actionCompleted(this, action);
		}
	}

	public abstract int getX();

	public abstract int getY();

	public abstract void setLocation(int x, int y);

	public abstract int getWidth();

	public abstract int getHeight();

	public abstract void setSize(int width, int height);

	public abstract double getOpacity();

	public abstract void setOpacity(double opacity);

	/**
	 * Reveals the Notification on the desktop.
	 */
	public abstract void show();

	/**
	 * Hides the Notification on the desktop.
	 */
	public abstract void hide();

	/**
	 * @return whether the Notification is being shown
	 */
	public abstract boolean isShown();

	class PauseOnMouseOverListener extends MouseAdapter {
		@Override
		public void mouseEntered(MouseEvent e) {
			stopRemoval();
		}

		@Override
		public void mouseExited(MouseEvent e) {
			startRemovalTimer();
		}
	}
}
