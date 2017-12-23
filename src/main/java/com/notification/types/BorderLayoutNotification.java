package com.notification.types;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.theme.WindowTheme;

/**
 * Lays out Swing Components in a BorderLayout.
 */
public class BorderLayoutNotification extends WindowNotification {
	private JPanel m_panel;
	private JPanel m_north_panel;
	private CloseButton closeButton;

	public static final int PANEL_PADDING = 10;

	public BorderLayoutNotification() {
		super();
		closeButton = new CloseButton(this);
		m_panel = new JPanel(new BorderLayout());
		m_panel.setBorder(new EmptyBorder(PANEL_PADDING, PANEL_PADDING, PANEL_PADDING, PANEL_PADDING));
		m_north_panel = new JPanel(new BorderLayout());
		m_north_panel.add(closeButton, BorderLayout.EAST);
		m_panel.add(m_north_panel, BorderLayout.NORTH);
		setPanel(m_panel);
	}

	/**
	 * Adds a Component to the Notification.
	 *
	 * @param comp
	 *            the Component to add
	 * @param borderLayout
	 *            the BorderLayout String, e.g. BorderLayout.NORTH
	 */
	public void addComponent(Component comp, String borderLayout) {
		if(BorderLayout.NORTH.equals(borderLayout)) {
			m_north_panel.add(comp, BorderLayout.CENTER);
		} else {
			m_panel.add(comp, borderLayout);
		}

		WindowTheme theme = this.getWindowTheme();
		if (theme != null) {
			comp.setBackground(theme.background);
			comp.setForeground(theme.foreground);
		}
		getWindow().revalidate();
		getWindow().repaint();
	}

	/**
	 * Removes a component.
	 *
	 * @param comp
	 *            the Component to remove
	 */
	public void removeComponent(Component comp) {
		m_panel.remove(comp);

		getWindow().revalidate();
		getWindow().repaint();
	}

	public void showCloseButton(boolean showCloseButton) {
		closeButton.setVisible(showCloseButton);
	}

	private class CloseButton extends JButton implements ActionListener {
		private static final long serialVersionUID = 1L;
		protected static final int BUTTON_HEIGHT = 20;
		protected static final int BUTTON_WIDTH = 20;
		protected Dimension buttonDimension = new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT);
		protected boolean isRollOver = false;
		protected Color rollOverBackGround = Color.RED;
		protected Color pressedBackGround = rollOverBackGround.brighter();
		private BorderLayoutNotification notif;

		public CloseButton(BorderLayoutNotification notif) {
			super();
			this.notif = notif;
			setFocusPainted(false);
			setBorderPainted(false);
			setOpaque(false);
			setFocusable(false);
			setContentAreaFilled(true);
			setIcon(null);

			addMouseListener(new java.awt.event.MouseAdapter() {
				@Override
				public void mouseEntered(java.awt.event.MouseEvent evt) {
					setIsRollOver(true);
				}

				@Override
				public void mouseExited(java.awt.event.MouseEvent evt) {
					setIsRollOver(false);
				}

				private void setIsRollOver(boolean mouseIn) {
					isRollOver = mouseIn;
					revalidate();
					repaint();
				}
			});

			addActionListener(this);
		}

		@Override
		public void paint(Graphics g) {
			boolean pressed = getModel().isPressed();
			boolean rolledOver = getModel().isRollover();

			Graphics2D g2 = (Graphics2D) g;
			// draw background
			g2.setColor(pressed ? pressedBackGround : isRollOver || rolledOver ? rollOverBackGround : getWindowTheme() != null ? getWindowTheme().background : Color.GRAY);//NOSONAR
			g2.fillRect(0, 0, BUTTON_WIDTH, BUTTON_HEIGHT - 1);

			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setColor(isRollOver ? Color.WHITE : Color.BLACK);
			g2.setStroke(new BasicStroke(1.4f));

			g2.drawLine(5, 5, 15, 15); // top
			g2.drawLine(15, 5, 5, 15); // top
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			notif.hide();
		}

		@Override
		public boolean isFocusTraversable() {
			return false;
		}

		@Override
		public void requestFocus() {
			// ignore request.
		}

		@Override
		public Dimension getMaximumSize() {
			return buttonDimension;
		}

		@Override
		public Dimension getMinimumSize() {
			return buttonDimension;
		}

		@Override
		public Dimension getPreferredSize() {
			return buttonDimension;
		}
	}
}
