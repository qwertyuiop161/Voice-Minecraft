import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;

import org.vosk.Model;
import org.vosk.Recognizer;

public class Main extends JFrame {
    private boolean isEnabled = false, isCommandMode = true;
    private Robot robot;
    private TargetDataLine line;
    private Recognizer commandRec, dictationRec;
    private final Set<String> seenCommands = new HashSet<>();
    
    private JPanel statusLight;
    private JLabel statusLabel;
    private JToggleButton masterToggle;

    private static final Map<String, String> AUTO_FIX = Map.of(
        "sea", "see", "there", "their", "to", "too", "four", "for", "read", "red"
    );

    public Main() throws AWTException {
        robot = new Robot();
        robot.setAutoDelay(0);
        setupUI();
        new Thread(this::initVosk).start();
    }

    private void setupUI() {
        setTitle("Zero-Latency Voice Control");
        setSize(400, 220);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 1, 10, 10));

        JPanel statusRow = new JPanel(new FlowLayout());
        statusLight = new JPanel();
        statusLight.setPreferredSize(new Dimension(15, 15));
        statusLight.setBackground(Color.RED);
        statusLabel = new JLabel("Status: LOADING MODEL...");
        statusRow.add(statusLight); statusRow.add(statusLabel);

        masterToggle = new JToggleButton("System: OFF");
        masterToggle.setEnabled(false);
        JButton modeButton = new JButton("Mode: Command (Gaming)");

        masterToggle.addActionListener(e -> {
            isEnabled = masterToggle.isSelected();
            updateUIState();
        });

        modeButton.addActionListener(e -> {
            isCommandMode = !isCommandMode;
            modeButton.setText(isCommandMode ? "Mode: Command" : "Mode: Dictation");
            resetState();
        });

        add(statusRow); add(masterToggle); add(modeButton);
        setAlwaysOnTop(true);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initVosk() {
        try {
            // BEST MODEL: vosk-model-en-us-0.22 (Accurate + Real-time optimized)
            Model model = new Model("vosk-model-en-us-0.22");
            
            // Critical: grammar set to focus CPU on specific triggers for commands
            String grammar = "[\"jump\", \"shift\", \"sprint\", \"backspace\", \"j\", \"s\", \"c\", \"[unk]\"]";
            commandRec = new Recognizer(model, 16000f, grammar);
            dictationRec = new Recognizer(model, 16000f);

            AudioFormat format = new AudioFormat(16000, 16, 1, true, false);
            line = (TargetDataLine) AudioSystem.getLine(new DataLine.Info(TargetDataLine.class, format));
            line.open(format);
            line.start();

            SwingUtilities.invokeLater(() -> {
                statusLight.setBackground(Color.YELLOW);
                statusLabel.setText("Status: READY");
                masterToggle.setEnabled(true);
            });

            runAudioLoop();
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void runAudioLoop() {
        byte[] buffer = new byte[2048]; // Smaller buffer = more frequent processing
        while (true) {
            int read = line.read(buffer, 0, buffer.length);
            if (isEnabled && read > 0) {
                Recognizer active = isCommandMode ? commandRec : dictationRec;
                if (active.acceptWaveForm(buffer, read)) {
                    handleSpeech(parseJson(active.getResult()), true);
                } else {
                    handleSpeech(parseJson(active.getPartialResult()), false);
                }
            }
        }
    }

    private void handleSpeech(String text, boolean isFinal) {
        if (text.isEmpty()) return;
        
        // Fix: Remove all spaces to catch "back space" as "backspace"
        String condensed = text.toLowerCase().replace(" ", "");

        if (isCommandMode) {
            // Trigger instantly from partial result stream
            if (condensed.contains("jump") || condensed.contains("j")) triggerOnce("jump", KeyEvent.VK_SPACE);
            if (condensed.contains("shift") || condensed.contains("s")) triggerOnce("shift", KeyEvent.VK_SHIFT);
            if (condensed.contains("sprint") || condensed.contains("c")) triggerOnce("sprint", KeyEvent.VK_CONTROL);
        } else {
            // Handle backspace immediately, even in dictation
            if (condensed.endsWith("backspace")) {
                robot.keyPress(KeyEvent.VK_CONTROL);
                tap(KeyEvent.VK_BACK_SPACE);
                robot.keyRelease(KeyEvent.VK_CONTROL);
                dictationRec.reset(); // Instant reset to prevent "backspace" from typing
                return;
            } 
            
            if (isFinal) {
                for (String word : text.toLowerCase().split("\\s+")) {
                    type(AUTO_FIX.getOrDefault(word, word) + " ");
                }
            }
        }

        if (isFinal) resetState();
    }

    private void triggerOnce(String cmd, int key) {
        if (!seenCommands.contains(cmd)) {
            tap(key);
            seenCommands.add(cmd);
        }
    }

    private void resetState() {
        seenCommands.clear();
    }

    private void type(String s) {
        for (char c : s.toCharArray()) {
            int code = KeyEvent.getExtendedKeyCodeForChar(c);
            if (code != KeyEvent.VK_UNDEFINED) tap(code);
        }
    }

    private void tap(int key) {
        robot.keyPress(key);
        robot.keyRelease(key);
    }

    private void updateUIState() {
        statusLight.setBackground(isEnabled ? Color.GREEN : Color.YELLOW);
        statusLabel.setText(isEnabled ? "Status: LISTENING..." : "Status: READY");
        if (!isEnabled) resetState();
    }

    private String parseJson(String json) {
        String key = json.contains("\"text\"") ? "\"text\"" : "\"partial\"";
        int start = json.indexOf(key) + key.length() + 4;
        int end = json.indexOf("\"", start);
        return (start > key.length() && end > start) ? json.substring(start, end) : "";
    }

    public static void main(String[] args) throws AWTException {
        new Main();
    }
}
