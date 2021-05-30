package tippjatek.ui.matches;

import tippjatek.model.MatchScore;
import tippjatek.model.UIResultListener;
import tippjatek.ui.DisplayStrings;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.*;

public class MatchResultDialog extends JDialog implements ActionListener {
    private final JSlider homeSlider;
    private final JSlider awaySlider;
    private final JLabel scoreLabel = new JLabel("0 - 0");
    private final UIResultListener<MatchScore> listener;


    public MatchResultDialog(JFrame parent, String message, UIResultListener<MatchScore> listener) {
        super(parent, true);
        this.listener = listener;
        this.setTitle(DisplayStrings.RESULT_DIALOG_TITLE);
        homeSlider = new JSlider(0, 5);
        homeSlider.setValue(0);
        homeSlider.setOrientation(SwingConstants.VERTICAL);
        homeSlider.setPaintTicks(true);
        homeSlider.setPaintTrack(true);
        homeSlider.addChangeListener(this::updateScoreLabel);
        JButton homeExtender = new JButton(DisplayStrings.RESULT_DIALOG_SCALE);
        homeExtender.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                homeSlider.setMaximum(10);
            }
        });
        JPanel homeSliderPanel = new JPanel();
        homeSliderPanel.add(homeSlider);
        homeSliderPanel.add(homeExtender);
        homeSliderPanel.setLayout(new BoxLayout(homeSliderPanel, BoxLayout.Y_AXIS));
        awaySlider = new JSlider(0, 5);
        awaySlider.setValue(0);
        awaySlider.setOrientation(SwingConstants.VERTICAL);
        awaySlider.setPaintTicks(true);
        awaySlider.setPaintTrack(true);
        awaySlider.addChangeListener(this::updateScoreLabel);
        JButton awayExtender = new JButton(DisplayStrings.RESULT_DIALOG_SCALE);
        awayExtender.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                awaySlider.setMaximum(10);
            }
        });
        JPanel awaySliderPanel = new JPanel();
        awaySliderPanel.add(awaySlider);
        awaySliderPanel.add(awayExtender);
        awaySliderPanel.setLayout(new BoxLayout(awaySliderPanel, BoxLayout.Y_AXIS));
        scoreLabel.setFont(new Font(scoreLabel.getFont().getName(), Font.PLAIN, 30));
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JButton send = new JButton(DisplayStrings.SEND);
        send.addActionListener(this);
        this.add(BorderLayout.NORTH, new Label(message));
        this.add(BorderLayout.WEST, homeSliderPanel);
        this.add(BorderLayout.CENTER, scoreLabel);
        this.add(BorderLayout.EAST, awaySliderPanel);
        this.add(BorderLayout.SOUTH, send);
        this.setSize(350, 350);
        this.setVisible(true);
    }

    private void updateScoreLabel(ChangeEvent changeEvent) {
        scoreLabel.setText(String.format("%d - %d", homeSlider.getValue(), awaySlider.getValue()));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        listener.notify(new MatchScore(homeSlider.getValue(), awaySlider.getValue()));
        dispose();
    }
}
