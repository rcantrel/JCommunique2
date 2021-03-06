package com.demo;

import com.notification.NotificationFactory;
import com.notification.manager.QueueManager;
import com.notification.types.WindowNotification;
import com.platform.Platform;
import com.theme.ThemePackagePresets;
import com.utils.IconUtils;
import com.utils.Time;

public class QueueManagerDemo {
	public static void main(String[] args) throws Exception {
		Platform.instance().setAdjustForPlatform(true);

		NotificationFactory factory = new NotificationFactory(ThemePackagePresets.cleanDark());
		QueueManager manager = new QueueManager(NotificationFactory.SOUTHEAST);
		// sets how quickly old notifications should move out of the way for new ones
		manager.setSnapFactor(0.23);
		manager.setScrollDirection(QueueManager.ScrollDirection.NORTH);
		manager.setFadeTime(Time.seconds(3.0D));

		for (int i = 0; i < 10; i++) {
			int type = i % 3;
			WindowNotification note = null;
			switch (type) {
			case 0:
				note = factory.buildIconNotification("IconNotification", "Subtitle",
						IconUtils.createIcon("/com/demo/exclamation.png", 50, 50));
				break;
			case 1:
				note = factory.buildTextNotification("WWWWWWWWWWWWWWWWWWWWWWWWWWWWWW", "Subtitle");
				break;
			case 2:
				note = factory.buildAcceptNotification("AcceptNotification", "Do you accept?");
				break;
			}
			// when the notification is clicked, it should hide itself
			note.setCloseOnClick(true);
			// make it show in the queue for five seconds
			manager.addNotification(note, Time.seconds(10));
			// one second delay between creations
			Thread.sleep(1000);
		}

		Thread.sleep(20000);
		System.exit(1);
	}
}